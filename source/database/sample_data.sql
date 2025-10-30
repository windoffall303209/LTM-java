-- ================================================
-- DỮ LIỆU MẪU CHO TESTING
-- ================================================

USE auction_db;

-- ================================================
-- 1. USERS (password: "admin123" - đã hash bằng BCrypt)
-- ================================================

-- Admin account
INSERT INTO users (username, password_hash, email, role) VALUES
('admin', '$2a$10$xqLhS3LqrKvdqPVXvqGqM.N7HqJ5qZU5yW.wZ3gGQ.m6zJZc6F3HG', 'admin@auction.com', 'admin');

-- Regular users (Alice, Bob, Charlie)
INSERT INTO users (username, password_hash, email, role) VALUES
('alice', '$2a$10$xqLhS3LqrKvdqPVXvqGqM.N7HqJ5qZU5yW.wZ3gGQ.m6zJZc6F3HG', 'alice@mail.com', 'user'),
('bob', '$2a$10$xqLhS3LqrKvdqPVXvqGqM.N7HqJ5qZU5yW.wZ3gGQ.m6zJZc6F3HG', 'bob@mail.com', 'user'),
('charlie', '$2a$10$xqLhS3LqrKvdqPVXvqGqM.N7HqJ5qZU5yW.wZ3gGQ.m6zJZc6F3HG', 'charlie@mail.com', 'user'),
('david', '$2a$10$xqLhS3LqrKvdqPVXvqGqM.N7HqJ5qZU5yW.wZ3gGQ.m6zJZc6F3HG', 'david@mail.com', 'user'),
('eve', '$2a$10$xqLhS3LqrKvdqPVXvqGqM.N7HqJ5qZU5yW.wZ3gGQ.m6zJZc6F3HG', 'eve@mail.com', 'user');

-- ================================================
-- 2. PRODUCTS
-- ================================================

INSERT INTO products (name, description, image_url, category) VALUES
('iPhone 15 Pro Max 256GB', 'Điện thoại Apple iPhone 15 Pro Max, màu Titan Tự Nhiên, bộ nhớ 256GB. Còn mới 99%, fullbox.', 'assets/images/iphone15.jpg', 'Điện Tử'),
('MacBook Pro M3 14 inch', 'MacBook Pro 14 inch với chip M3, RAM 16GB, SSD 512GB. Bảo hành 11 tháng.', 'assets/images/macbook.jpg', 'Điện Tử'),
('Sony WH-1000XM5', 'Tai nghe chống ồn cao cấp Sony WH-1000XM5, màu đen, còn mới.', 'assets/images/headphone.jpg', 'Điện Tử'),
('Canon EOS R6', 'Máy ảnh Canon EOS R6 body only, đã qua sử dụng 6 tháng, còn rất mới.', 'assets/images/camera.jpg', 'Máy Ảnh'),
('Samsung Galaxy Watch 6', 'Đồng hồ thông minh Samsung Galaxy Watch 6, 44mm, màu xám.', 'assets/images/watch.jpg', 'Điện Tử'),
('iPad Pro 12.9 inch M2', 'iPad Pro 12.9 inch với chip M2, 256GB, WiFi + Cellular.', 'assets/images/ipad.jpg', 'Điện Tử'),
('PlayStation 5', 'Máy chơi game Sony PlayStation 5 phiên bản Disc, fullbox.', 'assets/images/ps5.jpg', 'Game'),
('Nintendo Switch OLED', 'Máy chơi game Nintendo Switch phiên bản OLED, màu trắng.', 'assets/images/switch.jpg', 'Game');

-- ================================================
-- 3. AUCTIONS
-- ================================================

-- Active auctions (đang diễn ra)
INSERT INTO auctions (product_id, start_price, current_price, status, start_time, end_time, duration, created_by) VALUES
(1, 20000000, 25000000, 'active', NOW(), DATE_ADD(NOW(), INTERVAL 5 MINUTE), 300, 1),
(2, 30000000, 32000000, 'active', NOW(), DATE_ADD(NOW(), INTERVAL 10 MINUTE), 600, 1),
(3, 5000000, 5500000, 'active', NOW(), DATE_ADD(NOW(), INTERVAL 3 MINUTE), 180, 1);

-- Pending auctions (chưa bắt đầu)
INSERT INTO auctions (product_id, start_price, current_price, status, start_time, end_time, duration, created_by) VALUES
(4, 25000000, 25000000, 'pending', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 1 HOUR 10 MINUTE), 600, 1),
(5, 8000000, 8000000, 'pending', DATE_ADD(NOW(), INTERVAL 2 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR 5 MINUTE), 300, 1);

