package com.auction.server.service;

import com.auction.server.database.AuctionDAO;
import com.auction.server.database.BidDAO;
import com.auction.server.model.Auction;
import com.auction.server.model.Bid;
import com.auction.server.util.Logger;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service quản lý auctions
 */
public class AuctionService {
    private AuctionDAO auctionDAO;
    private BidDAO bidDAO;
    private Map<Integer, Auction> activeAuctions;
    
    public AuctionService() {
        this.auctionDAO = new AuctionDAO();
        this.bidDAO = new BidDAO();
        this.activeAuctions = new ConcurrentHashMap<>();
        loadActiveAuctions();
    }
    
    /**
     * Load active auctions từ database
     */
    private void loadActiveAuctions() {
        List<Auction> auctions = auctionDAO.findActive();
        for (Auction auction : auctions) {
            activeAuctions.put(auction.getId(), auction);
        }
        Logger.info("Loaded " + auctions.size() + " active auctions");
    }
    
    /**
     * Get tất cả auctions
     */
    public List<Auction> getAllAuctions() {
        return auctionDAO.findAll();
    }
    
    /**
     * Get active auctions
     */
    public List<Auction> getActiveAuctions() {
        return auctionDAO.findActive();
    }
    
    /**
     * Get auction by ID
     */
    public Auction getAuction(int auctionId) {
        // Try cache first
        if (activeAuctions.containsKey(auctionId)) {
            return activeAuctions.get(auctionId);
        }
        // Load from database
        return auctionDAO.findById(auctionId);
    }
    
    /**
     * Create auction mới
     */
    public Auction createAuction(int productId, long startPrice, int duration, int createdBy) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp endTime = new Timestamp(now.getTime() + (duration * 1000L));
        
        Auction auction = new Auction(productId, startPrice, duration, createdBy);
        auction.setStartTime(now);
        auction.setEndTime(endTime);
        auction.setStatus("active");
        
        if (auctionDAO.insertAuction(auction)) {
            activeAuctions.put(auction.getId(), auction);
            Logger.info("Created new auction: " + auction.getId());
            return auction;
        }
        
        return null;
    }
    
    /**
     * Update auction status
     */
    public boolean updateStatus(int auctionId, String status) {
        if (auctionDAO.updateStatus(auctionId, status)) {
            Auction auction = activeAuctions.get(auctionId);
            if (auction != null) {
                auction.setStatus(status);
            }
            return true;
        }
        return false;
    }
    
    /**
     * End auction
     */
    public boolean endAuction(int auctionId) {
        Auction auction = getAuction(auctionId);
        if (auction == null) return false;
        
        auction.setStatus("ended");
        auctionDAO.updateStatus(auctionId, "ended");
        activeAuctions.remove(auctionId);
        
        Logger.info("Auction ended: " + auctionId + ", Winner: " + auction.getWinnerUsername());
        return true;
    }
    
    /**
     * Extend auction time (chống giật giây cuối)
     */
    public boolean extendTime(int auctionId, int seconds) {
        Auction auction = getAuction(auctionId);
        if (auction == null) return false;
        
        auction.extendTime(seconds);
        auctionDAO.updateEndTime(auctionId, auction.getEndTime(), auction.getExtensionCount());
        
        Logger.info("Extended auction " + auctionId + " by " + seconds + " seconds");
        return true;
    }
    
    /**
     * Delete auction (admin only)
     */
    public boolean deleteAuction(int auctionId) {
        if (auctionDAO.delete(auctionId)) {
            activeAuctions.remove(auctionId);
            Logger.info("Deleted auction: " + auctionId);
            return true;
        }
        return false;
    }
    
    /**
     * Get bid history
     */
    public List<Bid> getBidHistory(int auctionId) {
        return bidDAO.findByAuctionId(auctionId);
    }
    
    /**
     * Check and end expired auctions
     */
    public void checkExpiredAuctions() {
        for (Auction auction : activeAuctions.values()) {
            if (auction.isEnded()) {
                endAuction(auction.getId());
            }
        }
    }
}

