package com.auction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity: User
 * Đại diện cho người dùng trong hệ thống (cả USER và ADMIN)
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải từ 3-50 ký tự")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Password không được để trống")
    @Column(nullable = false, length = 255)
    private String password; // Password đã mã hóa

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role = Role.USER;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Chỗ này sẽ thêm relationships với Bid và Watchlist:
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // private Set<Bid> bids = new HashSet<>();
    //
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // private Set<Watchlist> watchlists = new HashSet<>();

    // Chỗ này sẽ thêm helper methods:
    // public void addBalance(BigDecimal amount) { ... }
    // public void subtractBalance(BigDecimal amount) { ... }
    // public boolean hasEnoughBalance(BigDecimal amount) { ... }

    /**
     * Enum: Role
     * Phân quyền người dùng
     */
    public enum Role {
        USER,   // Người dùng thường
        ADMIN   // Quản trị viên
    }
}
