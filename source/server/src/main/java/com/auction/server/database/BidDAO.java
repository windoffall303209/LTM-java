package com.auction.server.database;

import com.auction.server.model.Bid;
import com.auction.server.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho Bid
 */
public class BidDAO {
    private Connection connection;
    
    public BidDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            Logger.error("Failed to get database connection", e);
        }
    }
    
    /**
     * Thêm bid mới
     */
    public boolean insertBid(Bid bid) {
        String sql = "INSERT INTO bids (auction_id, user_id, amount, is_auto_bid, max_auto_bid) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, bid.getAuctionId());
            stmt.setInt(2, bid.getUserId());
            stmt.setLong(3, bid.getAmount());
            stmt.setBoolean(4, bid.isAutoBid());
            stmt.setLong(5, bid.getMaxAutoBid());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    bid.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error inserting bid", e);
        }
        return false;
    }
    
    /**
     * Lấy lịch sử bids của 1 auction
     */
    public List<Bid> findByAuctionId(int auctionId) {
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT b.*, u.username FROM bids b " +
                     "JOIN users u ON b.user_id = u.id " +
                     "WHERE b.auction_id = ? " +
                     "ORDER BY b.bid_time DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, auctionId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bids.add(extractBidFromResultSet(rs));
            }
        } catch (SQLException e) {
            Logger.error("Error finding bids by auction ID", e);
        }
        return bids;
    }
    
    /**
     * Lấy bids của 1 user
     */
    public List<Bid> findByUserId(int userId) {
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT b.*, u.username FROM bids b " +
                     "JOIN users u ON b.user_id = u.id " +
                     "WHERE b.user_id = ? " +
                     "ORDER BY b.bid_time DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bids.add(extractBidFromResultSet(rs));
            }
        } catch (SQLException e) {
            Logger.error("Error finding bids by user ID", e);
        }
        return bids;
    }
    
    /**
     * Lấy highest bid của auction
     */
    public Bid findHighestBid(int auctionId) {
        String sql = "SELECT b.*, u.username FROM bids b " +
                     "JOIN users u ON b.user_id = u.id " +
                     "WHERE b.auction_id = ? " +
                     "ORDER BY b.amount DESC LIMIT 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, auctionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractBidFromResultSet(rs);
            }
        } catch (SQLException e) {
            Logger.error("Error finding highest bid", e);
        }
        return null;
    }
    
    /**
     * Đếm số bids của user cho 1 auction
     */
    public int countBidsByUser(int auctionId, int userId) {
        String sql = "SELECT COUNT(*) FROM bids WHERE auction_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, auctionId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            Logger.error("Error counting bids", e);
        }
        return 0;
    }
    
    /**
     * Extract Bid object từ ResultSet
     */
    private Bid extractBidFromResultSet(ResultSet rs) throws SQLException {
        Bid bid = new Bid();
        bid.setId(rs.getInt("id"));
        bid.setAuctionId(rs.getInt("auction_id"));
        bid.setUserId(rs.getInt("user_id"));
        bid.setUsername(rs.getString("username"));
        bid.setAmount(rs.getLong("amount"));
        bid.setAutoBid(rs.getBoolean("is_auto_bid"));
        bid.setMaxAutoBid(rs.getLong("max_auto_bid"));
        bid.setBidTime(rs.getTimestamp("bid_time"));
        return bid;
    }
}

