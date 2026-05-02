package com.daqem.coldcase.database.service;

import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.repository.ToolRepository;

public class ToolService {

    private final ToolRepository toolRepository;

    public ToolService(Database database) {
        this.toolRepository = new ToolRepository(database);
    }

    public void createTable() {
        toolRepository.createTable();
    }

    public void insert(String tool) {
        toolRepository.insert(tool);
    }
}
