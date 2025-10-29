package com.auction.controller;

import com.auction.dto.ApiResponse;
import com.auction.dto.AuctionDTO;
import com.auction.dto.UserDTO;
import com.auction.model.Auction;
import com.auction.service.AuctionService;
import com.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller: Admin
 * REST API cho quản trị viên
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AuctionService auctionService;
    private final UserService userService;

    // ==================== AUCTION MANAGEMENT ====================

    /**
     * Tạo auction mới
     * POST /api/admin/auctions
     */
    @PostMapping("/auctions")
    public ResponseEntity<ApiResponse<Auction>> createAuction(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam BigDecimal startingPrice,
            @RequestParam Integer durationMinutes,
            @RequestParam(required = false) String imageUrl) {
        try {
            Auction auction = new Auction();
            auction.setTitle(title);
            auction.setDescription(description);
            auction.setStartingPrice(startingPrice);
            auction.setCurrentPrice(startingPrice);
            auction.setDurationMinutes(durationMinutes);
            auction.setImageUrl(imageUrl);
            auction.setStatus(Auction.AuctionStatus.PENDING);
            
            // Set thời gian - sẽ được set khi start auction
            LocalDateTime now = LocalDateTime.now();
            auction.setStartTime(now);
            auction.setEndTime(now.plusMinutes(durationMinutes));

            Auction created = auctionService.createAuction(auction);
            return ResponseEntity.ok(ApiResponse.success("Tạo đấu giá thành công!", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Cập nhật auction
     * PUT /api/admin/auctions/{id}
     */
    @PutMapping("/auctions/{id}")
    public ResponseEntity<ApiResponse<Auction>> updateAuction(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal startingPrice,
            @RequestParam(required = false) Integer durationMinutes,
            @RequestParam(required = false) String imageUrl) {
        try {
            Auction auction = auctionService.getAuctionById(id);

            if (title != null) auction.setTitle(title);
            if (description != null) auction.setDescription(description);
            if (startingPrice != null) auction.setStartingPrice(startingPrice);
            if (durationMinutes != null) auction.setDurationMinutes(durationMinutes);
            if (imageUrl != null) auction.setImageUrl(imageUrl);

            Auction updated = auctionService.updateAuction(auction);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công!", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Xóa auction
     * DELETE /api/admin/auctions/{id}
     */
    @DeleteMapping("/auctions/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAuction(@PathVariable Long id) {
        try {
            auctionService.deleteAuction(id);
            return ResponseEntity.ok(ApiResponse.success("Đã xóa auction"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Lấy tất cả auctions (bao gồm cả ended)
     * GET /api/admin/auctions/all
     */
    @GetMapping("/auctions/all")
    public ResponseEntity<ApiResponse<List<AuctionDTO>>> getAllAuctions() {
        try {
            List<AuctionDTO> auctions = auctionService.getAllAuctions();
            return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thành công", auctions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Bắt đầu auction
     * POST /api/admin/auctions/{id}/start
     */
    @PostMapping("/auctions/{id}/start")
    public ResponseEntity<ApiResponse<Void>> startAuction(@PathVariable Long id) {
        try {
            auctionService.startAuction(id);
            return ResponseEntity.ok(ApiResponse.success("Đã bắt đầu auction"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Kết thúc auction
     * POST /api/admin/auctions/{id}/end
     */
    @PostMapping("/auctions/{id}/end")
    public ResponseEntity<ApiResponse<Void>> endAuction(@PathVariable Long id) {
        try {
            auctionService.endAuction(id);
            return ResponseEntity.ok(ApiResponse.success("Đã kết thúc auction"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // ==================== USER MANAGEMENT ====================

    /**
     * Lấy tất cả users
     * GET /api/admin/users
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thành công", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Ban/Unban user
     * POST /api/admin/users/{id}/toggle-status
     */
    @PostMapping("/users/{id}/toggle-status")
    public ResponseEntity<ApiResponse<Void>> toggleUserStatus(@PathVariable Long id) {
        try {
            userService.toggleUserStatus(id);
            return ResponseEntity.ok(ApiResponse.success("Đã cập nhật trạng thái user"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Cập nhật số dư user
     * POST /api/admin/users/{id}/update-balance
     */
    @PostMapping("/users/{id}/update-balance")
    public ResponseEntity<ApiResponse<Void>> updateUserBalance(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {
        try {
            userService.updateBalance(id, amount);
            return ResponseEntity.ok(ApiResponse.success("Đã cập nhật số dư"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // ==================== STATISTICS ====================

    /**
     * Lấy thống kê hệ thống
     * GET /api/admin/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Object>> getStatistics() {
        try {
            // Tạo object thống kê
            var stats = new java.util.HashMap<String, Object>();
            
            List<UserDTO> users = userService.getAllUsers();
            List<AuctionDTO> auctions = auctionService.getAllAuctions();
            
            stats.put("totalUsers", users.size());
            stats.put("activeUsers", users.stream().filter(u -> u.getIsActive()).count());
            stats.put("totalAuctions", auctions.size());
            stats.put("activeAuctions", auctions.stream()
                    .filter(a -> "ACTIVE".equals(a.getStatus())).count());
            stats.put("endedAuctions", auctions.stream()
                    .filter(a -> "ENDED".equals(a.getStatus())).count());
            
            BigDecimal totalBidValue = auctions.stream()
                    .map(AuctionDTO::getCurrentPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.put("totalBidValue", totalBidValue);

            return ResponseEntity.ok(ApiResponse.success("Lấy thống kê thành công", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

