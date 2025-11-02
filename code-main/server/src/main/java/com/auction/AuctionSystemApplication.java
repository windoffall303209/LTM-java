package com.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class for Auction System
 *
 * This is the entry point of the Spring Boot application.
 *
 * @SpringBootApplication enables:
 * - Auto-configuration
 * - Component scanning
 * - Configuration properties
 */
@SpringBootApplication
public class AuctionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionSystemApplication.class, args);
        System.out.println("====================================");
        System.out.println("Auction System Started Successfully!");
        System.out.println("Server running on: http://localhost:8080");
        System.out.println("====================================");
    }
}
