package com.daqem.coldcase.command;

import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.history.IHistory;
import com.daqem.coldcase.model.history.UnreliableBlockHistory;
import com.daqem.coldcase.model.history.UnreliableTransactionHistory;
import com.daqem.coldcase.network.ColdCaseNetwork;
import com.daqem.coldcase.util.ClueComponentUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClueNavigationCommand {

    // -------------------------------------------------------------------------
    // Server-side session state (cleared when server stops / player logs out)
    // -------------------------------------------------------------------------

    /**
     * Maps {@code playerUUID -> blockPos -> List<IHistory>}.
     * Stores the revealed clue list so navigation clicks can retrieve adjacent clues.
     */
    private static final Map<UUID, Map<BlockPos, List<IHistory>>> PLAYER_HISTORY_LISTS =
            new ConcurrentHashMap<>();

    /**
     * Maps {@code playerUUID -> blockPos -> messageId}.
     *
     * <p>The {@code messageId} is a stable UUID generated when the first clue is sent.
     * Every subsequent navigation packet for the same {@code (player, blockPos)} pair
     * reuses this UUID so the client can replace the existing chat line in-place
     * instead of appending a new one.
     */
    private static final Map<UUID, Map<BlockPos, UUID>> PLAYER_MESSAGE_IDS =
            new ConcurrentHashMap<>();

    // -------------------------------------------------------------------------
    // Command registration
    // -------------------------------------------------------------------------

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("coldcase")
                .then(Commands.literal("cluenav")
                        .then(Commands.argument("x", IntegerArgumentType.integer())
                                .then(Commands.argument("y", IntegerArgumentType.integer())
                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                .then(Commands.argument("index", IntegerArgumentType.integer())
                                                        .executes(ClueNavigationCommand::navigateClue)))))));
    }

    // -------------------------------------------------------------------------
    // Command execution
    // -------------------------------------------------------------------------

    private static int navigateClue(CommandContext<CommandSourceStack> context) {
        ServerPlayer serverPlayer = context.getSource().getPlayer();
        if (serverPlayer == null) {
            return 0;
        }

        BlockPos locationPos = new BlockPos(
                IntegerArgumentType.getInteger(context, "x"),
                IntegerArgumentType.getInteger(context, "y"),
                IntegerArgumentType.getInteger(context, "z")
        );
        int index = IntegerArgumentType.getInteger(context, "index");

        Map<BlockPos, List<IHistory>> playerHistories = PLAYER_HISTORY_LISTS.get(serverPlayer.getUUID());
        if (playerHistories == null) return 1;

        List<IHistory> histories = playerHistories.get(locationPos);
        if (histories == null || index < 0 || index >= histories.size()) return 1;

        // Retrieve the stable message UUID for this clue slot. If (somehow) it was
        // never stored, generate and save a fresh one – the client will add a new
        // line rather than replacing, which is safe as a fallback.
        UUID messageId = getPlayerMessageId(serverPlayer.getUUID(), locationPos)
                .orElseGet(() -> {
                    UUID freshId = UUID.randomUUID();
                    storePlayerMessageId(serverPlayer.getUUID(), locationPos, freshId);
                    return freshId;
                });

        IHistory history = histories.get(index);
        MutableComponent message;
        if (history instanceof UnreliableTransactionHistory transactionHistory) {
            message = transactionHistory.getClueComponent().copy();
        } else if (history instanceof UnreliableBlockHistory blockHistory) {
            message = blockHistory.getClueComponent().copy();
        } else {
            return 1;
        }

        message.append(ClueComponentUtils.createNavigationFooter(
                index,
                histories.size(),
                new BlockPosition(locationPos.getX(), locationPos.getY(), locationPos.getZ())
        ));

        // Send as a replaceable packet – the client will find the old line by
        // messageId and swap its content without appending a new chat line.
        ColdCaseNetwork.sendClueUpdate(serverPlayer, messageId, message);
        return 1;
    }

    // -------------------------------------------------------------------------
    // State helpers – called by MixinServerPlayer when the first clue is sent
    // -------------------------------------------------------------------------

    /**
     * Stores (or replaces) the revealed history list for a given player and location.
     * Must be called before the first {@link ColdCaseNetwork#sendClueUpdate} so that
     * subsequent navigation commands can look up adjacent clues.
     */
    public static void storePlayerHistoryList(UUID playerUuid, BlockPos locationPos, List<IHistory> histories) {
        PLAYER_HISTORY_LISTS
                .computeIfAbsent(playerUuid, k -> new ConcurrentHashMap<>())
                .put(locationPos, histories);
    }

    /**
     * Associates a stable {@code messageId} with the {@code (playerUuid, locationPos)}
     * pair so that every navigation click for this clue slot reuses the same UUID and
     * the client can perform an in-place chat-line replacement.
     *
     * <p>Call this once, immediately before sending the initial clue message.
     */
    public static void storePlayerMessageId(UUID playerUuid, BlockPos locationPos, UUID messageId) {
        PLAYER_MESSAGE_IDS
                .computeIfAbsent(playerUuid, k -> new ConcurrentHashMap<>())
                .put(locationPos, messageId);
    }

    /**
     * Returns the stored {@code messageId} for the given {@code (playerUuid, locationPos)},
     * or {@link Optional#empty()} if no clue has been sent yet for this pair.
     */
    public static Optional<UUID> getPlayerMessageId(UUID playerUuid, BlockPos locationPos) {
        Map<BlockPos, UUID> playerMessages = PLAYER_MESSAGE_IDS.get(playerUuid);
        if (playerMessages == null) return Optional.empty();
        return Optional.ofNullable(playerMessages.get(locationPos));
    }
}