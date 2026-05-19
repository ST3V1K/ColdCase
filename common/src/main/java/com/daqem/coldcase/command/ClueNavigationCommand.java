package com.daqem.coldcase.command;

import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.history.IHistory;
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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClueNavigationCommand {

    // Temporary storage for player-specific history lists
    // Player UUID -> BlockPos of Location -> List of IHistory
    private static final Map<UUID, Map<BlockPos, List<IHistory>>> PLAYER_HISTORY_LISTS = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("coldcase")
                .then(Commands.literal("cluenav")
                        .then(Commands.argument("x", IntegerArgumentType.integer())
                                .then(Commands.argument("y", IntegerArgumentType.integer())
                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                .then(Commands.argument("index", IntegerArgumentType.integer())
                                                        .executes(ClueNavigationCommand::navigateClue)))))));
    }

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

        if (playerHistories != null) {
            List<IHistory> histories = playerHistories.get(locationPos);
            if (histories != null && index >= 0 && index < histories.size()) {
                IHistory history = histories.get(index);
                MutableComponent message = history.getClueComponent().copy();

                // Always add navigation footer
                message.append(ClueComponentUtils.createNavigationFooter(index, histories.size(), new BlockPosition(locationPos.getX(), locationPos.getY(), locationPos.getZ())));

                serverPlayer.sendSystemMessage(message);
            }
        }
        return 1;
    }

    public static void storePlayerHistoryList(UUID playerUuid, BlockPos locationPos, List<IHistory> histories) {
        PLAYER_HISTORY_LISTS
                .computeIfAbsent(playerUuid, k -> new ConcurrentHashMap<>())
                .put(locationPos, histories);
    }
}
