package com.auction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity: AutoBid
 * Tự động đặt giá khi có người khác đặt giá cao hơn
 */
@Entity
@Table(name = "auto_bids", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "auction_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auto_bid_id")
    private Long autoBidId;

    @NotNull(message = "User không được null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Auction không được null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @NotNull(message = "Số tiền tối đa không được để trống")
    @Positive(message = "Số tiền tối đa phải > 0")
    @Column(name = "max_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal maxAmount;

    @NotNull(message = "Bước giá tăng không được để trống")
    @Positive(message = "Bước giá tăng phải > 0")
    @Column(name = "increment_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal incrementAmount;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructor helper
    public AutoBid(User user, Auction auction, BigDecimal maxAmount) {
        this.user = user;
        this.auction = auction;
        this.maxAmount = maxAmount;
        this.isActive = true;
    }

    public boolean canBid(BigDecimal currentPrice) {
        return isActive && maxAmount.compareTo(currentPrice) > 0;
    }

    public BigDecimal getNextBidAmount(BigDecimal currentPrice, BigDecimal increment) {
        BigDecimal nextBid = currentPrice.add(increment);
        return nextBid.compareTo(maxAmount) <= 0 ? nextBid : maxAmount;
    }
}

