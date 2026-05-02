package com.daqem.coldcase.util;

import com.daqem.coldcase.block.BlockHandler;
import com.daqem.coldcase.block.container.ContainerHandler;
import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.model.history.IHistory;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import com.daqem.coldcase.thread.ThreadManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RandomisedLookupUtils {

    public static void performLookup(Player player, Level level, BlockPos pos, Direction direction) {
        if (!(player instanceof ColdCaseServerPlayer serverPlayer)) {
            return;
        }

        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof DoorBlock) {
            handleDoorLookup(serverPlayer, level, pos, state);
        } else if (!state.hasBlockEntity()) {
            handleBlockLookup(serverPlayer, level, pos, direction);
        } else {
            handleContainerLookup(serverPlayer, level, pos, state);
        }
    }

    private static void handleDoorLookup(ColdCaseServerPlayer player, Level level, BlockPos pos, BlockState state) {
        List<BlockPos> positions = new ArrayList<>(List.of(pos));
        BlockHandler.getSecondDoorPosition(pos, state).ifPresent(positions::add);
        Services.COLD_CASE_BLOCK.getInteractionHistoryAsync(
                level,
                positions,
                player::coldcase$sendMagnifyingGlassMessage);
    }

    private static void handleBlockLookup(ColdCaseServerPlayer player, Level level, BlockPos pos, Direction direction) {
        Services.COLD_CASE_BLOCK.getBlockHistoryAsync(
                level,
                pos.relative(direction),
                player::coldcase$sendMagnifyingGlassMessage);
    }

    private static void handleContainerLookup(ColdCaseServerPlayer player, Level level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        Optional<BaseContainerBlockEntity> container = ContainerHandler.getContainer(blockEntity);
        if (container.isEmpty()) {
            return;
        }

        if (!state.hasProperty(ChestBlock.TYPE)) {
            fetchAndSendContainerHistory(player, level, List.of(pos));
            return;
        }

        BlockPos connectionPos = getChestConnection(state, pos);
        if (connectionPos != null) {
            fetchAndSendContainerHistory(player, level, List.of(pos, connectionPos));
        } else {
            fetchAndSendContainerHistory(player, level, List.of(pos));
        }
    }

    private static void fetchAndSendContainerHistory(ColdCaseServerPlayer player, Level level, List<BlockPos> positions) {
        ThreadManager.submit(() -> {
            List<IHistory> history = new ArrayList<>();
            List<IHistory> containerHistory;

            if (positions.size() > 1) {
                containerHistory = Services.CONTAINER.getHistory(level, positions.get(0), positions.get(1));
            } else {
                containerHistory = Services.CONTAINER.getHistory(level, positions.getFirst());
            }

            List<IHistory> interactionHistory = Services.COLD_CASE_BLOCK.getInteractionHistory(level, positions);

            history.addAll(containerHistory);
            history.addAll(interactionHistory);
            history.sort((a, b) -> Long.compare(b.getTime().time(), a.getTime().time()));
            return history;
        }, player::coldcase$sendMagnifyingGlassMessage);
    }

    private static BlockPos getChestConnection(BlockState state, BlockPos pos) {
        Direction connectionDirection = state.getValue(ChestBlock.FACING);
        return switch (state.getValue(ChestBlock.TYPE)) {
            case LEFT -> switch (connectionDirection) {
                case NORTH -> pos.east();
                case SOUTH -> pos.west();
                case WEST -> pos.north();
                case EAST -> pos.south();
                default -> null;
            };
            case RIGHT -> switch (connectionDirection) {
                case NORTH -> pos.west();
                case SOUTH -> pos.east();
                case WEST -> pos.south();
                case EAST -> pos.north();
                default -> null;
            };
            default -> null;
        };
    }
}
