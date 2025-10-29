package com.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Application Class - Online Auction System
 * Network Programming Project
 * 
 * @EnableAsync: Cho phép xử lý bất đồng bộ
 * @EnableScheduling: Cho phép scheduled tasks (countdown timer)
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class AuctionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionSystemApplication.class, args);
        System.out.println("\n" +
                "===========================================\n" +
                "   AUCTION SYSTEM - SERVER STARTED\n" +
                "   Access: http://localhost:8080\n" +
                "   WebSocket: ws://localhost:8080/ws\n" +
                "===========================================\n");
    }
}

