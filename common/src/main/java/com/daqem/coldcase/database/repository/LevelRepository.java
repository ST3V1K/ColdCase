package com.daqem.coldcase.database.repository;

import com.daqem.coldcase.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LevelRepository extends Repository {

    private final Database database;

    public LevelRepository(Database database) {
        this.database = database;
    }

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS levels (
                	id integer PRIMARY KEY,
                	name text NOT NULL UNIQUE
                );
                """;
        if (isMysql()) {
            sql = """
                    CREATE TABLE IF NOT EXISTS levels (
                    	id int PRIMARY KEY AUTO_INCREMENT,
                    	name varchar(256) NOT NULL UNIQUE
                    )
                    ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4;
                    """;
        }
        database.createTable(sql);
    }

    public void insert(String name) {
        String query = """
                INSERT OR IGNORE INTO levels(name)
                VALUES(?);
                """;

        if (isMysql()) {
            query = """
                    INSERT IGNORE INTO levels(name)
                    VALUES(?);
                    """;
        }

        try {
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setString(1, name);
            database.queue.add(preparedStatement);
        } catch (SQLException exception) {
            com.daqem.coldcase.ColdCase.LOGGER.error("Failed to insert level into database", exception);
        }
    }
}
