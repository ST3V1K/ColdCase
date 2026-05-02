package com.daqem.coldcase.database.service;

import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.repository.SkinColorRepository;

public class SkinColorService {

    private final SkinColorRepository skinColorRepository;

    public SkinColorService(Database database) {
        this.skinColorRepository = new SkinColorRepository(database);
    }

    public void createTable() {
        skinColorRepository.createTable();
    }

    public void insert(String skinColor) {
        skinColorRepository.insert(skinColor);
    }
}
