package com.auction.config;

import com.auction.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Cấu hình: Spring Security
 * Xử lý authentication (xác thực) và authorization (phân quyền)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Bean: Password Encoder
     * Mã hóa password bằng BCrypt (strength 12)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Bean: Authentication Manager
     * Quản lý xác thực người dùng
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    /**
     * Bean: Security Filter Chain
     * Cấu hình các rule bảo mật cho HTTP requests
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Sử dụng CORS config từ WebConfig
            .cors(cors -> {})

            // Tắt CSRF vì đây là REST API
            .csrf(csrf -> csrf.disable())

            // Cấu hình phân quyền endpoints
            // Hiện tại: Cho phép tất cả requests để dev dễ dàng
            // Chỗ này sẽ thêm rules cụ thể cho:
            // - Public endpoints: /api/auth/**, /api/auctions (GET)
            // - User endpoints: /api/bids/**, /api/watchlist/**
            // - Admin endpoints: /api/admin/**
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // Cấu hình session management
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )

            // Tắt form login mặc định (dùng REST API)
            .formLogin(form -> form.disable())

            // Tắt HTTP Basic Auth popup
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    // Chỗ này sẽ thêm JWT filter nếu dùng JWT authentication
    // Chỗ này sẽ cấu hình endpoint permissions chi tiết
}
