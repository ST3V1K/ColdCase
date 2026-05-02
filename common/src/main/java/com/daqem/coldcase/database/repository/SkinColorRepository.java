package com.daqem.coldcase.database.repository;

import com.daqem.coldcase.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SkinColorRepository extends Repository {

    private final Database database;

    public SkinColorRepository(Database database) {
        this.database = database;
    }

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS skin_colors (
                    id integer PRIMARY KEY AUTOINCREMENT,
                    name VARCHAR(32) UNIQUE NOT NULL
                );
                """;
        if (isMysql()) {
            sql = """
                    CREATE TABLE IF NOT EXISTS skin_colors (
                        id int PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(32) UNIQUE NOT NULL
                    )
                    ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4;
                    """;
        }
        database.createTable(sql);

        // Insert defaults
        database.execute("INSERT OR IGNORE INTO skin_colors(id, name) VALUES(1, 'unknown')", true);
    }

    public void insert(String skinColor) {
        String query = """
                INSERT OR IGNORE INTO skin_colors(name)
                VALUES(?);
                """;

        if (isMysql()) {
            query = """
                    INSERT IGNORE INTO skin_colors(name)
                    VALUES(?);
                    """;
        }

        try {
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setString(1, skinColor);
            database.queue.add(preparedStatement);
        } catch (SQLException exception) {
            com.daqem.coldcase.ColdCase.LOGGER.error("Failed to insert skin color into database", exception);
        }
    }
}
