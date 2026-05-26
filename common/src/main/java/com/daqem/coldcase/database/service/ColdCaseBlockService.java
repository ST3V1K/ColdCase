package com.daqem.coldcase.database.service;

import com.daqem.coldcase.model.Operation;
import com.daqem.coldcase.model.history.BlockHistory;
import com.daqem.coldcase.model.history.IHistory;
import com.daqem.coldcase.model.history.UnreliableBlockHistory;
import com.daqem.coldcase.thread.OnComplete;
import com.daqem.coldcase.thread.ThreadManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Collectors;

public class ColdCaseBlockService {

    private final BlockService blockService;

    public ColdCaseBlockService(BlockService blockService) {
        this.blockService = blockService;
    }

    public List<IHistory> getBlockHistory(Level level, BlockPos pos) {
        return blockService.getBlockHistory(level, pos).stream()
                .map(this::createUnreliableHistory)
                .collect(Collectors.toList());
    }

    public void getBlockHistoryAsync(Level level, BlockPos pos, OnComplete<List<IHistory>> onComplete) {
        ThreadManager.submit(() -> getBlockHistory(level, pos), onComplete);
    }

    public List<IHistory> getInteractionHistory(Level level, BlockPos pos) {
        return blockService.getInteractionHistory(level, pos).stream()
                .filter(hist -> hist.getAction().getOperation() != Operation.NEUTRAL)
                .map(this::createUnreliableHistory)
                .collect(Collectors.toList());
    }

    public void getInteractionHistoryAsync(Level level, BlockPos pos, OnComplete<List<IHistory>> onComplete) {
        ThreadManager.submit(() -> getInteractionHistory(level, pos), onComplete);
    }

    public List<IHistory> getInteractionHistory(Level level, List<BlockPos> pos) {
        return blockService.getInteractionHistory(level, pos).stream()
                .filter(hist -> hist.getAction().getOperation() != Operation.NEUTRAL)
                .map(this::createUnreliableHistory)
                .collect(Collectors.toList());
    }

    public void getInteractionHistoryAsync(Level level, List<BlockPos> pos, OnComplete<List<IHistory>> onComplete) {
        ThreadManager.submit(() -> getInteractionHistory(level, pos), onComplete);
    }

    private IHistory createUnreliableHistory(IHistory history) {
        if (history instanceof BlockHistory blockHistory) {
            return new UnreliableBlockHistory(blockHistory);
        }
        return history;
    }
}
