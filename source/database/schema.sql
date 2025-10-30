-- ================================================
-- HỆ THỐNG ĐẤU GIÁ TRỰC TUYẾN - DATABASE SCHEMA
-- ================================================

-- Tạo database
CREATE DATABASE IF NOT EXISTS auction_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE auction_db;

-- Xóa tables cũ nếu có (để re-run script)
DROP TABLE IF EXISTS logs;
DROP TABLE IF EXISTS bids;
DROP TABLE IF EXISTS auctions;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- ================================================
-- TABLE: users
-- ================================================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role ENUM('user', 'admin') DEFAULT 'user',
    is_banned BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================
-- TABLE: products
-- ================================================
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    image_url VARCHAR(500),
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================
-- TABLE: auctions
-- ================================================
CREATE TABLE auctions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    start_price BIGINT NOT NULL,
    current_price BIGINT NOT NULL,
    reserve_price BIGINT DEFAULT 0,
    winner_id INT NULL,
    status ENUM('pending', 'active', 'ended', 'cancelled') DEFAULT 'pending',
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP NOT NULL,
    duration INT NOT NULL COMMENT 'Duration in seconds',
    extension_count INT DEFAULT 0 COMMENT 'Number of time extensions',
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (winner_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_status (status),
    INDEX idx_end_time (end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================
-- TABLE: bids
-- ================================================
CREATE TABLE bids (
    id INT AUTO_INCREMENT PRIMARY KEY,
    auction_id INT NOT NULL,
    user_id INT NOT NULL,
    amount BIGINT NOT NULL,
    is_auto_bid BOOLEAN DEFAULT FALSE,
    max_auto_bid BIGINT DEFAULT 0,
    bid_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (auction_id) REFERENCES auctions(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_auction_id (auction_id),
    INDEX idx_user_id (user_id),
    INDEX idx_bid_time (bid_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================
-- TABLE: logs
-- ================================================
CREATE TABLE logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    action VARCHAR(100) NOT NULL,
    details TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================
-- VIEWS (Optional - để query dễ hơn)
-- ================================================

-- View: Active auctions with product info
CREATE OR REPLACE VIEW active_auctions AS
SELECT 
    a.id AS auction_id,
    a.current_price,
    a.status,
    a.end_time,
    p.name AS product_name,
    p.description AS product_description,
    p.image_url,
    u.username AS current_winner,
    TIMESTAMPDIFF(SECOND, NOW(), a.end_time) AS time_left_seconds
FROM auctions a
JOIN products p ON a.product_id = p.id
LEFT JOIN users u ON a.winner_id = u.id
WHERE a.status = 'active';

-- View: Bid history with user info
CREATE OR REPLACE VIEW bid_history AS
SELECT 
    b.id AS bid_id,
    b.auction_id,
    b.amount,
    b.is_auto_bid,
    b.bid_time,
    u.username,
    p.name AS product_name
FROM bids b
JOIN users u ON b.user_id = u.id
JOIN auctions a ON b.auction_id = a.id
JOIN products p ON a.product_id = p.id
ORDER BY b.bid_time DESC;

-- ================================================
-- TRIGGERS (Auto-update timestamps, etc.)
-- ================================================

DELIMITER //

-- Trigger: Update last_login when user logs in
CREATE TRIGGER update_last_login
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    IF NEW.last_login IS NOT NULL AND NEW.last_login != OLD.last_login THEN
        INSERT INTO logs (user_id, action, details) 
        VALUES (NEW.id, 'LOGIN', CONCAT('User ', NEW.username, ' logged in'));
    END IF;
END//

-- Trigger: Log khi có bid mới
CREATE TRIGGER log_new_bid
AFTER INSERT ON bids
FOR EACH ROW
BEGIN
    INSERT INTO logs (user_id, action, details)
    VALUES (NEW.user_id, 'BID_PLACED', 
            CONCAT('Bid ', NEW.amount, ' on auction ', NEW.auction_id));
END//

DELIMITER ;

-- ================================================
-- INDEXES cho performance
-- ================================================

-- Composite indexes
CREATE INDEX idx_auction_status_end_time ON auctions(status, end_time);
CREATE INDEX idx_bid_auction_time ON bids(auction_id, bid_time);

-- ================================================
-- COMPLETED
-- ================================================
SELECT 'Database schema created successfully!' AS message;

