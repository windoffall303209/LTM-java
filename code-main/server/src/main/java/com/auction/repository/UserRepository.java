package com.auction.repository;

import com.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository: User
 * Lớp truy cập dữ liệu cho User entity
 *
 * JpaRepository cung cấp sẵn các methods:
 * - save(), findById(), findAll(), deleteById(), count(), v.v.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Tìm user theo username
     * Dùng cho: Authentication (đăng nhập)
     */
    Optional<User> findByUsername(String username);

    /**
     * Tìm user theo email
     * Dùng cho: Validation khi đăng ký
     */
    Optional<User> findByEmail(String email);

    /**
     * Kiểm tra username đã tồn tại chưa
     * Dùng cho: Validation khi đăng ký
     */
    boolean existsByUsername(String username);

    /**
     * Kiểm tra email đã tồn tại chưa
     * Dùng cho: Validation khi đăng ký
     */
    boolean existsByEmail(String email);

    // Chỗ này sẽ thêm custom queries:
    // - List<User> findByRole(User.Role role);
    // - List<User> findByIsActive(Boolean isActive);
    // - @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword%")
    //   List<User> searchByKeyword(@Param("keyword") String keyword);
}
