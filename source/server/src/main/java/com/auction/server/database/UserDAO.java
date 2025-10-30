package com.auction.server.database;

import com.auction.server.model.User;
import com.auction.server.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho User
 */
public class UserDAO {
    private Connection connection;
    
    public UserDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            Logger.error("Failed to get database connection", e);
        }
    }
    
    /**
     * Thêm user mới
     */
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (username, password_hash, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
                Logger.info("User created: " + user.getUsername());
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error inserting user", e);
        }
        return false;
    }
    
    /**
     * Tìm user theo username
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            Logger.error("Error finding user by username", e);
        }
        return null;
    }
    
    /**
     * Tìm user theo ID
     */
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            Logger.error("Error finding user by ID", e);
        }
        return null;
    }
    
    /**
     * Lấy tất cả users
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            Logger.error("Error finding all users", e);
        }
        return users;
    }
    
    /**
     * Update last login time
     */
    public boolean updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.error("Error updating last login", e);
        }
        return false;
    }
    
    /**
     * Ban user
     */
    public boolean banUser(int userId) {
        String sql = "UPDATE users SET is_banned = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.error("Error banning user", e);
        }
        return false;
    }
    
    /**
     * Unban user
     */
    public boolean unbanUser(int userId) {
        String sql = "UPDATE users SET is_banned = FALSE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.error("Error unbanning user", e);
        }
        return false;
    }
    
    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }
    
    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Logger.error("Error checking email existence", e);
        }
        return false;
    }
    
    /**
     * Extract User object từ ResultSet
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setBanned(rs.getBoolean("is_banned"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        return user;
    }
}

