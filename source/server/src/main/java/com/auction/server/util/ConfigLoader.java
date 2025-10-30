package com.auction.server.util;

import java.io.*;
import java.util.Properties;

/**
 * Load config từ file config.properties
 */
public class ConfigLoader {
    private static Properties properties;
    
    static {
        properties = new Properties();
        loadConfig();
    }
    
    private static void loadConfig() {
        try {
            // Thử load từ resources
            InputStream input = ConfigLoader.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            
            if (input == null) {
                // Nếu không có trong resources, thử load từ file
                input = new FileInputStream("src/main/resources/config.properties");
            }
            
            properties.load(input);
            input.close();
            
            Logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            Logger.error("Failed to load configuration, using defaults", e);
            setDefaults();
        }
    }
    
    private static void setDefaults() {
        // Database defaults
        properties.setProperty("db.host", "localhost");
        properties.setProperty("db.port", "3306");
        properties.setProperty("db.name", "auction_db");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "");
        
        // Server defaults
        properties.setProperty("server.port", "8888");
        properties.setProperty("server.max.threads", "100");
        
        // Auction defaults
        properties.setProperty("auction.min.bid.increment", "100000");
        properties.setProperty("auction.extension.time", "60");
        properties.setProperty("auction.warning.time", "10");
    }
    
    public static String get(String key) {
        return properties.getProperty(key);
    }
    
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                Logger.warn("Invalid integer value for " + key + ", using default");
            }
        }
        return defaultValue;
    }
    
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }
    
    // Specific getters
    public static String getDbHost() {
        return get("db.host", "localhost");
    }
    
    public static int getDbPort() {
        return getInt("db.port", 3306);
    }
    
    public static String getDbName() {
        return get("db.name", "auction_db");
    }
    
    public static String getDbUsername() {
        return get("db.username", "root");
    }
    
    public static String getDbPassword() {
        return get("db.password", "");
    }
    
    public static int getServerPort() {
        return getInt("server.port", 8888);
    }
    
    public static int getMaxThreads() {
        return getInt("server.max.threads", 100);
    }
    
    public static long getMinBidIncrement() {
        return getInt("auction.min.bid.increment", 100000);
    }
    
    public static int getExtensionTime() {
        return getInt("auction.extension.time", 60);
    }
    
    public static int getWarningTime() {
        return getInt("auction.warning.time", 10);
    }
}

