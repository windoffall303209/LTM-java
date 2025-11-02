package com.auction.service;

import com.auction.dto.UserDTO;
import com.auction.model.User;
import com.auction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service: Admin User Management
 * Xử lý các chức năng quản lý user dành riêng cho Admin
 */
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    /**
     * Lấy tất cả users (Admin only)
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Ban/Unban user (Admin only)
     */
    @Transactional
    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
    }

    /**
     * Cập nhật số dư user (Admin only)
     */
    @Transactional
    public void updateBalance(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        user.addBalance(amount);
        userRepository.save(user);
    }

    /**
     * Lấy thống kê về users (Admin only)
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

        // Tổng số dư trong hệ thống
        BigDecimal totalBalance = allUsers.stream()
                .map(User::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalSystemBalance", totalBalance);

        return stats;
    }

    /**
     * Xóa user (Admin only) - Cẩn thận khi dùng
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Không cho phép xóa admin
        if (user.getRole() == User.Role.ADMIN) {
            throw new RuntimeException("Không thể xóa tài khoản admin");
        }

        userRepository.delete(user);
    }

    /**
     * Tìm users theo role (Admin only)
     */
    public List<UserDTO> getUsersByRole(User.Role role) {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == role)
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Tìm users theo trạng thái active (Admin only)
     */
    public List<UserDTO> getUsersByActiveStatus(boolean isActive) {
        return userRepository.findAll().stream()
                .filter(u -> u.getIsActive() == isActive)
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Reset password user (Admin only)
     * TODO: Implement nếu cần
     */
    @Transactional
    public void resetUserPassword(Long userId, String newPassword) {
        // Implement logic reset password
        throw new UnsupportedOperationException("Chức năng chưa được triển khai");
    }
}
