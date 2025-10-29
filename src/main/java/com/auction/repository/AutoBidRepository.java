package com.auction.repository;

import com.auction.model.AutoBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository: AutoBid
 * Xử lý truy vấn database cho AutoBid entity
 */
@Repository
public interface AutoBidRepository extends JpaRepository<AutoBid, Long> {
    
    /**
     * Lấy tất cả auto bids active của một auction
     */
    @Query("SELECT ab FROM AutoBid ab WHERE ab.auction.auctionId = :auctionId AND ab.isActive = true")
    List<AutoBid> findActiveAutoBidsByAuctionId(@Param("auctionId") Long auctionId);
    
    /**
     * Lấy auto bid của user cho auction cụ thể
     */
    Optional<AutoBid> findByUser_UserIdAndAuction_AuctionId(Long userId, Long auctionId);
    
    /**
     * Lấy tất cả auto bids của user
     */
    List<AutoBid> findByUser_UserId(Long userId);
    
    /**
     * Kiểm tra user đã có auto bid cho auction này chưa
     */
    boolean existsByUser_UserIdAndAuction_AuctionIdAndIsActive(Long userId, Long auctionId, Boolean isActive);
    
    /**
     * Xóa auto bid của user trong auction
     */
    void deleteByUser_UserIdAndAuction_AuctionId(Long userId, Long auctionId);

    // Additional methods for AutoBidService
    Optional<AutoBid> findByUserAndAuction(com.auction.model.User user, com.auction.model.Auction auction);
    
    @Query("SELECT ab FROM AutoBid ab WHERE ab.auction = :auction AND ab.isActive = true")
    List<AutoBid> findActiveByAuction(@Param("auction") com.auction.model.Auction auction);
    
    List<AutoBid> findByUser(com.auction.model.User user);
}

