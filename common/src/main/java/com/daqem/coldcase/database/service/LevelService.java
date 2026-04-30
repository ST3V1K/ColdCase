package com.daqem.coldcase.database.service;

import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.repository.LevelRepository;
import com.daqem.coldcase.thread.ThreadManager;

public class LevelService {

    private final LevelRepository levelRepository;

    public LevelService(Database database) {
        this.levelRepository = new LevelRepository(database);
    }

    public void createTable() {
        levelRepository.createTable();
    }

    public void insert(String name) {
        levelRepository.insert(name);
    }
}
