package com.auction.controller;

import com.auction.dto.ApiResponse;
import com.auction.dto.AuctionDTO;
import com.auction.dto.UserDTO;
import com.auction.model.Auction;
import com.auction.service.AdminAuctionService;
import com.auction.service.AdminStatisticsService;
import com.auction.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller: Admin
 * REST API cho quản trị viên
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminAuctionService adminAuctionService;
    private final AdminUserService adminUserService;
    private final AdminStatisticsService adminStatisticsService;

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
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer durationMinutes,
            @RequestParam(required = false) String imageUrl) {
        try {
            Auction auction = new Auction();
            auction.setTitle(title);
            auction.setDescription(description);
            auction.setStartingPrice(startingPrice);
            auction.setCurrentPrice(startingPrice);
            auction.setImageUrl(imageUrl);
            auction.setStatus(Auction.AuctionStatus.PENDING);
            
            // Set thời gian
            LocalDateTime start;
            LocalDateTime end;
            
            if (startTime != null && !startTime.isEmpty()) {
                // Parse from ISO datetime string (yyyy-MM-ddTHH:mm)
                start = LocalDateTime.parse(startTime);
            } else {
                start = LocalDateTime.now();
            }
            
            if (endTime != null && !endTime.isEmpty()) {
                end = LocalDateTime.parse(endTime);
            } else if (durationMinutes != null && durationMinutes > 0) {
                end = start.plusMinutes(durationMinutes);
            } else {
                // Mặc định 60 phút
                end = start.plusMinutes(60);
            }
            
            auction.setStartTime(start);
            auction.setEndTime(end);
            auction.setDurationMinutes((int) java.time.Duration.between(start, end).toMinutes());

            Auction created = adminAuctionService.createAuction(auction);

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
            Auction updatedData = new Auction();
            updatedData.setTitle(title);
            updatedData.setDescription(description);
            updatedData.setStartingPrice(startingPrice);
            updatedData.setDurationMinutes(durationMinutes);
            updatedData.setImageUrl(imageUrl);

            Auction updated = adminAuctionService.updateAuction(id, updatedData);
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
            adminAuctionService.deleteAuction(id);
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
            List<AuctionDTO> auctions = adminAuctionService.getAllAuctions();
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
            adminAuctionService.startAuction(id);
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
            adminAuctionService.endAuction(id);
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
            List<UserDTO> users = adminUserService.getAllUsers();
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
            adminUserService.toggleUserStatus(id);
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
            adminUserService.updateBalance(id, amount);
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
            Map<String, Object> stats = adminStatisticsService.getSystemStatistics();
            return ResponseEntity.ok(ApiResponse.success("Lấy thống kê thành công", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

}

