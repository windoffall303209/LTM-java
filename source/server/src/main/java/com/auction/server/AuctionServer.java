package com.auction.server;

import com.auction.server.controller.ClientHandler;
import com.auction.server.database.DatabaseConnection;
import com.auction.server.service.*;
import com.auction.server.util.ConfigLoader;
import com.auction.server.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Main Server Class - Entry Point
 * Multi-threaded TCP Socket Server cho hệ thống đấu giá
 */
public class AuctionServer {
    
    // Server configuration
    private static final int PORT = ConfigLoader.getServerPort();
    private static final int MAX_THREADS = ConfigLoader.getMaxThreads();
    
    // Connected clients
    private static final Set<ClientHandler> connectedClients = ConcurrentHashMap.newKeySet();
    
    // Thread pool for handling clients
    private static ExecutorService threadPool;
    
    // Services
    private static AuthService authService;
    private static AuctionService auctionService;
    private static BidService bidService;
    private static BroadcastService broadcastService;
    
    // Server state
    private static volatile boolean running = true;
    
    public static void main(String[] args) {
        Logger.info("========================================");
        Logger.info("   AUCTION SYSTEM SERVER STARTING");
        Logger.info("========================================");
        
        // Initialize services
        initializeServices();
        
        // Test database connection
        if (!testDatabaseConnection()) {
            Logger.error("Failed to connect to database. Exiting...");
            return;
        }
        
        // Start timer thread for auction management
        startAuctionTimerThread();
        
        // Start server
        startServer();
    }
    
    /**
     * Initialize all services
     */
    private static void initializeServices() {
        Logger.info("Initializing services...");
        
        try {
            authService = new AuthService();
            auctionService = new AuctionService();
            bidService = new BidService();
            broadcastService = new BroadcastService();
            
            Logger.info("Services initialized successfully");
        } catch (Exception e) {
            Logger.error("Failed to initialize services", e);
            System.exit(1);
        }
    }
    
    /**
     * Test database connection
     */
    private static boolean testDatabaseConnection() {
        Logger.info("Testing database connection...");
        
        try {
            boolean connected = DatabaseConnection.getInstance().testConnection();
            if (connected) {
                Logger.info("Database connected successfully");
            } else {
                Logger.error("Database connection failed");
            }
            return connected;
        } catch (Exception e) {
            Logger.error("Database error", e);
            return false;
        }
    }
    
    /**
     * Start the main server
     */
    private static void startServer() {
        // Create thread pool
        threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Get server IP
            String serverIP = InetAddress.getLocalHost().getHostAddress();
            
            Logger.info("========================================");
            Logger.info("Server started successfully!");
            Logger.info("Port: " + PORT);
            Logger.info("Server IP: " + serverIP);
            Logger.info("Max threads: " + MAX_THREADS);
            Logger.info("Clients can connect to: " + serverIP + ":" + PORT);
            Logger.info("========================================");
            Logger.info("Waiting for clients...");
            
            // Accept clients loop
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    
                    // Create client handler
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    connectedClients.add(clientHandler);
                    
                    // Submit to thread pool
                    threadPool.execute(clientHandler);
                    
                    Logger.info("New client connected from: " + 
                               clientSocket.getInetAddress().getHostAddress() + 
                               " (Total clients: " + connectedClients.size() + ")");
                    
                } catch (IOException e) {
                    if (running) {
                        Logger.error("Error accepting client", e);
                    }
                }
            }
            
        } catch (IOException e) {
            Logger.error("Server error", e);
        } finally {
            shutdown();
        }
    }
    
    /**
     * Start auction timer thread
     * Kiểm tra và kết thúc các auction hết hạn
     */
    private static void startAuctionTimerThread() {
        Thread timerThread = new Thread(() -> {
            Logger.info("Auction timer thread started");
            
            while (running) {
                try {
                    // Check expired auctions every 5 seconds
                    Thread.sleep(5000);
                    
                    // Check and end expired auctions
                    auctionService.checkExpiredAuctions();
                    
                } catch (InterruptedException e) {
                    Logger.info("Timer thread interrupted");
                    break;
                } catch (Exception e) {
                    Logger.error("Error in timer thread", e);
                }
            }
        });
        
        timerThread.setDaemon(true);
        timerThread.start();
    }
    
    /**
     * Broadcast message to all connected clients
     */
    public static void broadcast(String message) {
        Logger.info("Broadcasting to " + connectedClients.size() + " clients: " + message);
        
        for (ClientHandler client : connectedClients) {
            try {
                client.sendMessage(message);
            } catch (Exception e) {
                Logger.error("Failed to broadcast to client", e);
            }
        }
    }
    
    /**
     * Broadcast to specific clients
     */
    public static void broadcastToClients(Set<ClientHandler> clients, String message) {
        for (ClientHandler client : clients) {
            try {
                client.sendMessage(message);
            } catch (Exception e) {
                Logger.error("Failed to send to client", e);
            }
        }
    }
    
    /**
     * Remove client from connected list
     */
    public static void removeClient(ClientHandler client) {
        connectedClients.remove(client);
        Logger.info("Client removed (Remaining: " + connectedClients.size() + ")");
    }
    
    /**
     * Shutdown server gracefully
     */
    private static void shutdown() {
        Logger.info("Shutting down server...");
        running = false;
        
        // Close all client connections
        for (ClientHandler client : connectedClients) {
            client.close();
        }
        
        // Shutdown thread pool
        if (threadPool != null) {
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
            }
        }
        
        // Close database connection
        DatabaseConnection.getInstance().closeConnection();
        
        Logger.info("Server shutdown complete");
    }
    
    // Getters for services
    public static AuthService getAuthService() {
        return authService;
    }
    
    public static AuctionService getAuctionService() {
        return auctionService;
    }
    
    public static BidService getBidService() {
        return bidService;
    }
    
    public static BroadcastService getBroadcastService() {
        return broadcastService;
    }
    
    public static Set<ClientHandler> getConnectedClients() {
        return connectedClients;
    }
}

