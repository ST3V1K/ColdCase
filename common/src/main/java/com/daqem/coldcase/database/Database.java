package com.daqem.coldcase.database;

import com.daqem.coldcase.config.ColdCaseConfig;
import com.daqem.coldcase.database.queue.IQueue;
import com.daqem.coldcase.database.queue.Queue;
import com.supermartijn642.configlib.ConfigLib;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.sql.*;
import java.util.List;

public class Database {

    @Nullable
    private Connection connection;
    @Nullable
    private Statement statement;
    public final IQueue queue;
    public final IQueue batchQueue;

    public Database() {
        queue = new Queue(this, false);
        batchQueue = new Queue(this, true);
    }

    public boolean createConnection() {
        boolean connected;
        if (ColdCaseConfig.useMysql.get()) {
            connected = createMysqlConnection();
        } else {
            connected = createSqliteConnection();
        }
        if (connection != null) {
            com.daqem.coldcase.ColdCase.LOGGER.info("Connected to database");
            try {
                statement = connection.createStatement();
            } catch (SQLException e) {
                com.daqem.coldcase.ColdCase.LOGGER.error("Failed to create statement", e);
                return false;
            }
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                com.daqem.coldcase.ColdCase.LOGGER.error("Failed to set auto commit", e);
                return false;
            }
        }
        return connected && connection != null && statement != null;
    }

    public boolean createMysqlConnection() {
        String host = ColdCaseConfig.mysqlHost.get();
        int port = ColdCaseConfig.mysqlPort.get();
        String database = ColdCaseConfig.mysqlDatabase.get();
        String user = ColdCaseConfig.mysqlUsername.get();
        String password = ColdCaseConfig.mysqlPassword.get();
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?allowReconnect=true&autoReconnect=true&connectTimeout=" + ColdCaseConfig.mysqlTimeout.get();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            com.daqem.coldcase.ColdCase.LOGGER.error("Failed to load MySQL driver", e);
            return false;
        }
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            com.daqem.coldcase.ColdCase.LOGGER.error("Failed to connect to MySQL database", e);
            return false;
        }
        return connection != null;
    }

    public boolean createSqliteConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            com.daqem.coldcase.ColdCase.LOGGER.error("Failed to load SQLite driver", e);
            return false;
        }
        Path path = ConfigLib.getConfigFolder().toPath().resolve(com.daqem.coldcase.ColdCase.MOD_ID);
        if (!path.toFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            path.toFile().mkdirs();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (SQLException e) {
            com.daqem.coldcase.ColdCase.LOGGER.error("Failed to connect to SQLite database", e);
            return false;
        }
        return connection != null;
    }

    public void createTable(String sql) {
        try {
            if (statement != null) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            com.daqem.coldcase.ColdCase.LOGGER.error("Failed to create table", e);
        }
    }

    public void execute(String sql, boolean logError) {
        try {
            if (statement != null) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            if (logError) {
                com.daqem.coldcase.ColdCase.LOGGER.error("Failed to execute statement", e);
            }
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        if (connection != null) {
            return connection.prepareStatement(query);
        } else {
            throw new SQLException("Connection is null");
        }
    }

    public void executeStatements(List<PreparedStatement> statements, boolean isBatch) {
        try {
            for (PreparedStatement statement : statements) {
                if (statement == null) {
                    com.daqem.coldcase.ColdCase.LOGGER.error("Statement is null");
                    continue;
                }

                if (statement.isClosed()) {
                    com.daqem.coldcase.ColdCase.LOGGER.error("Statement is closed");
                    continue;
                }

                try (statement) {
                    if (isBatch) {
                        statement.executeBatch(); // Execute as a batch
                    } else {
                        statement.executeUpdate(); // Execute individually
                    }
                }
            }
            if (!statements.isEmpty()) {
                if (connection != null) {
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            com.daqem.coldcase.ColdCase.LOGGER.error("Failed to execute statements", e);
        }
    }
}
