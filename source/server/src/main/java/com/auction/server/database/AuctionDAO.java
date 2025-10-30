package com.auction.server.database;

import com.auction.server.model.Auction;
import com.auction.server.model.Product;
import com.auction.server.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho Auction
 */
public class AuctionDAO {
    private Connection connection;
    
    public AuctionDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            Logger.error("Failed to get database connection", e);
        }
    }
    
    /**
     * Thêm auction mới
     */
    public boolean insertAuction(Auction auction) {
        String sql = "INSERT INTO auctions (product_id, start_price, current_price, status, start_time, end_time, duration, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, auction.getProductId());
            stmt.setLong(2, auction.getStartPrice());
            stmt.setLong(3, auction.getCurrentPrice());
            stmt.setString(4, auction.getStatus());
            stmt.setTimestamp(5, auction.getStartTime());
            stmt.setTimestamp(6, auction.getEndTime());
            stmt.setInt(7, auction.getDuration());
            stmt.setInt(8, auction.getCreatedBy());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    auction.setId(rs.getInt(1));
                }
                Logger.info("Auction created: ID " + auction.getId());
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error inserting auction", e);
        }
        return false;
    }
    
    /**
     * Tìm auction theo ID (với product info)
     */
    public Auction findById(int id) {
        String sql = "SELECT a.*, p.*, u.username AS winner_username " +
                     "FROM auctions a " +
                     "JOIN products p ON a.product_id = p.id " +
                     "LEFT JOIN users u ON a.winner_id = u.id " +
                     "WHERE a.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractAuctionFromResultSet(rs);
            }
        } catch (SQLException e) {
            Logger.error("Error finding auction by ID", e);
        }
        return null;
    }
    
    /**
     * Lấy tất cả auctions active
     */
    public List<Auction> findActive() {
        List<Auction> auctions = new ArrayList<>();
        String sql = "SELECT a.*, p.*, u.username AS winner_username " +
                     "FROM auctions a " +
                     "JOIN products p ON a.product_id = p.id " +
                     "LEFT JOIN users u ON a.winner_id = u.id " +
                     "WHERE a.status = 'active' " +
                     "ORDER BY a.end_time ASC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                auctions.add(extractAuctionFromResultSet(rs));
            }
        } catch (SQLException e) {
            Logger.error("Error finding active auctions", e);
        }
        return auctions;
    }
    
    /**
     * Lấy tất cả auctions
     */
    public List<Auction> findAll() {
        List<Auction> auctions = new ArrayList<>();
        String sql = "SELECT a.*, p.*, u.username AS winner_username " +
                     "FROM auctions a " +
                     "JOIN products p ON a.product_id = p.id " +
                     "LEFT JOIN users u ON a.winner_id = u.id " +
                     "ORDER BY a.created_at DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                auctions.add(extractAuctionFromResultSet(rs));
            }
        } catch (SQLException e) {
            Logger.error("Error finding all auctions", e);
        }
        return auctions;
    }
    
    /**
     * Update current price và winner
     */
    public boolean updateBid(int auctionId, long newPrice, int winnerId) {
        String sql = "UPDATE auctions SET current_price = ?, winner_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, newPrice);
            stmt.setInt(2, winnerId);
            stmt.setInt(3, auctionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.error("Error updating auction bid", e);
        }
        return false;
    }
    
    /**
     * Update auction status
     */
    public boolean updateStatus(int auctionId, String status) {
        String sql = "UPDATE auctions SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, auctionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.error("Error updating auction status", e);
        }
        return false;
    }
    
    /**
     * Update end time (khi extend)
     */
    public boolean updateEndTime(int auctionId, Timestamp newEndTime, int extensionCount) {
        String sql = "UPDATE auctions SET end_time = ?, extension_count = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, newEndTime);
            stmt.setInt(2, extensionCount);
            stmt.setInt(3, auctionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.error("Error updating auction end time", e);
        }
        return false;
    }
    
    /**
     * Delete auction
     */
    public boolean delete(int auctionId) {
        String sql = "DELETE FROM auctions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, auctionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.error("Error deleting auction", e);
        }
        return false;
    }
    
    /**
     * Extract Auction object từ ResultSet
     */
    private Auction extractAuctionFromResultSet(ResultSet rs) throws SQLException {
        Auction auction = new Auction();
        auction.setId(rs.getInt("id"));
        auction.setProductId(rs.getInt("product_id"));
        auction.setStartPrice(rs.getLong("start_price"));
        auction.setCurrentPrice(rs.getLong("current_price"));
        auction.setReservePrice(rs.getLong("reserve_price"));
        auction.setWinnerId(rs.getObject("winner_id", Integer.class));
        auction.setStatus(rs.getString("status"));
        auction.setStartTime(rs.getTimestamp("start_time"));
        auction.setEndTime(rs.getTimestamp("end_time"));
        auction.setDuration(rs.getInt("duration"));
        auction.setExtensionCount(rs.getInt("extension_count"));
        auction.setCreatedBy(rs.getInt("created_by"));
        auction.setCreatedAt(rs.getTimestamp("created_at"));
        auction.setWinnerUsername(rs.getString("winner_username"));
        
        // Extract product info
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setImageUrl(rs.getString("image_url"));
        product.setCategory(rs.getString("category"));
        auction.setProduct(product);
        
        return auction;
    }
}

