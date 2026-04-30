package com.daqem.coldcase.database.service;

import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.repository.EntityRepository;
import com.daqem.coldcase.thread.ThreadManager;

public class EntityService {

    private final EntityRepository entityRepository;

    public EntityService(Database database) {
        this.entityRepository = new EntityRepository(database);
    }

    public void createTable() {
        entityRepository.createTable();
    }

    public void insert(String name) {
        entityRepository.insert(name);
    }
}
