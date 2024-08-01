package com.feyleya;

import com.feyleya.exception.DatabaseFileNotFoundException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionManager {
    private static HikariDataSource connectionPool;

    static {
        try {
            initConnectionPool();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error initializing the connection pool", e);
        }
    }

    private ConnectionManager() {
    }

    private static void initConnectionPool() throws URISyntaxException {
        Properties properties = new Properties();
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
        String path = properties.getProperty("database.path");
        if (path == null || !Files.exists(Paths.get(path))) {
            throw new DatabaseFileNotFoundException("Database file not found at specified path: " + path);
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + path);
        config.setMaximumPoolSize(10);
        connectionPool = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public static void closePool() {
        if (connectionPool != null && !connectionPool.isClosed()) {
            connectionPool.close();
        }
    }
}
