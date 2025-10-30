package com.auction.server.service;

import com.auction.server.database.UserDAO;
import com.auction.server.model.User;
import com.auction.server.util.Logger;
import com.auction.server.util.PasswordUtil;

/**
 * Service xử lý authentication
 */
public class AuthService {
    private UserDAO userDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Đăng ký user mới
     */
    public User register(String username, String password, String email) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        
        // Check if username exists
        if (userDAO.usernameExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if email exists
        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Hash password
        String passwordHash = PasswordUtil.hashPassword(password);
        
        // Create user
        User user = new User(username, passwordHash, email);
        if (userDAO.insertUser(user)) {
            Logger.info("User registered: " + username);
            return user;
        }
        
        return null;
    }
    
    /**
     * Đăng nhập
     */
    public User login(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        // Find user
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Username not found");
        }
        
        // Check if banned
        if (user.isBanned()) {
            throw new IllegalArgumentException("User is banned");
        }
        
        // Verify password
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            // Thử verify với BCrypt-like hash (cho sample data)
            if (!PasswordUtil.verifyBCryptLike(password, user.getPasswordHash())) {
                throw new IllegalArgumentException("Invalid password");
            }
        }
        
        // Update last login
        userDAO.updateLastLogin(user.getId());
        
        Logger.info("User logged in: " + username);
        return user;
    }
    
    /**
     * Logout (chỉ log)
     */
    public void logout(String username) {
        Logger.info("User logged out: " + username);
    }
}

