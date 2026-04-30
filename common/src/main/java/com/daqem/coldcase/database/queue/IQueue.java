package com.daqem.coldcase.database.queue;

import java.sql.PreparedStatement;

public interface IQueue {

    void add(PreparedStatement statement);
    void execute();
    void hello();
}
