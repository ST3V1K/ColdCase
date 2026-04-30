package com.daqem.coldcase.database.repository;

import com.daqem.coldcase.config.ColdCaseConfig;

public abstract class Repository implements IRepository {

    @Override
    public boolean isMysql() {
        return ColdCaseConfig.useMysql.get();
    }
}
