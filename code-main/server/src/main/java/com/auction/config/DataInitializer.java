package com.auction.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Khởi tạo dữ liệu ban đầu
 * Chạy tự động khi application start
 *
 * Chỗ này sẽ implement logic khởi tạo data:
 * - Tạo tài khoản admin mặc định (username: admin, password: admin123)
 * - Tạo tài khoản user test
 * - Tạo sample auctions (optional)
 * - Set balance ban đầu cho users
 *
 * Cách implement:
 * 1. @Autowired UserRepository và PasswordEncoder
 * 2. Trong run() method:
 *    - Check xem admin đã tồn tại chưa
 *    - Nếu chưa, tạo admin user
 *    - Tạo test users
 *    - Tạo sample auctions (optional)
 */
@Component
public class DataInitializer implements CommandLineRunner {

    // Chỗ này sẽ inject dependencies
    // @Autowired
    // private UserRepository userRepository;
    //
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    //
    // @Autowired
    // private AuctionRepository auctionRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println("Data Initializer Started");
        System.out.println("========================================");

        // Chỗ này sẽ thêm logic khởi tạo data

        // Ví dụ tạo admin:
        // if (!userRepository.existsByUsername("admin")) {
        //     User admin = new User();
        //     admin.setUsername("admin");
        //     admin.setEmail("admin@auction.com");
        //     admin.setPassword(passwordEncoder.encode("admin123"));
        //     admin.setRole(User.Role.ADMIN);
        //     admin.setBalance(new BigDecimal("999999999"));
        //     userRepository.save(admin);
        //     System.out.println("✓ Admin user created: admin/admin123");
        // }

        // Ví dụ tạo test user:
        // if (!userRepository.existsByUsername("user1")) {
        //     User user = new User();
        //     user.setUsername("user1");
        //     user.setEmail("user1@test.com");
        //     user.setPassword(passwordEncoder.encode("123456"));
        //     user.setRole(User.Role.USER);
        //     user.setBalance(new BigDecimal("10000000"));
        //     userRepository.save(user);
        //     System.out.println("✓ Test user created: user1/123456");
        // }

        System.out.println("========================================");
        System.out.println("Data Initializer Completed");
        System.out.println("========================================");
    }
}
