package com.auction.repository;

import com.auction.model.Auction;
import com.auction.model.Auction.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository: Auction
 * Lớp truy cập dữ liệu cho Auction entity
 */
@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    /**
     * Tìm auctions theo status
     * Dùng cho: Lấy danh sách auctions ACTIVE/PENDING/ENDED
     */
    List<Auction> findByStatus(AuctionStatus status);

    /**
     * Tìm auctions theo status, sắp xếp theo thời gian kết thúc
     * Dùng cho: Hiển thị auctions sắp kết thúc
     */
    List<Auction> findByStatusOrderByEndTimeAsc(AuctionStatus status);

    // Chỗ này sẽ thêm custom queries:
    //
    // Tìm kiếm theo keyword:
    // List<Auction> findByTitleContainingOrDescriptionContaining(String title, String desc);
    //
    // Tìm auctions đã hết hạn (cho scheduler):
    // List<Auction> findByEndTimeBeforeAndStatus(LocalDateTime time, AuctionStatus status);
    //
    // Tìm auctions được tạo bởi user:
    // List<Auction> findByCreatedBy(User user);
}
