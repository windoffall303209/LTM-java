package com.auction.server.model;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Model class cho Auction
 */
public class Auction {
    private int id;
    private int productId;
    private Product product;
    private long startPrice;
    private long currentPrice;
    private long reservePrice;
    private Integer winnerId;
    private String winnerUsername;
    private String status; // "pending", "active", "ended", "cancelled"
    private Timestamp startTime;
    private Timestamp endTime;
    private int duration; // in seconds
    private int extensionCount;
    private int createdBy;
    private Timestamp createdAt;
    
    // Runtime fields
    private Timer timer;
    private long lastBidTime;
    
    public Auction() {
        this.status = "pending";
        this.extensionCount = 0;
    }
    
    public Auction(int productId, long startPrice, int duration, int createdBy) {
        this.productId = productId;
        this.startPrice = startPrice;
        this.currentPrice = startPrice;
        this.duration = duration;
        this.createdBy = createdBy;
        this.status = "pending";
        this.extensionCount = 0;
    }
    
    /**
     * Tính thời gian còn lại (seconds)
     */
    public int getTimeLeft() {
        if (endTime == null) return 0;
        long now = System.currentTimeMillis();
        long end = endTime.getTime();
        long diff = end - now;
        return diff > 0 ? (int)(diff / 1000) : 0;
    }
    
    /**
     * Kiểm tra có nên extend time không (bid trong 10s cuối)
     */
    public boolean shouldExtendTime() {
        int timeLeft = getTimeLeft();
        long timeSinceLastBid = System.currentTimeMillis() - lastBidTime;
        return timeLeft <= 10 && timeSinceLastBid < 10000;
    }
    
    /**
     * Extend thời gian thêm 60s
     */
    public void extendTime(int seconds) {
        if (endTime != null) {
            long newEndTime = endTime.getTime() + (seconds * 1000L);
            this.endTime = new Timestamp(newEndTime);
            this.extensionCount++;
        }
    }
    
    /**
     * Kiểm tra auction đã kết thúc chưa
     */
    public boolean isEnded() {
        return "ended".equals(status) || getTimeLeft() <= 0;
    }
    
    /**
     * Kiểm tra auction đang active không
     */
    public boolean isActive() {
        return "active".equals(status) && getTimeLeft() > 0;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    public long getStartPrice() { return startPrice; }
    public void setStartPrice(long startPrice) { this.startPrice = startPrice; }
    
    public long getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(long currentPrice) { this.currentPrice = currentPrice; }
    
    public long getReservePrice() { return reservePrice; }
    public void setReservePrice(long reservePrice) { this.reservePrice = reservePrice; }
    
    public Integer getWinnerId() { return winnerId; }
    public void setWinnerId(Integer winnerId) { this.winnerId = winnerId; }
    
    public String getWinnerUsername() { return winnerUsername; }
    public void setWinnerUsername(String winnerUsername) { this.winnerUsername = winnerUsername; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }
    
    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public int getExtensionCount() { return extensionCount; }
    public void setExtensionCount(int extensionCount) { this.extensionCount = extensionCount; }
    
    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timer getTimer() { return timer; }
    public void setTimer(Timer timer) { this.timer = timer; }
    
    public long getLastBidTime() { return lastBidTime; }
    public void setLastBidTime(long lastBidTime) { this.lastBidTime = lastBidTime; }
    
    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", productId=" + productId +
                ", currentPrice=" + currentPrice +
                ", status='" + status + '\'' +
                ", timeLeft=" + getTimeLeft() + "s" +
                '}';
    }
}

