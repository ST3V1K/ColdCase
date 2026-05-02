package com.daqem.coldcase.database.repository;

import com.daqem.coldcase.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ToolRepository extends Repository {

    private final Database database;

    public ToolRepository(Database database) {
        this.database = database;
    }

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS tools (
                    id integer PRIMARY KEY AUTOINCREMENT,
                    name VARCHAR(255) UNIQUE NOT NULL
                );
                """;
        if (isMysql()) {
            sql = """
                    CREATE TABLE IF NOT EXISTS tools (
                        id int PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(255) UNIQUE NOT NULL
                    )
                    ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4;
                    """;
        }
        database.createTable(sql);

        // Insert air as default tool 1
        database.execute("INSERT OR IGNORE INTO tools(id, name) VALUES(1, 'minecraft:air')", true);
    }

    public void insert(String tool) {
        String query = """
                INSERT OR IGNORE INTO tools(name)
                VALUES(?);
                """;

        if (isMysql()) {
            query = """
                    INSERT IGNORE INTO tools(name)
                    VALUES(?);
                    """;
        }

        try {
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setString(1, tool);
            database.queue.add(preparedStatement);
        } catch (SQLException exception) {
            com.daqem.coldcase.ColdCase.LOGGER.error("Failed to insert tool into database", exception);
        }
    }
}
