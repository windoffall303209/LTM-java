# Database Module

Database scripts cho hệ thống đấu giá trực tuyến.

## Cấu trúc Database

### Tables

1. **users** - Thông tin người dùng
   - id, username, password_hash, email, role, is_banned
   
2. **products** - Sản phẩm đấu giá
   - id, name, description, image_url, category
   
3. **auctions** - Phiên đấu giá
   - id, product_id, start_price, current_price, winner_id, status, start_time, end_time
   
4. **bids** - Lịch sử đặt giá
   - id, auction_id, user_id, amount, is_auto_bid, bid_time
   
5. **logs** - Nhật ký hoạt động
   - id, user_id, action, details, ip_address, created_at

## Cài đặt

### Bước 1: Cài đặt MySQL

Download và cài đặt MySQL 8.0+:
- Windows: https://dev.mysql.com/downloads/installer/
- Mac: `brew install mysql`
- Linux: `sudo apt install mysql-server`

### Bước 2: Tạo Database

```bash
# Chạy MySQL command line
mysql -u root -p

# Hoặc dùng file SQL
mysql -u root -p < schema.sql
mysql -u root -p < sample_data.sql
```

### Bước 3: Verify

```sql
USE auction_db;
SHOW TABLES;
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM auctions;
```

## Dữ liệu mẫu

Sau khi chạy `sample_data.sql`, bạn có:

- **6 users** (admin, alice, bob, charlie, david, eve)
- **8 products** (iPhone, MacBook, Sony Headphone, ...)
- **7 auctions** (3 active, 2 pending, 2 ended)
- **15+ bids**

### Tài khoản test

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | admin |
| alice | admin123 | user |
| bob | admin123 | user |
| charlie | admin123 | user |

⚠️ **Lưu ý:** Password đã được hash bằng BCrypt trong database.

## Queries hữu ích

Xem file `queries.sql` để biết các câu lệnh SQL hữu ích:
- Xem auctions đang diễn ra
- Lịch sử bids
- Top bidders
- Thống kê revenue
- ...

## Troubleshooting

### Lỗi: Access denied

```bash
# Reset password MySQL
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'your_password';
FLUSH PRIVILEGES;
```

### Lỗi: Database already exists

```sql
DROP DATABASE IF EXISTS auction_db;
-- Sau đó chạy lại schema.sql
```

### Lỗi: Foreign key constraint fails

Đảm bảo chạy theo thứ tự:
1. schema.sql (tạo tables)
2. sample_data.sql (insert data)

