package com.auction.controller;

import com.auction.dto.ApiResponse;
import com.auction.model.AutoBid;
import com.auction.model.User;
import com.auction.service.AutoBidService;
import com.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller: AutoBid
 * REST API cho auto bidding
 */
@RestController
@RequestMapping("/api/autobids")
@RequiredArgsConstructor
public class AutoBidController {

    private final AutoBidService autoBidService;
    private final UserService userService;

    /**
     * Tạo auto bid
     * POST /api/autobids
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AutoBid>> createAutoBid(
            @RequestParam Long userId,
            @RequestParam Long auctionId,
            @RequestParam BigDecimal maxAmount,
            @RequestParam BigDecimal incrementAmount) {
        try {
            User user = userService.findById(userId);
            AutoBid autoBid = autoBidService.createAutoBid(user, auctionId, maxAmount, incrementAmount);
            return ResponseEntity.ok(ApiResponse.success("Tạo auto bid thành công!", autoBid));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Lấy auto bid của user cho auction
     * GET /api/autobids/auction/{auctionId}
     */
    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<ApiResponse<AutoBid>> getUserAutoBid(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            AutoBid autoBid = autoBidService.getUserAutoBid(user, auctionId);
            return ResponseEntity.ok(ApiResponse.success("Lấy thông tin thành công", autoBid));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Lấy tất cả auto bids của user
     * GET /api/autobids/user
     */
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<AutoBid>>> getUserAutoBids(@RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            List<AutoBid> autoBids = autoBidService.getUserAutoBids(user);
            return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thành công", autoBids));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Hủy auto bid
     * DELETE /api/autobids/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelAutoBid(
            @PathVariable Long id,
            @RequestParam Long userId) {
        try {
            autoBidService.cancelAutoBid(id, userId);
            return ResponseEntity.ok(ApiResponse.success("Đã hủy auto bid"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}


