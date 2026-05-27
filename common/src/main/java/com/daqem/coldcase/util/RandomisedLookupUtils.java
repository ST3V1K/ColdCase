package com.daqem.coldcase.util;

import com.daqem.coldcase.block.BlockHandler;
import com.daqem.coldcase.block.container.ContainerHandler;
import com.daqem.coldcase.config.ColdCaseCustomConfig;
import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.model.history.BlockHistory;
import com.daqem.coldcase.model.history.ContainerHistory;
import com.daqem.coldcase.model.history.IHistory;
import com.daqem.coldcase.model.history.UnreliableBlockHistory;
import com.daqem.coldcase.model.history.UnreliableContainerHistory;
import com.daqem.coldcase.model.history.UnreliableTransactionHistory;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
                direction != null ? pos.relative(direction) : pos,
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
        Services.COLD_CASE_CONTAINER.getContainerHistoryAsync(level, positions, containerHistory -> {
            List<IHistory> history = new ArrayList<>();

            containerHistory.stream()
                    .collect(Collectors.groupingBy(h -> new UnreliableTransactionHistory.TransactionKey(h.getOriginalTime(), h.getUser()
                            .getUUID())))
                    .forEach((key, transactions) -> {
                        List<ContainerHistory> containerTransactions = transactions.stream()
                                .map(t -> (ContainerHistory) t)
                                .toList();

                        int totalItems = containerTransactions.stream()
                                .mapToInt(t -> t.getItemStack().getCount())
                                .sum();

                        double transactionFactor = getTransactionFactor(totalItems);

                        List<UnreliableContainerHistory> unreliableTransactions = containerTransactions.stream()
                                .map(UnreliableContainerHistory::new)
                                .collect(Collectors.toList());

                        history.add(new UnreliableTransactionHistory(key, unreliableTransactions, transactionFactor));
                    });

            Services.COLD_CASE_BLOCK.getInteractionHistoryAsync(level, positions, interactionHistory -> {
                history.addAll(interactionHistory.stream()
                        .map(h -> new UnreliableBlockHistory((BlockHistory) h))
                        .toList());
                fetchBlockHistoryRecursive(level, new ArrayList<>(positions), history, combinedHistory -> {
                    combinedHistory.sort(Comparator.comparingLong(IHistory::getOriginalTime)
                            .reversed());
                    player.coldcase$sendMagnifyingGlassMessage(combinedHistory);
                });
            });
        });
    }

    private static double getTransactionFactor(int totalItems) {
        double minFactor = ColdCaseCustomConfig.minItemTransactionFactor.get();
        double maxFactor = ColdCaseCustomConfig.maxItemTransactionFactor.get();
        int minItems = ColdCaseCustomConfig.minItemsForFactor.get();
        int maxItems = ColdCaseCustomConfig.maxItemsForFactor.get();

        if (totalItems <= minItems) {
            return minFactor;
        }
        if (totalItems >= maxItems) {
            return maxFactor;
        }

        double slope = (maxFactor - minFactor) / (maxItems - minItems);
        return minFactor + (totalItems - minItems) * slope;
    }

    private static void fetchBlockHistoryRecursive(Level level, List<BlockPos> positions, List<IHistory> history, Consumer<List<IHistory>> callback) {
        if (positions.isEmpty()) {
            callback.accept(history);
            return;
        }
        BlockPos currentPos = positions.removeFirst();
        Services.COLD_CASE_BLOCK.getBlockHistoryAsync(level, currentPos, blockHistory -> {
            history.addAll(blockHistory.stream()
                    .map(h -> new UnreliableBlockHistory((BlockHistory) h))
                    .toList());
            fetchBlockHistoryRecursive(level, positions, history, callback);
        });
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