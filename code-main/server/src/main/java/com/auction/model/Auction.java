package com.auction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity: Auction
 * Đại diện cho một phiên đấu giá
 */
@Entity
@Table(name = "auctions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Long auctionId;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Giá khởi điểm không được để trống")
    @Positive(message = "Giá khởi điểm phải > 0")
    @Column(name = "starting_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal startingPrice;

    @Column(name = "current_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "reserve_price", precision = 15, scale = 2)
    private BigDecimal reservePrice; // Giá dự trữ (tối thiểu để bán)

    @Column(name = "bid_increment", precision = 15, scale = 2)
    private BigDecimal bidIncrement = new BigDecimal("100000"); // Bước giá: 100k VND

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AuctionStatus status = AuctionStatus.PENDING;

    @Column(name = "total_bids")
    private Integer totalBids = 0;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Chỗ này sẽ thêm relationships:
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "created_by")
    // private User createdBy;
    //
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "highest_bidder_id")
    // private User highestBidder;
    //
    // @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    // private List<Bid> bids = new ArrayList<>();
    //
    // @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    // private Set<Watchlist> watchlists = new HashSet<>();

    // Chỗ này sẽ thêm business logic methods:
    // public boolean isActive() { return status == AuctionStatus.ACTIVE; }
    // public boolean hasEnded() { return LocalDateTime.now().isAfter(endTime); }
    // public BigDecimal getMinimumBid() { return currentPrice.add(bidIncrement); }
    // v.v...

    /**
     * Enum: AuctionStatus
     * Trạng thái của phiên đấu giá
     */
    public enum AuctionStatus {
        PENDING,    // Chưa bắt đầu
        ACTIVE,     // Đang diễn ra
        ENDED,      // Đã kết thúc
        CANCELLED   // Đã hủy
    }
}
