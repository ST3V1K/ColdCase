package com.daqem.coldcase.database.service;

import com.daqem.coldcase.model.Operation;
import com.daqem.coldcase.model.history.ContainerHistory;
import com.daqem.coldcase.model.history.IHistory;
import com.daqem.coldcase.model.history.UnreliableContainerHistory;
import com.daqem.coldcase.thread.OnComplete;
import com.daqem.coldcase.thread.ThreadManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ColdCaseContainerService {

    private final ContainerService containerService;

    public ColdCaseContainerService(ContainerService containerService) {
        this.containerService = containerService;
    }

    public List<IHistory> getContainerHistory(Level level, List<BlockPos> pos) {
        List<IHistory> history = new ArrayList<>();
        for (BlockPos blockPos : pos) {
            history.addAll(containerService.getHistory(level, blockPos).stream()
                    .filter(hist -> hist.getAction() != null && hist.getAction().getOperation() != Operation.NEUTRAL)
                    .map(this::createUnreliableHistory)
                    .toList());
        }
        return history;
    }

    public void getContainerHistoryAsync(Level level, List<BlockPos> pos, OnComplete<List<IHistory>> onComplete) {
        ThreadManager.submit(() -> getContainerHistory(level, pos), onComplete);
    }

    private IHistory createUnreliableHistory(IHistory history) {
        if (history instanceof ContainerHistory containerHistory) {
            return new UnreliableContainerHistory(containerHistory, 1.0);
        }
        return history;
    }
}