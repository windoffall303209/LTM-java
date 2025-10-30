package com.auction.server.controller;

import com.auction.common.protocol.MessageType;
import com.auction.common.protocol.Protocol;
import com.auction.server.AuctionServer;
import com.auction.server.model.Auction;
import com.auction.server.model.Bid;
import com.auction.server.model.User;
import com.auction.server.util.Logger;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * ClientHandler - Xử lý mỗi client connection trong 1 thread riêng
 */
public class ClientHandler implements Runnable {
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private User currentUser;
    private volatile boolean running;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.running = true;
    }
    
    @Override
    public void run() {
        try {
            // Setup I/O streams
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            Logger.info("ClientHandler started for: " + socket.getInetAddress());
            
            // Main message loop
            String message;
            while (running && (message = in.readLine()) != null) {
                handleMessage(message);
            }
            
        } catch (IOException e) {
            Logger.error("ClientHandler I/O error", e);
        } finally {
            close();
        }
    }
    
    /**
     * Handle incoming message
     */
    private void handleMessage(String message) {
        Logger.info("Received from " + getClientInfo() + ": " + message);
        
        try {
            // Parse message
            String[] parts = Protocol.parseMessage(message);
            if (parts.length == 0) return;
            
            String command = parts[0];
            
            // Route to appropriate handler
            switch (command) {
                case MessageType.LOGIN:
                    handleLogin(parts);
                    break;
                    
                case MessageType.REGISTER:
                    handleRegister(parts);
                    break;
                    
                case MessageType.LOGOUT:
                    handleLogout();
                    break;
                    
                case MessageType.LIST_AUCTIONS:
                    handleListAuctions();
                    break;
                    
                case MessageType.GET_AUCTION:
                    handleGetAuction(parts);
                    break;
                    
                case MessageType.JOIN_AUCTION:
                    handleJoinAuction(parts);
                    break;
                    
                case MessageType.BID:
                    handleBid(parts);
                    break;
                    
                case MessageType.AUTO_BID:
                    handleAutoBid(parts);
                    break;
                    
                case MessageType.GET_HISTORY:
                    handleGetHistory(parts);
                    break;
                    
                case MessageType.CREATE_AUCTION:
                    handleCreateAuction(parts);
                    break;
                    
                case MessageType.DELETE_AUCTION:
                    handleDeleteAuction(parts);
                    break;
                    
                default:
                    sendMessage(Protocol.buildMessage(MessageType.ERROR, "Unknown command: " + command));
            }
            
        } catch (Exception e) {
            Logger.error("Error handling message", e);
            sendMessage(Protocol.buildMessage(MessageType.ERROR, e.getMessage()));
        }
    }
    
    /**
     * Handle LOGIN
     */
    private void handleLogin(String[] parts) {
        if (parts.length < 3) {
            sendMessage(Protocol.buildMessage(MessageType.LOGIN_FAILED, "Invalid format"));
            return;
        }
        
        String username = parts[1];
        String password = parts[2];
        
        try {
            User user = AuctionServer.getAuthService().login(username, password);
            currentUser = user;
            
            sendMessage(Protocol.buildMessage(
                MessageType.LOGIN_SUCCESS,
                String.valueOf(user.getId()),
                user.getUsername(),
                user.getRole()
            ));
            
            Logger.info("User logged in: " + username);
            
        } catch (IllegalArgumentException e) {
            sendMessage(Protocol.buildMessage(MessageType.LOGIN_FAILED, e.getMessage()));
        }
    }
    
    /**
     * Handle REGISTER
     */
    private void handleRegister(String[] parts) {
        if (parts.length < 4) {
            sendMessage(Protocol.buildMessage(MessageType.REGISTER_FAILED, "Invalid format"));
            return;
        }
        
        String username = parts[1];
        String password = parts[2];
        String email = parts[3];
        
        try {
            User user = AuctionServer.getAuthService().register(username, password, email);
            if (user != null) {
                sendMessage(Protocol.buildMessage(MessageType.REGISTER_SUCCESS, "Registration successful"));
                Logger.info("User registered: " + username);
            } else {
                sendMessage(Protocol.buildMessage(MessageType.REGISTER_FAILED, "Registration failed"));
            }
        } catch (IllegalArgumentException e) {
            sendMessage(Protocol.buildMessage(MessageType.REGISTER_FAILED, e.getMessage()));
        }
    }
    
    /**
     * Handle LOGOUT
     */
    private void handleLogout() {
        if (currentUser != null) {
            Logger.info("User logged out: " + currentUser.getUsername());
            currentUser = null;
        }
    }
    
    /**
     * Handle LIST_AUCTIONS
     */
    private void handleListAuctions() {
        List<Auction> auctions = AuctionServer.getAuctionService().getActiveAuctions();
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < auctions.size(); i++) {
            Auction a = auctions.get(i);
            sb.append(a.getId()).append(":");
            sb.append(a.getProduct() != null ? a.getProduct().getName() : "Unknown").append(":");
            sb.append(a.getCurrentPrice()).append(":");
            sb.append(a.getTimeLeft()).append(":");
            sb.append(a.getWinnerUsername() != null ? a.getWinnerUsername() : "");
            
            if (i < auctions.size() - 1) {
                sb.append(";");
            }
        }
        
        sendMessage(Protocol.buildMessage(MessageType.AUCTION_LIST, sb.toString()));
    }
    
    /**
     * Handle GET_AUCTION
     */
    private void handleGetAuction(String[] parts) {
        if (parts.length < 2) return;
        
        int auctionId = Integer.parseInt(parts[1]);
        Auction auction = AuctionServer.getAuctionService().getAuction(auctionId);
        
        if (auction == null) {
            sendMessage(Protocol.buildMessage(MessageType.ERROR, "Auction not found"));
            return;
        }
        
        sendMessage(Protocol.buildMessage(
            MessageType.AUCTION_INFO,
            String.valueOf(auction.getId()),
            auction.getProduct() != null ? auction.getProduct().getName() : "Unknown",
            auction.getProduct() != null ? auction.getProduct().getDescription() : "",
            String.valueOf(auction.getCurrentPrice()),
            auction.getWinnerUsername() != null ? auction.getWinnerUsername() : "",
            String.valueOf(auction.getTimeLeft()),
            auction.getStatus()
        ));
    }
    
    /**
     * Handle JOIN_AUCTION
     */
    private void handleJoinAuction(String[] parts) {
        if (parts.length < 2) return;
        
        int auctionId = Integer.parseInt(parts[1]);
        // Register this client as interested in this auction
        AuctionServer.getBroadcastService().joinAuction(auctionId, this);
        Logger.info(getClientInfo() + " joined auction " + auctionId);
    }
    
    /**
     * Handle BID
     */
    private void handleBid(String[] parts) {
        if (!checkAuth()) return;
        if (parts.length < 3) return;
        
        int auctionId = Integer.parseInt(parts[1]);
        long amount = Long.parseLong(parts[2]);
        
        try {
            AuctionServer.getBidService().placeBid(
                auctionId,
                currentUser.getId(),
                currentUser.getUsername(),
                amount
            );
            
            // Send success
            sendMessage(Protocol.buildMessage(
                MessageType.BID_SUCCESS,
                String.valueOf(auctionId),
                String.valueOf(amount)
            ));
            
            // Broadcast to all participants
            Auction auction = AuctionServer.getAuctionService().getAuction(auctionId);
            AuctionServer.getBroadcastService().broadcastBidUpdate(
                auctionId,
                currentUser.getUsername(),
                amount,
                auction.getTimeLeft()
            );
            
        } catch (Exception e) {
            sendMessage(Protocol.buildMessage(MessageType.BID_FAILED, String.valueOf(auctionId), e.getMessage()));
        }
    }
    
    /**
     * Handle AUTO_BID
     */
    private void handleAutoBid(String[] parts) {
        if (!checkAuth()) return;
        if (parts.length < 3) return;
        
        int auctionId = Integer.parseInt(parts[1]);
        long maxAmount = Long.parseLong(parts[2]);
        
        try {
            AuctionServer.getBidService().placeAutoBid(
                auctionId,
                currentUser.getId(),
                currentUser.getUsername(),
                maxAmount
            );
            
            sendMessage(Protocol.buildMessage(MessageType.SUCCESS, "Auto-bid set"));
            
        } catch (Exception e) {
            sendMessage(Protocol.buildMessage(MessageType.ERROR, e.getMessage()));
        }
    }
    
    /**
     * Handle GET_HISTORY
     */
    private void handleGetHistory(String[] parts) {
        if (parts.length < 2) return;
        
        int auctionId = Integer.parseInt(parts[1]);
        List<Bid> history = AuctionServer.getAuctionService().getBidHistory(auctionId);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            Bid bid = history.get(i);
            sb.append(bid.getUsername()).append(":");
            sb.append(bid.getAmount()).append(":");
            sb.append(bid.getBidTime().getTime());
            
            if (i < history.size() - 1) {
                sb.append(";");
            }
        }
        
        sendMessage(Protocol.buildMessage(MessageType.BID_HISTORY, sb.toString()));
    }
    
    /**
     * Handle CREATE_AUCTION (Admin only)
     */
    private void handleCreateAuction(String[] parts) {
        if (!checkAuth() || !currentUser.isAdmin()) {
            sendMessage(Protocol.buildMessage(MessageType.ERROR, "Admin access required"));
            return;
        }
        
        // TODO: Implement create auction
        sendMessage(Protocol.buildMessage(MessageType.SUCCESS, "Feature not yet implemented"));
    }
    
    /**
     * Handle DELETE_AUCTION (Admin only)
     */
    private void handleDeleteAuction(String[] parts) {
        if (!checkAuth() || !currentUser.isAdmin()) {
            sendMessage(Protocol.buildMessage(MessageType.ERROR, "Admin access required"));
            return;
        }
        
        // TODO: Implement delete auction
        sendMessage(Protocol.buildMessage(MessageType.SUCCESS, "Feature not yet implemented"));
    }
    
    /**
     * Check if user is authenticated
     */
    private boolean checkAuth() {
        if (currentUser == null) {
            sendMessage(Protocol.buildMessage(MessageType.ERROR, "Not authenticated"));
            return false;
        }
        return true;
    }
    
    /**
     * Send message to client
     */
    public void sendMessage(String message) {
        if (out != null && !socket.isClosed()) {
            out.println(message);
            Logger.info("Sent to " + getClientInfo() + ": " + message);
        }
    }
    
    /**
     * Get client info string
     */
    private String getClientInfo() {
        String username = currentUser != null ? currentUser.getUsername() : "Guest";
        return username + "@" + socket.getInetAddress().getHostAddress();
    }
    
    /**
     * Close connection
     */
    public void close() {
        running = false;
        
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            
            AuctionServer.removeClient(this);
            
            Logger.info("Client disconnected: " + getClientInfo());
        } catch (IOException e) {
            Logger.error("Error closing client", e);
        }
    }
}

