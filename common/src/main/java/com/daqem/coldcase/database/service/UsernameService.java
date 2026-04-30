package com.daqem.coldcase.database.service;

import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.repository.UsernameRepository;
import com.daqem.coldcase.thread.ThreadManager;

import java.util.UUID;

public class UsernameService {

    private final UsernameRepository usernameRepository;

    public UsernameService(Database database) {
        this.usernameRepository = new UsernameRepository(database);
    }

    public void createTable() {
        usernameRepository.createTable();
    }

    public void insert(UUID uuid, String name) {
        usernameRepository.insert(System.currentTimeMillis(), uuid.toString(), name);
    }
}
