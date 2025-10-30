package com.auction.common.dto;

import java.io.Serializable;

/**
 * Data Transfer Object cho Bid
 */
public class BidDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int auctionId;
    private int userId;
    private String username;
    private long amount;
    private long bidTime;
    private boolean isAutoBid;
    
    public BidDTO() {}
    
    public BidDTO(int auctionId, int userId, String username, long amount) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.username = username;
        this.amount = amount;
        this.bidTime = System.currentTimeMillis();
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
    
    public long getBidTime() { return bidTime; }
    public void setBidTime(long bidTime) { this.bidTime = bidTime; }
    
    public boolean isAutoBid() { return isAutoBid; }
    public void setAutoBid(boolean autoBid) { isAutoBid = autoBid; }
    
    @Override
    public String toString() {
        return "BidDTO{" +
                "id=" + id +
                ", auctionId=" + auctionId +
                ", username='" + username + '\'' +
                ", amount=" + amount +
                ", bidTime=" + bidTime +
                '}';
    }
}

