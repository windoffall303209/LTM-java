package com.auction.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility để hash và verify passwords
 * Sử dụng SHA-256 với salt (hoặc có thể dùng BCrypt nếu có thư viện)
 */
public class PasswordUtil {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Hash password với salt
     * Format: salt$hash
     */
    public static String hashPassword(String password) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Hash password với salt
            String hash = hashWithSalt(password, salt);
            
            // Return: base64(salt)$hash
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            return saltBase64 + "$" + hash;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }
    
    /**
     * Verify password với hash đã lưu
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Split salt và hash
            String[] parts = storedHash.split("\\$");
            if (parts.length != 2) {
                return false;
            }
            
            String saltBase64 = parts[0];
            String hash = parts[1];
            
            // Decode salt
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            
            // Hash password input với cùng salt
            String inputHash = hashWithSalt(password, salt);
            
            // So sánh
            return hash.equals(inputHash);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Hash password với salt cho trước
     */
    private static String hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        
        // Convert to hex string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    /**
     * Tạo password hash đơn giản (không có salt - CHỈ DÙNG ĐỂ TEST)
     * Trong production nên dùng hashPassword()
     */
    public static String simpleHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hashedPassword = md.digest(password.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }
    
    /**
     * Tạo hash giống BCrypt format để tương thích với sample data
     * (Trong thực tế nên dùng thư viện BCrypt thật)
     */
    public static String createBCryptCompatibleHash(String password) {
        // Đơn giản hóa: dùng SHA-256 nhưng format giống BCrypt
        String hash = simpleHash(password);
        return "$2a$10$" + hash;
    }
    
    /**
     * Verify password với BCrypt-like hash
     */
    public static boolean verifyBCryptLike(String password, String storedHash) {
        if (storedHash.startsWith("$2a$10$")) {
            String hash = storedHash.substring(7);
            return hash.equals(simpleHash(password));
        }
        return false;
    }
}

