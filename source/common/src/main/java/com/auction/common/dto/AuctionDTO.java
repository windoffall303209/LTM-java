package com.auction.common.dto;

import java.io.Serializable;

/**
 * Data Transfer Object cho Auction
 */
public class AuctionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String productName;
    private String productDescription;
    private long startPrice;
    private long currentPrice;
    private String currentWinner;
    private int winnerId;
    private String status; // "active", "ended", "cancelled"
    private long startTime;
    private long endTime;
    private int timeLeft; // seconds
    
    public AuctionDTO() {}
    
    public AuctionDTO(int id, String productName, long currentPrice, int timeLeft) {
        this.id = id;
        this.productName = productName;
        this.currentPrice = currentPrice;
        this.timeLeft = timeLeft;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
    
    public long getStartPrice() { return startPrice; }
    public void setStartPrice(long startPrice) { this.startPrice = startPrice; }
    
    public long getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(long currentPrice) { this.currentPrice = currentPrice; }
    
    public String getCurrentWinner() { return currentWinner; }
    public void setCurrentWinner(String currentWinner) { this.currentWinner = currentWinner; }
    
    public int getWinnerId() { return winnerId; }
    public void setWinnerId(int winnerId) { this.winnerId = winnerId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public int getTimeLeft() { return timeLeft; }
    public void setTimeLeft(int timeLeft) { this.timeLeft = timeLeft; }
    
    @Override
    public String toString() {
        return "AuctionDTO{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", currentPrice=" + currentPrice +
                ", currentWinner='" + currentWinner + '\'' +
                ", timeLeft=" + timeLeft +
                '}';
    }
}

