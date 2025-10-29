package com.auction.service;

import com.auction.model.Auction;
import com.auction.model.AutoBid;
import com.auction.model.User;
import com.auction.repository.AutoBidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service: AutoBid
 * Xử lý logic đặt giá tự động
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AutoBidService {

    private final AutoBidRepository autoBidRepository;
    private final AuctionService auctionService;
    private final BidService bidService;

    /**
     * Tạo auto bid mới
     */
    @Transactional
    public AutoBid createAutoBid(User user, Long auctionId, BigDecimal maxAmount, BigDecimal incrementAmount) {
        // Kiểm tra auction tồn tại
        Auction auction = auctionService.getAuctionById(auctionId);
        
        if (auction.getStatus() != Auction.AuctionStatus.ACTIVE) {
            throw new RuntimeException("Đấu giá không còn hoạt động");
        }

        // Kiểm tra user có đủ tiền không
        if (!user.hasEnoughBalance(maxAmount)) {
            throw new RuntimeException("Số dư không đủ cho auto bid này");
        }

        // Kiểm tra xem user đã có auto bid cho auction này chưa
        autoBidRepository.findByUserAndAuction(user, auction)
                .ifPresent(existing -> {
                    // Xóa auto bid cũ
                    autoBidRepository.delete(existing);
                });

        // Tạo auto bid mới
        AutoBid autoBid = new AutoBid();
        autoBid.setUser(user);
        autoBid.setAuction(auction);
        autoBid.setMaxAmount(maxAmount);
        autoBid.setIncrementAmount(incrementAmount);
        autoBid.setIsActive(true);

        AutoBid saved = autoBidRepository.save(autoBid);
        log.info("Created AutoBid for user {} on auction {}: max={}, increment={}", 
                user.getUsername(), auctionId, maxAmount, incrementAmount);

        return saved;
    }

    /**
     * Xử lý auto bid khi có bid mới
     * Được gọi từ BidService
     */
    @Transactional
    public void processAutoBids(Auction auction, BigDecimal currentPrice, Long excludeUserId) {
        // Lấy tất cả auto bids còn active cho auction này
        List<AutoBid> autoBids = autoBidRepository.findActiveByAuction(auction);

        for (AutoBid autoBid : autoBids) {
            // Bỏ qua user vừa đặt giá thủ công
            if (autoBid.getUser().getUserId().equals(excludeUserId)) {
                continue;
            }

            // Kiểm tra xem auto bid còn đủ tiền không
            BigDecimal nextBid = currentPrice.add(autoBid.getIncrementAmount());
            
            if (nextBid.compareTo(autoBid.getMaxAmount()) <= 0) {
                try {
                    // Đặt giá tự động
                    bidService.placeBid(autoBid.getUser(), auction.getAuctionId(), nextBid, true);
                    log.info("Auto bid placed: user={}, amount={}", 
                            autoBid.getUser().getUsername(), nextBid);
                    
                    // Chỉ cho phép 1 auto bid mỗi lần
                    break;
                } catch (Exception e) {
                    log.warn("Auto bid failed for user {}: {}", 
                            autoBid.getUser().getUsername(), e.getMessage());
                    // Nếu auto bid thất bại, vô hiệu hóa nó
                    autoBid.setIsActive(false);
                    autoBidRepository.save(autoBid);
                }
            } else {
                // Đã vượt quá max amount, vô hiệu hóa auto bid
                autoBid.setIsActive(false);
                autoBidRepository.save(autoBid);
                log.info("Auto bid deactivated (max reached): user={}", 
                        autoBid.getUser().getUsername());
            }
        }
    }

    /**
     * Lấy auto bid của user cho auction
     */
    public AutoBid getUserAutoBid(User user, Long auctionId) {
        Auction auction = auctionService.getAuctionById(auctionId);
        return autoBidRepository.findByUserAndAuction(user, auction)
                .orElse(null);
    }

    /**
     * Hủy auto bid
     */
    @Transactional
    public void cancelAutoBid(Long autoBidId, Long userId) {
        AutoBid autoBid = autoBidRepository.findById(autoBidId)
                .orElseThrow(() -> new RuntimeException("Auto bid không tồn tại"));

        if (!autoBid.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền hủy auto bid này");
        }

        autoBidRepository.delete(autoBid);
        log.info("Auto bid cancelled: id={}, user={}", autoBidId, autoBid.getUser().getUsername());
    }

    /**
     * Lấy tất cả auto bids của user
     */
    public List<AutoBid> getUserAutoBids(User user) {
        return autoBidRepository.findByUser(user);
    }
}


