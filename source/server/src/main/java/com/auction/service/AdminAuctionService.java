package com.auction.service;

import com.auction.dto.AuctionDTO;
import com.auction.model.Auction;
import com.auction.model.User;
import com.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service: Admin Auction Management
 * Xử lý các chức năng CRUD và quản lý auction dành riêng cho Admin
 */
@Service
@RequiredArgsConstructor
public class AdminAuctionService {

    private final AuctionRepository auctionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Tạo auction mới (Admin only)
     */
    @Transactional
    public Auction createAuction(Auction auction) {
        auction.setCurrentPrice(auction.getStartingPrice());
        auction.setStatus(Auction.AuctionStatus.PENDING);
        auction.setTotalBids(0);
        auction.setExtendCount(0);

        Auction saved = auctionRepository.save(auction);

        // Broadcast auction created event
        broadcastAuctionEvent("AUCTION_CREATED", saved);

        return saved;
    }

    /**
     * Tạo auction mới với thông tin createdBy (Admin only)
     */
    @Transactional
    public AuctionDTO createAuction(Auction auction, User createdBy) {
        auction.setCreatedBy(createdBy);
        Auction saved = createAuction(auction);
        return AuctionDTO.fromEntity(saved);
    }

    /**
     * Cập nhật auction (Admin only)
     */
    @Transactional
    public Auction updateAuction(Long auctionId, Auction updatedData) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction không tồn tại"));

        // Chỉ cho phép update khi auction chưa active hoặc đã ended
        if (auction.getStatus() == Auction.AuctionStatus.ACTIVE) {
            throw new RuntimeException("Không thể cập nhật auction đang hoạt động");
        }

        // Update các field
        if (updatedData.getTitle() != null) {
            auction.setTitle(updatedData.getTitle());
        }
        if (updatedData.getDescription() != null) {
            auction.setDescription(updatedData.getDescription());
        }
        if (updatedData.getStartingPrice() != null) {
            auction.setStartingPrice(updatedData.getStartingPrice());
        }
        if (updatedData.getImageUrl() != null) {
            auction.setImageUrl(updatedData.getImageUrl());
        }
        if (updatedData.getDurationMinutes() != null) {
            auction.setDurationMinutes(updatedData.getDurationMinutes());
        }
        if (updatedData.getStartTime() != null) {
            auction.setStartTime(updatedData.getStartTime());
        }
        if (updatedData.getEndTime() != null) {
            auction.setEndTime(updatedData.getEndTime());
        }

