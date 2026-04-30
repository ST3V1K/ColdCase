package com.daqem.coldcase.database.service;

import com.daqem.coldcase.command.filter.FilterList;
import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.repository.SessionRepository;
import com.daqem.coldcase.model.action.SessionAction;
import com.daqem.coldcase.model.history.SessionHistory;
import com.daqem.coldcase.thread.ThreadManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(Database database) {
        this.sessionRepository = new SessionRepository(database);
    }

    public void createTable() {
        sessionRepository.createTable();
    }

    public void createIndexes() {
        sessionRepository.createIndexes();
    }

    public void insert(UUID userUuid, Level level, BlockPos pos, SessionAction sessionAction) {
        sessionRepository.insert(
                System.currentTimeMillis(),
                userUuid.toString(),
                level.dimension().location().toString(),
                pos.getX(), pos.getY(), pos.getZ(),
                sessionAction.getId()
        );
    }

    public List<SessionHistory> getFilteredSessionHistory(Level level, FilterList filterList) {
        return sessionRepository.getFilteredSessionHistory(
                level.dimension().location().toString(),
                filterList
        );
    }
}
