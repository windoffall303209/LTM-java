package com.auction.server.model;

import java.sql.Timestamp;

/**
 * Model class cho Bid
 */
public class Bid {
    private int id;
    private int auctionId;
    private int userId;
    private String username;
    private long amount;
    private boolean isAutoBid;
    private long maxAutoBid;
    private Timestamp bidTime;
    
    public Bid() {}
    
    public Bid(int auctionId, int userId, long amount) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.amount = amount;
        this.isAutoBid = false;
        this.bidTime = new Timestamp(System.currentTimeMillis());
    }
    
    public Bid(int auctionId, int userId, long amount, boolean isAutoBid, long maxAutoBid) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.amount = amount;
        this.isAutoBid = isAutoBid;
        this.maxAutoBid = maxAutoBid;
        this.bidTime = new Timestamp(System.currentTimeMillis());
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getAuctionId() { return auctionId; }
    public void setAuctionId(int auctionId) { this.auctionId = auctionId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    
    public boolean isAutoBid() { return isAutoBid; }
    public void setAutoBid(boolean autoBid) { isAutoBid = autoBid; }
    
    public long getMaxAutoBid() { return maxAutoBid; }
    public void setMaxAutoBid(long maxAutoBid) { this.maxAutoBid = maxAutoBid; }
    
    public Timestamp getBidTime() { return bidTime; }
    public void setBidTime(Timestamp bidTime) { this.bidTime = bidTime; }
    
    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", auctionId=" + auctionId +
                ", userId=" + userId +
                ", amount=" + amount +
                ", isAutoBid=" + isAutoBid +
                '}';
    }
}

