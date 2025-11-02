package com.auction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cấu hình: Web / CORS
 * Cho phép frontend (chạy trên port khác) gọi API backend
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Cấu hình CORS (Cross-Origin Resource Sharing)
     * Cho phép các origin sau truy cập API:
     * - localhost:5500 (Live Server)
     * - localhost:3000 (React dev server nếu dùng)
     * - 127.0.0.1:5500
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:5500",
                    "http://127.0.0.1:5500",
                    "http://localhost:3000"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // Chỗ này sẽ thêm các cấu hình web khác nếu cần
}
