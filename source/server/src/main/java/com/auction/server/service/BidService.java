package com.auction.server.service;

import com.auction.server.database.AuctionDAO;
import com.auction.server.database.BidDAO;
import com.auction.server.model.Auction;
import com.auction.server.model.Bid;
import com.auction.server.util.ConfigLoader;
import com.auction.server.util.Logger;

/**
 * Service xử lý bids
 */
public class BidService {
    private AuctionDAO auctionDAO;
    private BidDAO bidDAO;
    private long minBidIncrement;
    
    public BidService() {
        this.auctionDAO = new AuctionDAO();
        this.bidDAO = new BidDAO();
        this.minBidIncrement = ConfigLoader.getMinBidIncrement();
    }
    
    /**
     * Place bid thủ công
     */
    public synchronized boolean placeBid(int auctionId, int userId, String username, long amount) {
        // Get auction
        Auction auction = auctionDAO.findById(auctionId);
        if (auction == null) {
            throw new IllegalArgumentException("Auction not found");
        }
        
        // Validate
        validateBid(auction, userId, amount);
        
        // Create bid
        Bid bid = new Bid(auctionId, userId, amount);
        bid.setUsername(username);
        
        // Save to database
        if (!bidDAO.insertBid(bid)) {
            throw new RuntimeException("Failed to save bid");
        }
        
        // Update auction
        auctionDAO.updateBid(auctionId, amount, userId);
        auction.setCurrentPrice(amount);
        auction.setWinnerId(userId);
        auction.setWinnerUsername(username);
        auction.setLastBidTime(System.currentTimeMillis());
        
        Logger.info("Bid placed: User " + username + " bid " + amount + " on auction " + auctionId);
        
        return true;
    }
    
    /**
     * Place auto-bid
     */
    public synchronized boolean placeAutoBid(int auctionId, int userId, String username, long maxAmount) {
        // Get auction
        Auction auction = auctionDAO.findById(auctionId);
        if (auction == null) {
            throw new IllegalArgumentException("Auction not found");
        }
        
        // Tính bid amount (current price + increment)
        long bidAmount = auction.getCurrentPrice() + minBidIncrement;
        
        // Không vượt quá max
        if (bidAmount > maxAmount) {
            bidAmount = maxAmount;
        }
        
        // Validate
        validateBid(auction, userId, bidAmount);
        
        // Create auto-bid
        Bid bid = new Bid(auctionId, userId, bidAmount, true, maxAmount);
        bid.setUsername(username);
        
        // Save
        if (!bidDAO.insertBid(bid)) {
            throw new RuntimeException("Failed to save auto-bid");
        }
        
        // Update auction
        auctionDAO.updateBid(auctionId, bidAmount, userId);
        auction.setCurrentPrice(bidAmount);
        auction.setWinnerId(userId);
        auction.setWinnerUsername(username);
        
        Logger.info("Auto-bid placed: User " + username + " max " + maxAmount + " on auction " + auctionId);
        
        return true;
    }
    
    /**
     * Validate bid
     */
    private void validateBid(Auction auction, int userId, long amount) {
        // Check if auction is active
        if (!"active".equals(auction.getStatus())) {
            throw new IllegalArgumentException("Auction is not active");
        }
        
        // Check if auction has ended
        if (auction.isEnded()) {
            throw new IllegalArgumentException("Auction has ended");
        }
        
        // Check if amount is higher than current price
        if (amount <= auction.getCurrentPrice()) {
            throw new IllegalArgumentException("Bid amount must be higher than current price");
        }
        
        // Check minimum increment
        if (amount < auction.getCurrentPrice() + minBidIncrement) {
            throw new IllegalArgumentException("Bid must be at least " + minBidIncrement + " VNĐ higher");
        }
        
        // Check if user is already winner
        if (auction.getWinnerId() != null && auction.getWinnerId() == userId) {
            throw new IllegalArgumentException("You are already the highest bidder");
        }
    }
    
    /**
     * Get highest bid
     */
    public Bid getHighestBid(int auctionId) {
        return bidDAO.findHighestBid(auctionId);
    }
    
    /**
     * Get bid history
     */
    public java.util.List<Bid> getBidHistory(int auctionId) {
        return bidDAO.findByAuctionId(auctionId);
    }
}

