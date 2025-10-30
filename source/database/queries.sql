-- ================================================
-- USEFUL QUERIES CHO HỆ THỐNG ĐẤU GIÁ
-- ================================================

USE auction_db;

-- ================================================
-- 1. XEM DANH SÁCH ĐẤU GIÁ ĐANG DIỄN RA
-- ================================================

SELECT 
    a.id,
    p.name AS product_name,
    a.current_price,
    u.username AS current_winner,
    TIMESTAMPDIFF(SECOND, NOW(), a.end_time) AS time_left_seconds,
    a.status
FROM auctions a
JOIN products p ON a.product_id = p.id
LEFT JOIN users u ON a.winner_id = u.id
WHERE a.status = 'active'
ORDER BY a.end_time ASC;

-- ================================================
-- 2. XEM LỊCH SỬ ĐẶT GIÁ CỦA 1 AUCTION
-- ================================================

SELECT 
    u.username,
    b.amount,
    b.is_auto_bid,
    b.bid_time
FROM bids b
JOIN users u ON b.user_id = u.id
WHERE b.auction_id = 1  -- Thay 1 bằng auction_id cần xem
ORDER BY b.bid_time DESC
LIMIT 20;

-- ================================================
-- 3. TOP BIDDERS (Người đặt giá nhiều nhất)
-- ================================================

SELECT 
    u.username,
    COUNT(b.id) AS total_bids,
    SUM(b.amount) AS total_amount_bid,
    COUNT(DISTINCT b.auction_id) AS auctions_participated
FROM users u
JOIN bids b ON u.id = b.user_id
GROUP BY u.id
ORDER BY total_bids DESC
LIMIT 10;

-- ================================================
-- 4. DANH SÁCH NGƯỜI THẮNG ĐẤU GIÁ
-- ================================================

SELECT 
    u.username,
    p.name AS product_name,
    a.current_price AS final_price,
    a.end_time
FROM auctions a
JOIN users u ON a.winner_id = u.id
JOIN products p ON a.product_id = p.id
WHERE a.status = 'ended'
ORDER BY a.end_time DESC;

-- ================================================
-- 5. THỐNG KÊ THEO USER
-- ================================================

SELECT 
    u.username,
    COUNT(DISTINCT b.auction_id) AS auctions_joined,
    COUNT(b.id) AS total_bids,
    COUNT(CASE WHEN a.winner_id = u.id THEN 1 END) AS auctions_won,
    MAX(b.amount) AS highest_bid
FROM users u
LEFT JOIN bids b ON u.id = b.user_id
LEFT JOIN auctions a ON b.auction_id = a.id
WHERE u.role = 'user'
GROUP BY u.id;

-- ================================================
-- 6. ĐẤU GIÁ SẮP KẾT THÚC (trong 5 phút)
-- ================================================

SELECT 
    a.id,
    p.name,
    a.current_price,
    TIMESTAMPDIFF(SECOND, NOW(), a.end_time) AS seconds_left
FROM auctions a
JOIN products p ON a.product_id = p.id
WHERE a.status = 'active'
  AND a.end_time BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 5 MINUTE)
ORDER BY a.end_time ASC;

-- ================================================
-- 7. HOẠT ĐỘNG GẦN NHẤT CỦA HỆ THỐNG
-- ================================================

SELECT 
    l.action,
    u.username,
    l.details,
    l.created_at
FROM logs l
LEFT JOIN users u ON l.user_id = u.id
ORDER BY l.created_at DESC
LIMIT 50;

-- ================================================
-- 8. REVENUE (Tổng doanh thu từ các đấu giá đã kết thúc)
-- ================================================

SELECT 
    COUNT(*) AS total_auctions_ended,
    SUM(current_price) AS total_revenue,
    AVG(current_price) AS average_price,
    MAX(current_price) AS highest_sale
FROM auctions
WHERE status = 'ended';

-- ================================================
-- 9. SẢN PHẨM HOT (Có nhiều bids nhất)
-- ================================================

SELECT 
    p.name,
    COUNT(b.id) AS total_bids,
    MAX(b.amount) AS highest_bid,
    a.status
FROM products p
JOIN auctions a ON p.id = a.product_id
JOIN bids b ON a.id = b.auction_id
GROUP BY p.id
ORDER BY total_bids DESC
LIMIT 10;

-- ================================================
-- 10. USER CHƯA THAM GIA ĐẤU GIÁ NÀO
-- ================================================

SELECT u.username, u.email, u.created_at
FROM users u
LEFT JOIN bids b ON u.id = b.user_id
WHERE b.id IS NULL
  AND u.role = 'user';

-- ================================================
-- 11. RESET DATA (XÓA TẤT CẢ DATA - CAREFUL!)
-- ================================================

-- TRUNCATE TABLE logs;
-- TRUNCATE TABLE bids;
-- TRUNCATE TABLE auctions;
-- TRUNCATE TABLE products;
-- TRUNCATE TABLE users;

-- ================================================
-- 12. BACKUP COMMANDS
-- ================================================

-- Export database:
-- mysqldump -u root -p auction_db > backup.sql

-- Import database:
-- mysql -u root -p auction_db < backup.sql

-- ================================================
-- 13. CHECK AUCTION STATUS (Cập nhật status tự động)
-- ================================================

-- Update ended auctions
UPDATE auctions 
SET status = 'ended' 
WHERE status = 'active' 
  AND end_time < NOW();

-- Start pending auctions
UPDATE auctions 
SET status = 'active' 
WHERE status = 'pending' 
  AND start_time <= NOW();