        return auctionRepository.save(auction);
    }

    /**
     * Xóa auction (Admin only)
     */
    @Transactional
    public void deleteAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction không tồn tại"));

        // Không cho phép xóa auction đang active
        if (auction.getStatus() == Auction.AuctionStatus.ACTIVE) {
            throw new RuntimeException("Không thể xóa auction đang hoạt động");
        }

        auctionRepository.delete(auction);

        // Broadcast auction deleted event
        Map<String, Object> message = new HashMap<>();
        message.put("type", "AUCTION_DELETED");
        message.put("auctionId", auctionId);
        messagingTemplate.convertAndSend("/topic/auctions", message);
    }

    /**
     * Bắt đầu auction (Admin only)
     */
    @Transactional
    public void startAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction không tồn tại"));

        if (auction.getStatus() != Auction.AuctionStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể start auction ở trạng thái PENDING");
        }

        auction.setStatus(Auction.AuctionStatus.ACTIVE);

        // Cập nhật thời gian bắt đầu nếu chưa set
        if (auction.getStartTime() == null) {
            auction.setStartTime(LocalDateTime.now());
        }

        // Cập nhật thời gian kết thúc nếu chưa set
        if (auction.getEndTime() == null && auction.getDurationMinutes() != null) {
            auction.setEndTime(LocalDateTime.now().plusMinutes(auction.getDurationMinutes()));
        }

        auctionRepository.save(auction);

        // Broadcast auction started event
        broadcastAuctionEvent("AUCTION_STARTED", auction);
    }

    /**
     * Kết thúc auction (Admin only)
     */
    @Transactional
    public void endAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction không tồn tại"));

        if (auction.getStatus() != Auction.AuctionStatus.ACTIVE) {
            throw new RuntimeException("Chỉ có thể end auction ở trạng thái ACTIVE");
        }

        auction.setStatus(Auction.AuctionStatus.ENDED);
        auctionRepository.save(auction);

        // Broadcast auction ended event
        broadcastAuctionEvent("AUCTION_ENDED", auction);
    }

    /**
     * Hủy auction (Admin only)
     */
    @Transactional
    public void cancelAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction không tồn tại"));

        auction.setStatus(Auction.AuctionStatus.CANCELLED);
        auctionRepository.save(auction);

        // Broadcast auction cancelled event
        broadcastAuctionEvent("AUCTION_CANCELLED", auction);
    }

    /**
     * Lấy tất cả auctions (Admin only)
     */
    public List<AuctionDTO> getAllAuctions() {
        return auctionRepository.findAll().stream()
                .map(AuctionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Lấy auctions theo status (Admin only)
     */
    public List<AuctionDTO> getAuctionsByStatus(Auction.AuctionStatus status) {
        return auctionRepository.findByStatus(status).stream()
                .map(AuctionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thống kê về auctions (Admin only)
     */
    public Map<String, Object> getAuctionStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<Auction> allAuctions = auctionRepository.findAll();

        stats.put("totalAuctions", allAuctions.size());
        stats.put("activeAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.ACTIVE).count());
        stats.put("pendingAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.PENDING).count());
        stats.put("endedAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.ENDED).count());
        stats.put("cancelledAuctions", allAuctions.stream()
                .filter(a -> a.getStatus() == Auction.AuctionStatus.CANCELLED).count());

        // Tổng số bids trong hệ thống
        int totalBids = allAuctions.stream()
                .mapToInt(Auction::getTotalBids)
                .sum();
        stats.put("totalBids", totalBids);

        return stats;
    }

    /**
     * Extend thời gian auction (Admin only)
     */
    @Transactional
    public void extendAuction(Long auctionId, int minutes) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction không tồn tại"));

        if (auction.getStatus() != Auction.AuctionStatus.ACTIVE) {
            throw new RuntimeException("Chỉ có thể extend auction đang ACTIVE");
        }

        auction.extendAuction(minutes * 60); // Convert minutes to seconds
        auctionRepository.save(auction);

        // Broadcast auction extended event
        Map<String, Object> message = new HashMap<>();
        message.put("type", "AUCTION_EXTENDED");
        message.put("auctionId", auctionId);
        message.put("extendedMinutes", minutes);
        messagingTemplate.convertAndSend("/topic/auction/" + auctionId, message);
    }

    /**
     * Kiểm tra và kết thúc các auctions đã hết hạn (Admin/System)
     */
    @Transactional
    public void checkAndEndExpiredAuctions() {
        List<Auction> expired = auctionRepository.findExpiredAuctions(LocalDateTime.now());
        for (Auction auction : expired) {
            auction.setStatus(Auction.AuctionStatus.ENDED);
            auctionRepository.save(auction);

            // Broadcast auction ended event
            broadcastAuctionEvent("AUCTION_ENDED", auction);
        }
    }

    /**
     * Helper: Broadcast auction events qua WebSocket
     */
    private void broadcastAuctionEvent(String eventType, Auction auction) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", eventType);
        message.put("auctionId", auction.getAuctionId());
        message.put("title", auction.getTitle());
        message.put("status", auction.getStatus().toString());
        message.put("startTime", auction.getStartTime());
        message.put("endTime", auction.getEndTime());

        messagingTemplate.convertAndSend("/topic/auctions", message);
    }
}
