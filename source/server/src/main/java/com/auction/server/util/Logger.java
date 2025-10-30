package com.auction.server.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple logger để ghi log vào file và console
 */
public class Logger {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "server.log";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static PrintWriter logWriter;
    
    static {
        try {
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            File logFile = new File(LOG_DIR, LOG_FILE);
            logWriter = new PrintWriter(new FileWriter(logFile, true), true);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }
    
    public static void info(String message) {
        log("INFO", message);
    }
    
    public static void warn(String message) {
        log("WARN", message);
    }
    
    public static void error(String message) {
        log("ERROR", message);
    }
    
    public static void error(String message, Throwable throwable) {
        log("ERROR", message + " - " + throwable.getMessage());
        if (logWriter != null) {
            throwable.printStackTrace(logWriter);
        }
    }
    
    public static void debug(String message) {
        log("DEBUG", message);
    }
    
    private static void log(String level, String message) {
        String timestamp = DATE_FORMAT.format(new Date());
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // Ghi ra console
        System.out.println(logMessage);
        
        // Ghi vào file
        if (logWriter != null) {
            logWriter.println(logMessage);
            logWriter.flush();
        }
    }
    
    public static void close() {
        if (logWriter != null) {
            logWriter.close();
        }
    }
}

