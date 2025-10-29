package com.auction.config;

import com.auction.model.User;
import com.auction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Component: Data Initializer
 * Khởi tạo dữ liệu mẫu khi ứng dụng start
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("===== STARTING DATA INITIALIZATION =====");
        
        // Tạo admin account nếu chưa tồn tại
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@auction.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setFullName("Administrator");
            admin.setBalance(new BigDecimal("2000000000")); // 2 tỷ VND
            admin.setRole(User.Role.ADMIN);
            admin.setIsActive(true);
            
            userRepository.save(admin);
            log.info("✅ Created admin account: username=admin, password=admin");
        } else {
            log.info("ℹ️ Admin account already exists");
        }

        // Tạo một vài user demo nếu cần
        if (!userRepository.existsByUsername("user1")) {
            User user1 = new User();
            user1.setUsername("user1");
            user1.setEmail("user1@auction.com");
            user1.setPassword(passwordEncoder.encode("123456"));
            user1.setFullName("Nguyễn Văn A");
            user1.setBalance(new BigDecimal("2000000000")); // 2 tỷ VND
            user1.setRole(User.Role.USER);
            user1.setIsActive(true);
            
            userRepository.save(user1);
            log.info("✅ Created demo user: username=user1, password=123456");
        }

        if (!userRepository.existsByUsername("user2")) {
            User user2 = new User();
            user2.setUsername("user2");
            user2.setEmail("user2@auction.com");
            user2.setPassword(passwordEncoder.encode("123456"));
            user2.setFullName("Trần Thị B");
            user2.setBalance(new BigDecimal("2000000000")); // 2 tỷ VND
            user2.setRole(User.Role.USER);
            user2.setIsActive(true);
            
            userRepository.save(user2);
            log.info("✅ Created demo user: username=user2, password=123456");
        }

        log.info("===== DATA INITIALIZATION COMPLETED =====");
        log.info("");
        log.info("📝 Demo Accounts:");
        log.info("   Admin  -> username: admin  | password: admin");
        log.info("   User 1 -> username: user1  | password: 123456");
        log.info("   User 2 -> username: user2  | password: 123456");
        log.info("");
    }
}


