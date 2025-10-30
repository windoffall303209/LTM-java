package com.auction.server.service;

import com.auction.common.protocol.Protocol;
import com.auction.common.protocol.MessageType;
import com.auction.server.util.Logger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Service broadcast messages đến clients
 */
public class BroadcastService {
    
    // Map: auctionId -> Set of client connections
    private ConcurrentHashMap<Integer, Set<Object>> auctionParticipants;
    
    // All connected clients
    private Set<Object> allClients;
    
    public BroadcastService() {
        this.auctionParticipants = new ConcurrentHashMap<>();
        this.allClients = new CopyOnWriteArraySet<>();
    }
    
    /**
     * Add client to all clients list
     */
    public void addClient(Object client) {
        allClients.add(client);
        Logger.info("Client added, total: " + allClients.size());
    }
    
    /**
     * Remove client
     */
    public void removeClient(Object client) {
        allClients.remove(client);
        // Remove from all auctions
        for (Set<Object> participants : auctionParticipants.values()) {
            participants.remove(client);
        }
        Logger.info("Client removed, remaining: " + allClients.size());
    }
    
    /**
     * Join auction (client quan tâm đến auction này)
     */
    public void joinAuction(int auctionId, Object client) {
        auctionParticipants.computeIfAbsent(auctionId, k -> new CopyOnWriteArraySet<>()).add(client);
        Logger.info("Client joined auction " + auctionId);
    }
    
    /**
     * Leave auction
     */
    public void leaveAuction(int auctionId, Object client) {
        Set<Object> participants = auctionParticipants.get(auctionId);
        if (participants != null) {
            participants.remove(client);
        }
    }
    
    /**
     * Broadcast đến tất cả clients
     */
    public void broadcastToAll(String message) {
        Logger.info("Broadcasting to all (" + allClients.size() + " clients): " + message);
        for (Object client : allClients) {
            sendToClient(client, message);
        }
    }
    
    /**
     * Broadcast đến participants của 1 auction
     */
    public void broadcastToAuction(int auctionId, String message) {
        Set<Object> participants = auctionParticipants.get(auctionId);
        if (participants != null && !participants.isEmpty()) {
            Logger.info("Broadcasting to auction " + auctionId + " (" + participants.size() + " clients)");
            for (Object client : participants) {
                sendToClient(client, message);
            }
        }
    }
    
    /**
     * Send bid update broadcast
     */
    public void broadcastBidUpdate(int auctionId, String username, long amount, int timeLeft) {
        String message = Protocol.buildMessage(
            MessageType.BID_UPDATE,
            String.valueOf(auctionId),
            username,
            String.valueOf(amount),
            String.valueOf(timeLeft)
        );
        broadcastToAuction(auctionId, message);
    }
    
    /**
     * Broadcast auction ended
     */
    public void broadcastAuctionEnded(int auctionId, String winner, long finalPrice) {
        String message = Protocol.buildMessage(
            MessageType.AUCTION_ENDED,
            String.valueOf(auctionId),
            winner != null ? winner : "none",
            String.valueOf(finalPrice)
        );
        broadcastToAuction(auctionId, message);
    }
    
    /**
     * Broadcast auction ending soon
     */
    public void broadcastEndingSoon(int auctionId, int seconds) {
        String message = Protocol.buildMessage(
            MessageType.AUCTION_ENDING_SOON,
            String.valueOf(auctionId),
            String.valueOf(seconds)
        );
        broadcastToAuction(auctionId, message);
    }
    
    /**
     * Broadcast time extended
     */
    public void broadcastTimeExtended(int auctionId, int newTimeLeft) {
        String message = Protocol.buildMessage(
            MessageType.AUCTION_EXTENDED,
            String.valueOf(auctionId),
            String.valueOf(newTimeLeft)
        );
        broadcastToAuction(auctionId, message);
    }
    
    /**
     * Send message to specific client
     * (Client object có thể là WebSocket connection)
     */
    private void sendToClient(Object client, String message) {
        try {
            // NOTE: Implementation phụ thuộc vào loại connection object
            // Nếu dùng WebSocket library, có thể: ((WebSocket)client).send(message);
            // Đây là interface, implement chi tiết trong WebSocketServer
        } catch (Exception e) {
            Logger.error("Failed to send to client", e);
        }
    }
    
    /**
     * Get active clients count
     */
    public int getActiveClientsCount() {
        return allClients.size();
    }
    
    /**
     * Get auction participants count
     */
    public int getAuctionParticipantsCount(int auctionId) {
        Set<Object> participants = auctionParticipants.get(auctionId);
        return participants != null ? participants.size() : 0;
    }
}

