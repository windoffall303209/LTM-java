package com.auction.service;

import com.auction.model.Auction;
import com.auction.model.User;
import com.auction.model.Watchlist;
import com.auction.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service: Watchlist
 * Xử lý logic danh sách theo dõi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final AuctionService auctionService;

    /**
     * Thêm auction vào watchlist
     */
    @Transactional
    public Watchlist addToWatchlist(User user, Long auctionId) {
        Auction auction = auctionService.getAuctionById(auctionId);

        // Kiểm tra đã tồn tại chưa
        if (watchlistRepository.findByUserAndAuction(user, auction).isPresent()) {
            throw new RuntimeException("Đấu giá đã có trong watchlist");
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setAuction(auction);

        Watchlist saved = watchlistRepository.save(watchlist);
        log.info("Added to watchlist: user={}, auction={}", user.getUsername(), auctionId);

        return saved;
    }

    /**
     * Xóa khỏi watchlist
     */
    @Transactional
    public void removeFromWatchlist(Long watchlistId, Long userId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new RuntimeException("Watchlist item không tồn tại"));

        if (!watchlist.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền xóa item này");
        }

        watchlistRepository.delete(watchlist);
        log.info("Removed from watchlist: id={}, user={}", watchlistId, watchlist.getUser().getUsername());
    }

    /**
     * Xóa theo auctionId
     */
    @Transactional
    public void removeByAuctionId(Long userId, Long auctionId) {
        User user = new User();
        user.setUserId(userId);
        
        Auction auction = auctionService.getAuctionById(auctionId);
        
        watchlistRepository.findByUserAndAuction(user, auction)
                .ifPresent(watchlistRepository::delete);
    }

    /**
     * Lấy watchlist của user
     */
    public List<Watchlist> getUserWatchlist(User user) {
        return watchlistRepository.findByUserOrderByAddedAtDesc(user);
    }

    /**
     * Kiểm tra auction có trong watchlist không
     */
    public boolean isInWatchlist(User user, Long auctionId) {
        Auction auction = auctionService.getAuctionById(auctionId);
        return watchlistRepository.findByUserAndAuction(user, auction).isPresent();
    }
}