-- Ended auctions (đã kết thúc)
INSERT INTO auctions (product_id, start_price, current_price, winner_id, status, start_time, end_time, duration, created_by) VALUES
(6, 18000000, 22000000, 2, 'ended', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY) + INTERVAL 5 MINUTE, 300, 1),
(7, 12000000, 14500000, 3, 'ended', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY) + INTERVAL 10 MINUTE, 600, 1);

-- ================================================
-- 4. BIDS (Lịch sử đặt giá)
-- ================================================

-- Bids cho auction 1 (iPhone 15 Pro Max)
INSERT INTO bids (auction_id, user_id, amount, bid_time) VALUES
(1, 2, 20000000, DATE_SUB(NOW(), INTERVAL 4 MINUTE)),
(1, 3, 21000000, DATE_SUB(NOW(), INTERVAL 3 MINUTE)),
(1, 2, 22000000, DATE_SUB(NOW(), INTERVAL 2 MINUTE)),
(1, 4, 23000000, DATE_SUB(NOW(), INTERVAL 1 MINUTE)),
(1, 2, 25000000, DATE_SUB(NOW(), INTERVAL 30 SECOND));

-- Bids cho auction 2 (MacBook Pro)
INSERT INTO bids (auction_id, user_id, amount, bid_time) VALUES
(2, 3, 30000000, DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
(2, 4, 31000000, DATE_SUB(NOW(), INTERVAL 3 MINUTE)),
(2, 3, 32000000, DATE_SUB(NOW(), INTERVAL 1 MINUTE));

-- Bids cho auction 3 (Sony Headphone)
INSERT INTO bids (auction_id, user_id, amount, bid_time) VALUES
(3, 5, 5000000, DATE_SUB(NOW(), INTERVAL 2 MINUTE)),
(3, 2, 5500000, DATE_SUB(NOW(), INTERVAL 1 MINUTE));

-- Bids cho ended auctions
INSERT INTO bids (auction_id, user_id, amount, bid_time) VALUES
(6, 2, 18000000, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(6, 3, 20000000, DATE_SUB(NOW(), INTERVAL 1 DAY) + INTERVAL 2 MINUTE),
(6, 2, 22000000, DATE_SUB(NOW(), INTERVAL 1 DAY) + INTERVAL 4 MINUTE),
(7, 3, 12000000, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(7, 4, 13000000, DATE_SUB(NOW(), INTERVAL 2 DAY) + INTERVAL 3 MINUTE),
(7, 3, 14500000, DATE_SUB(NOW(), INTERVAL 2 DAY) + INTERVAL 8 MINUTE);

-- ================================================
-- 5. LOGS (Sample logs)
-- ================================================

INSERT INTO logs (user_id, action, details, ip_address) VALUES
(1, 'LOGIN', 'Admin logged in', '127.0.0.1'),
(2, 'LOGIN', 'Alice logged in', '127.0.0.1'),
(3, 'LOGIN', 'Bob logged in', '127.0.0.1'),
(1, 'CREATE_AUCTION', 'Created auction for iPhone 15 Pro Max', '127.0.0.1'),
(2, 'BID_PLACED', 'Placed bid 25000000 on auction 1', '127.0.0.1');

-- ================================================
-- 6. UPDATE WINNER_ID cho active auctions
-- ================================================

UPDATE auctions SET winner_id = 2 WHERE id = 1;
UPDATE auctions SET winner_id = 3 WHERE id = 2;
UPDATE auctions SET winner_id = 2 WHERE id = 3;

-- ================================================
-- VERIFICATION QUERIES
-- ================================================

SELECT '========================================' AS '';
SELECT 'DATA INSERTED SUCCESSFULLY!' AS message;
SELECT '========================================' AS '';

SELECT 'Users:' AS '', COUNT(*) AS count FROM users;
SELECT 'Products:' AS '', COUNT(*) AS count FROM products;
SELECT 'Auctions:' AS '', COUNT(*) AS count FROM auctions;
SELECT 'Bids:' AS '', COUNT(*) AS count FROM bids;
SELECT 'Logs:' AS '', COUNT(*) AS count FROM logs;

SELECT '========================================' AS '';
SELECT '* Username: alice, bob, charlie, admin' AS 'Test Accounts';
SELECT '* Password: admin123 (cho tất cả)' AS '';
SELECT '========================================' AS '';

