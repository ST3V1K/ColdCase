package com.daqem.coldcase.database.service;

import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.repository.MaterialRepository;
import com.daqem.coldcase.thread.ThreadManager;

public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(Database database) {
        this.materialRepository = new MaterialRepository(database);
    }

    public void createTable() {
        materialRepository.createTable();
    }

    public void insert(String material) {
        materialRepository.insert(material);
    }
}
