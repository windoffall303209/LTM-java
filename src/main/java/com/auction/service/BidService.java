package com.auction.service;

import com.auction.dto.BidDTO;
import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.User;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service: Bid
 * Xử lý business logic liên quan đến Bidding
 * Đây là phần QUAN TRỌNG NHẤT - Network Programming Core
 */
@Service
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AutoBidService autoBidService;

    // Constructor với @Lazy để tránh circular dependency
    public BidService(BidRepository bidRepository, 
                      AuctionRepository auctionRepository,
                      SimpMessagingTemplate messagingTemplate,
                      @Lazy AutoBidService autoBidService) {
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.messagingTemplate = messagingTemplate;
        this.autoBidService = autoBidService;
    }

    /**
     * Đặt giá (CORE FUNCTION) - Manual bid
     * Synchronized để tránh race condition khi nhiều users bid cùng lúc
     */
    @Transactional
    public synchronized BidDTO placeBid(User user, Long auctionId, BigDecimal bidAmount) {
        return placeBid(user, auctionId, bidAmount, false);
    }

    /**
     * Đặt giá (OVERLOADED) - Hỗ trợ auto bid
     */
    @Transactional
    public synchronized BidDTO placeBid(User user, Long auctionId, BigDecimal bidAmount, boolean isAutoBid) {
        // 1. Lấy auction
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction không tồn tại"));

        // 2. Validate auction status
        if (!auction.isActive()) {
            throw new RuntimeException("Auction không đang hoạt động");
        }

        if (auction.hasEnded()) {
            throw new RuntimeException("Auction đã kết thúc");
        }

        // 3. Validate bid amount
        if (!auction.isValidBid(bidAmount)) {
            throw new RuntimeException("Giá đặt phải >= " + auction.getMinimumBid() + " VND");
        }

        // 4. Kiểm tra không được bid vào auction của chính mình
        if (auction.getHighestBidder() != null && 
            auction.getHighestBidder().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn đang dẫn đầu rồi!");
        }

        // 5. Kiểm tra balance
        if (!user.hasEnoughBalance(bidAmount)) {
            throw new RuntimeException("Số dư không đủ");
        }

        // 6. Tạo bid mới
        Bid bid = new Bid(auction, user, bidAmount, isAutoBid);
        Bid savedBid = bidRepository.save(bid);

        // 7. Cập nhật auction
        auction.placeBid(savedBid);
        auctionRepository.save(auction);

        // 8. Check nếu bid trong 60s cuối -> extend time
        long secondsRemaining = auction.getSecondsRemaining();
        if (secondsRemaining > 0 && secondsRemaining <= 60 && auction.getExtendCount() < 3) {
            auction.extendAuction(60);
            auctionRepository.save(auction);
            
            // Broadcast extension
            broadcastAuctionExtended(auctionId, 60);
        }

        // 9. Broadcast bid update đến tất cả clients (REAL-TIME)
        broadcastBidUpdate(auction, user, bidAmount);

        // 10. Kích hoạt auto bid cho những user khác (nếu không phải auto bid)
        if (!isAutoBid) {
            try {
                autoBidService.processAutoBids(auction, bidAmount, user.getUserId());
            } catch (Exception e) {
                // Log lỗi nhưng không throw để không ảnh hưởng đến bid chính
                System.err.println("Error processing auto bids: " + e.getMessage());
            }
        }

        return BidDTO.fromEntity(savedBid);
    }

    /**
     * Broadcast bid update qua WebSocket
     * Gửi đến tất cả users đang theo dõi auction này
     */
    private void broadcastBidUpdate(Auction auction, User bidder, BigDecimal newPrice) {
        Map<String, Object> update = new HashMap<>();
        update.put("type", "BID_UPDATE");
        update.put("auctionId", auction.getAuctionId());
        update.put("bidderName", bidder.getUsername());
        update.put("newPrice", newPrice);
        update.put("minimumBid", auction.getMinimumBid());  // Thêm minimum bid mới
        update.put("timeLeft", auction.getSecondsRemaining());
        update.put("totalBids", auction.getTotalBids());

        // Gửi đến topic /topic/auction/{auctionId}
        messagingTemplate.convertAndSend(
            "/topic/auction/" + auction.getAuctionId(), 
            update
        );
    }

    /**
     * Broadcast auction extended
     */
    private void broadcastAuctionExtended(Long auctionId, int seconds) {
        Map<String, Object> update = new HashMap<>();
        update.put("type", "AUCTION_EXTENDED");
        update.put("auctionId", auctionId);
        update.put("extendedSeconds", seconds);

        messagingTemplate.convertAndSend("/topic/auction/" + auctionId, update);
    }

    /**
     * Lấy lịch sử bids của auction
     */
    public List<BidDTO> getBidHistory(Long auctionId) {
        return bidRepository.findByAuction_AuctionIdOrderByBidTimeDesc(auctionId)
                .stream()
                .map(BidDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Lấy bids của user
     */
    public List<BidDTO> getUserBids(Long userId) {
        return bidRepository.findByUser_UserIdOrderByBidTimeDesc(userId)
                .stream()
                .map(BidDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Lấy bid cao nhất của auction
     */
    public BidDTO getHighestBid(Long auctionId) {
        return bidRepository.findHighestBidForAuction(auctionId)
                .map(BidDTO::fromEntity)
                .orElse(null);
    }

    /**
     * Kiểm tra user đã bid chưa
     */
    public boolean hasUserBid(Long auctionId, Long userId) {
        return bidRepository.existsByAuction_AuctionIdAndUser_UserId(auctionId, userId);
    }
}

