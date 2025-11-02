package com.auction.service;

import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.User;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import com.auction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service: Admin Statistics
 * Xử lý các thống kê và báo cáo dành riêng cho Admin Dashboard
 */
@Service
@RequiredArgsConstructor
public class AdminStatisticsService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    /**
     * Lấy tổng quan thống kê hệ thống (cho Admin Dashboard)
     */
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // User statistics
        List<User> allUsers = userRepository.findAll();
        stats.put("totalUsers", allUsers.size());
        stats.put("activeUsers", allUsers.stream().filter(User::getIsActive).count());
        stats.put("bannedUsers", allUsers.stream().filter(u -> !u.getIsActive()).count());

        // Auction statistics
        List<Auction> allAuctions = auctionRepository.findAll();
        stats.put("totalAuctions", allAuctions.size());
        stats.put("activeAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.ACTIVE).count());
        stats.put("pendingAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.PENDING).count());
        stats.put("endedAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.ENDED).count());

        // Bid statistics
        List<Bid> allBids = bidRepository.findAll();
        stats.put("totalBids", allBids.size());

        // Financial statistics
        BigDecimal totalBidValue = allBids.stream()
                .map(Bid::getBidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalBidValue", totalBidValue);

        BigDecimal totalUserBalance = allUsers.stream()
                .map(User::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalUserBalance", totalUserBalance);

        // Current active auction value
        BigDecimal activeAuctionValue = allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.ACTIVE)
                .map(Auction::getCurrentPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("activeAuctionValue", activeAuctionValue);

        return stats;
    }

    /**
     * Lấy thống kê chi tiết về users
     */
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<User> allUsers = userRepository.findAll();

        stats.put("totalUsers", allUsers.size());
        stats.put("activeUsers", allUsers.stream().filter(User::getIsActive).count());
        stats.put("bannedUsers", allUsers.stream().filter(u -> !u.getIsActive()).count());
        stats.put("adminCount", allUsers.stream()
                .filter(u -> u.getRole() == User.Role.ADMIN).count());
        stats.put("regularUserCount", allUsers.stream()
                .filter(u -> u.getRole() == User.Role.USER).count());

        BigDecimal totalBalance = allUsers.stream()
                .map(User::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalSystemBalance", totalBalance);

        BigDecimal avgBalance = allUsers.isEmpty() ? BigDecimal.ZERO
                : totalBalance.divide(new BigDecimal(allUsers.size()), 2, BigDecimal.ROUND_HALF_UP);
        stats.put("averageUserBalance", avgBalance);

        return stats;
    }

    /**
     * Lấy thống kê chi tiết về auctions
     */
    public Map<String, Object> getAuctionStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<Auction> allAuctions = auctionRepository.findAll();

        stats.put("totalAuctions", allAuctions.size());
        stats.put("activeAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.ACTIVE).count());
        stats.put("pendingAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.PENDING).count());
        stats.put("endedAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.ENDED).count());
        stats.put("cancelledAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.CANCELLED).count());

        int totalBids = allAuctions.stream()
                .mapToInt(Auction::getTotalBids)
                .sum();
        stats.put("totalBids", totalBids);

        double avgBidsPerAuction = allAuctions.isEmpty() ? 0.0
                : (double) totalBids / allAuctions.size();
        stats.put("averageBidsPerAuction", avgBidsPerAuction);

        return stats;
    }

    /**
     * Lấy thống kê chi tiết về bidding
     */
    public Map<String, Object> getBidStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<Bid> allBids = bidRepository.findAll();

        stats.put("totalBids", allBids.size());

        BigDecimal totalBidValue = allBids.stream()
                .map(Bid::getBidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalBidValue", totalBidValue);

        BigDecimal avgBidValue = allBids.isEmpty() ? BigDecimal.ZERO
                : totalBidValue.divide(new BigDecimal(allBids.size()), 2, BigDecimal.ROUND_HALF_UP);
        stats.put("averageBidValue", avgBidValue);

        // Highest bid
        BigDecimal highestBid = allBids.stream()
                .map(Bid::getBidAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        stats.put("highestBid", highestBid);

        // Lowest bid
        BigDecimal lowestBid = allBids.stream()
                .map(Bid::getBidAmount)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        stats.put("lowestBid", lowestBid);

        return stats;
    }

    /**
     * Lấy danh sách auctions gần đây (cho dashboard)
     */
    public List<Auction> getRecentAuctions(int limit) {
        return auctionRepository.findAll().stream()
                .sorted((a, b) -> {
                    LocalDateTime timeA = a.getCreatedAt() != null ? a.getCreatedAt() : LocalDateTime.MIN;
                    LocalDateTime timeB = b.getCreatedAt() != null ? b.getCreatedAt() : LocalDateTime.MIN;
                    return timeB.compareTo(timeA);
                })
                .limit(limit)
                .toList();
    }

    /**
     * Lấy danh sách bids gần đây (cho dashboard)
     */
    public List<Bid> getRecentBids(int limit) {
        return bidRepository.findAll().stream()
                .sorted((a, b) -> b.getBidTime().compareTo(a.getBidTime()))
                .limit(limit)
                .toList();
    }

    /**
     * Lấy top users theo số lượng bids
     */
    public Map<String, Object> getTopBidders(int limit) {
        Map<String, Object> result = new HashMap<>();

        List<Bid> allBids = bidRepository.findAll();

        // Group by user and count
        Map<Long, Long> userBidCounts = new HashMap<>();
        for (Bid bid : allBids) {
            Long userId = bid.getUser().getUserId();
            userBidCounts.put(userId, userBidCounts.getOrDefault(userId, 0L) + 1);
        }

        // Sort and get top
        List<Map<String, Object>> topBidders = userBidCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(limit)
                .map(entry -> {
                    User user = userRepository.findById(entry.getKey()).orElse(null);
                    Map<String, Object> bidder = new HashMap<>();
                    bidder.put("userId", entry.getKey());
                    bidder.put("username", user != null ? user.getUsername() : "Unknown");
                    bidder.put("bidCount", entry.getValue());
                    return bidder;
                })
                .toList();

        result.put("topBidders", topBidders);
        return result;
    }

    /**
     * Lấy top auctions theo số lượng bids
     */
    public Map<String, Object> getTopAuctions(int limit) {
        Map<String, Object> result = new HashMap<>();

        List<Auction> topAuctions = auctionRepository.findAll().stream()
                .sorted((a, b) -> Integer.compare(b.getTotalBids(), a.getTotalBids()))
                .limit(limit)
                .toList();

        result.put("topAuctions", topAuctions);
        return result;
    }

    /**
     * Lấy thống kê theo khoảng thời gian (hôm nay, tuần này, tháng này)
     */
    public Map<String, Object> getTimeBasedStatistics(String period) {
        Map<String, Object> stats = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime;

        switch (period.toLowerCase()) {
            case "today":
                startTime = now.toLocalDate().atStartOfDay();
                break;
            case "week":
                startTime = now.minusDays(7);
                break;
            case "month":
                startTime = now.minusDays(30);
                break;
            default:
                startTime = LocalDateTime.MIN;
        }

        // Count auctions created in period
        LocalDateTime finalStartTime = startTime;
        long auctionsInPeriod = auctionRepository.findAll().stream()
                .filter(a -> a.getCreatedAt() != null && a.getCreatedAt().isAfter(finalStartTime))
                .count();
        stats.put("auctionsInPeriod", auctionsInPeriod);

        // Count bids in period
        long bidsInPeriod = bidRepository.findAll().stream()
                .filter(b -> b.getBidTime().isAfter(finalStartTime))
                .count();
        stats.put("bidsInPeriod", bidsInPeriod);

        stats.put("period", period);
        stats.put("startTime", startTime);

        return stats;
    }
}
