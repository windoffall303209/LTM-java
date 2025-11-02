package com.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Auction Data Transfer Object
 *
 * Used to send auction data to frontend.
 * Can include additional computed fields like timeRemaining.
 *
 * Chỗ này sẽ thêm factory method để convert từ Auction entity
 * Chỗ này sẽ thêm fields cho seller info, winner info nếu cần
 */
public class AuctionDTO {

    private Long id;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal currentPrice;
    private BigDecimal reservePrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Integer bidCount;

    // Chỗ này sẽ thêm computed fields:
    // private Long timeRemainingSeconds;
    // private String sellerUsername;
    // private String winnerUsername;

    public AuctionDTO() {
    }

    // Chỗ này sẽ thêm factory method:
    // public static AuctionDTO fromEntity(Auction auction) {
    //     AuctionDTO dto = new AuctionDTO();
    //     dto.setId(auction.getId());
    //     dto.setTitle(auction.getTitle());
    //     // ... set other fields
    //     return dto;
    // }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(BigDecimal reservePrice) {
        this.reservePrice = reservePrice;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBidCount() {
        return bidCount;
    }

    public void setBidCount(Integer bidCount) {
        this.bidCount = bidCount;
    }
}
