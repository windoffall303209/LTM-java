package com.auction.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {
    
    // Endpoint kiểm tra trạng thái server
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Máy chủ Hệ Thống Đấu Giá đang chạy");
        return response;
    }
}

