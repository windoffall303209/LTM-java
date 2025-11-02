package com.auction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Cấu hình: WebSocket
 * Cho phép giao tiếp real-time 2 chiều giữa server và client
 *
 * Sử dụng cho:
 * - Cập nhật giá đấu giá real-time
 * - Thông báo bid mới
 * - Cập nhật trạng thái auction
 * - Notifications
 *
 * Chỗ này sẽ thêm:
 * - WebSocketController để handle messages
 * - Format message cho các loại event
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Cấu hình Message Broker
     * - /topic: Prefix cho các topic broadcast đến nhiều clients
     * - /app: Prefix cho các message gửi từ client lên server
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple broker cho topic-based messaging
        config.enableSimpleBroker("/topic");

        // Prefix cho application destinations
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Đăng ký STOMP endpoints
     * Clients sẽ kết nối WebSocket qua endpoint này
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(
                    "http://localhost:5500",
                    "http://127.0.0.1:5500",
                    "http://localhost:3000"
                )
                .withSockJS();
    }
}
