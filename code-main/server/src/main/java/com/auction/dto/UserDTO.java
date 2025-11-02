package com.auction.dto;

import com.auction.model.User;
import java.math.BigDecimal;

/**
 * User Data Transfer Object
 *
 * Used to send user data to frontend.
 * Does NOT include sensitive information like password.
 *
 * Chỗ này sẽ thêm factory method để convert từ User entity
 * Example: public static UserDTO fromEntity(User user)
 */
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String role;
    private BigDecimal balance;
    private String status;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String role, BigDecimal balance, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.balance = balance;
        this.status = status;
    }

    // Chỗ này sẽ thêm factory method:
    // public static UserDTO fromEntity(User user) {
    //     return new UserDTO(
    //         user.getId(),
    //         user.getUsername(),
    //         user.getEmail(),
    //         user.getRole().name(),
    //         user.getBalance(),
    //         user.getStatus().name()
    //     );
    // }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
