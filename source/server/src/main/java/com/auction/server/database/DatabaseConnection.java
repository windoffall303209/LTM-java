package com.auction.server.database;

import com.auction.server.util.ConfigLoader;
import com.auction.server.util.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Quản lý kết nối database
 * Sử dụng singleton pattern
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private String url;
    private String username;
    private String password;
    
    private DatabaseConnection() {
        try {
            // Load config
            String host = ConfigLoader.getDbHost();
            int port = ConfigLoader.getDbPort();
            String dbName = ConfigLoader.getDbName();
            username = ConfigLoader.getDbUsername();
            password = ConfigLoader.getDbPassword();
            
            // Build connection URL
            url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    host, port, dbName);
            
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Logger.info("Database configuration loaded");
        } catch (ClassNotFoundException e) {
            Logger.error("MySQL Driver not found", e);
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Lấy connection, tự động reconnect nếu bị disconnect
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
            Logger.info("Database connected successfully");
        }
        return connection;
    }
    
    /**
     * Test connection
     */
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            Logger.error("Database connection test failed", e);
            return false;
        }
    }
    
    /**
     * Đóng connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            Logger.error("Error closing database connection", e);
        }
    }
}

