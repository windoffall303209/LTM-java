# 📖 HƯỚNG DẪN SỬ DỤNG 3 FOLDERS

## 🎯 Mục đích

3 folders đã được chia sẵn code theo nhiệm vụ của từng người:
- **basic-project-admin** → Người 1 (Khởi hoặc Nam)
- **basic-project-user1** → Người 2 (Tâm)
- **basic-project-user2** → Người 3 (Người còn lại)

Mỗi folder có thể **chạy độc lập** để test, sau đó **merge lại** dễ dàng.

---

## 📁 Cấu trúc mỗi folder

```
basic-project-xxx/
├── source/
│   ├── server/          # Backend Spring Boot
│   │   ├── pom.xml     # Maven dependencies
│   │   └── src/main/java/com/auction/
│   │       ├── model/          # Entities (User, Auction, Bid, Watchlist)
│   │       ├── dto/            # Data Transfer Objects
│   │       ├── repository/     # JPA Repositories
│   │       ├── service/        # Business logic
│   │       ├── controller/     # REST API endpoints
│   │       ├── config/         # Configuration
│   │       └── websocket/      # WebSocket (chỉ User1)
│   │
│   └── client/          # Frontend
│       ├── package.json
│       └── public/
│           ├── *.html          # HTML pages
│           ├── js/             # JavaScript
│           └── css/            # CSS
```

---

## 🚀 HƯỚNG DẪN CHẠY TỪNG FOLDER

### Bước 1: Chuẩn bị Database

```sql
-- Tạo database
CREATE DATABASE auction_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Kiểm tra
SHOW DATABASES;
```

### Bước 2: Chạy Backend (Server)

Mỗi người làm việc trên folder của mình:

```bash
# Ví dụ: Người 1 (Admin)
cd "basic-project -admin/source/server"

# Build project
mvn clean install

# Chạy server
mvn spring-boot:run
```

Server sẽ chạy tại: `http://localhost:8000`

**Lưu ý:** Database config trong `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auction_db
spring.datasource.username=root
spring.datasource.password=admin
```

### Bước 3: Chạy Frontend (Client)

Mở terminal mới:

```bash
# Ví dụ: Người 1 (Admin)
cd "basic-project -admin/source/client"

# Cài dependencies (chỉ lần đầu)
npm install

# Chạy client
npm start
```

Client sẽ chạy tại: `http://localhost:3000` hoặc `http://localhost:8080`

---

## 👥 NHIỆM VỤ TỪNG NGƯỜI

### 👤 Người 1 (basic-project-admin)

**Backend cần làm:**
- ✅ AdminAuctionService.java
- ✅ AdminUserService.java
- ✅ AdminStatisticsService.java
- ✅ AdminController.java
- ✅ SecurityConfig.java (với admin role)

**Frontend cần làm:**
- ✅ admin/dashboard.html
- ✅ admin/auctions.html
- ✅ admin/users.html
- ✅ admin-*.js (8 files)
- ✅ admin-style.css

**Test:**
1. Login as admin: `admin / admin123`
2. Xem admin dashboard
3. Tạo/sửa/xóa auction
4. Quản lý users

---

### 👤 Người 2 (basic-project-user1)

**Backend cần làm:**
- ✅ AuctionService.java
- ✅ BidService.java (CORE - Real-time bidding)
- ✅ AuctionSchedulerService.java
- ✅ AuctionController.java
- ✅ BidController.java
- ✅ WebSocketController.java

**Frontend cần làm:**
- ✅ dashboard.html (All auctions)
- ✅ auction-detail.html (Bidding panel)
- ✅ my-bids.html
- ✅ dashboard.js
- ✅ auction.js (Real-time updates)

**Test:**
1. Login as user: `user1 / password`
2. Xem dashboard với auctions
3. Vào auction detail
4. Place bid
5. Kiểm tra real-time updates (mở 2 browsers)
6. Xem countdown timer
7. Test auto-extend

---

### 👤 Người 3 (basic-project-user2)

**Backend cần làm:**
- ✅ WatchlistService.java
- ✅ UserService.java
- ✅ WatchlistController.java
- ✅ UserController.java
- ✅ CustomUserDetailsService.java

**Frontend cần làm:**
- ✅ watchlist.html
- ✅ index.html (Landing page)
- ✅ watchlist.js
- ✅ main.js
- ✅ admin-header.js

**Test:**
1. Login as user: `user2 / password`
2. Add auction to watchlist
3. Xem watchlist page
4. Remove from watchlist
5. Xem landing page
6. Test user profile update

---

## 🔄 MERGE 3 FOLDERS

Sau khi cả 3 người test xong, merge lại:

### Option 1: Tự động (dùng script)

```bash
chmod +x merge-all-folders.sh
./merge-all-folders.sh
```

### Option 2: Manual

Xem chi tiết tại [CODE_SUMMARY.md](CODE_SUMMARY.md) section "MERGE STRATEGY"

---

## 📝 COMMIT WORKFLOW

### Người 3 commit trước (Foundation)

```bash
cd "basic-project-user2"
git add .
git commit -m "feat(user): add watchlist functionality"
git commit -m "feat(user): add user service"
git commit -m "feat(auth): add Spring Security user details"
git push origin feature/user-support
```

### Người 2 commit sau (Core)

```bash
cd "basic-project-user1"
git add .
git commit -m "feat(user): add auction service with search"
git commit -m "feat(user): add bid service with WebSocket"
git commit -m "feat(user): add real-time bidding"
git commit -m "feat(scheduler): add auction auto start/end"
git push origin feature/user-core
```

### Người 1 commit cuối (Admin)

```bash
cd "basic-project-admin"
git add .
git commit -m "feat(admin): add admin services"
git commit -m "feat(admin): add admin dashboard"
git commit -m "feat(admin): add auction CRUD"
git commit -m "feat(admin): add user management"
git push origin feature/admin
```

---

## ⚠️ TROUBLESHOOTING

### Lỗi: Cannot connect to database

```bash
# Kiểm tra MySQL đang chạy
mysql -u root -p

# Tạo lại database
DROP DATABASE IF EXISTS auction_db;
CREATE DATABASE auction_db CHARACTER SET utf8mb4;
```

### Lỗi: Port 8000 already in use

```bash
# Tìm process đang dùng port 8000
netstat -ano | findstr :8000

# Kill process (Windows)
taskkill /PID <PID> /F

# Hoặc đổi port trong application.properties
server.port=8001
```

### Lỗi: Maven build failed

```bash
# Clean và rebuild
mvn clean
mvn install -U

# Nếu vẫn lỗi, xóa .m2 cache
rm -rf ~/.m2/repository
```

### Lỗi: npm install failed

```bash
# Xóa node_modules và package-lock.json
rm -rf node_modules package-lock.json

# Install lại
npm install
```

---

## 📚 TÀI LIỆU THAM KHẢO

- [CODE_SUMMARY.md](CODE_SUMMARY.md) - Chi tiết files trong mỗi folder
- [Chia_viec_.md](Chia_viec_.md) - Kế hoạch chia việc chi tiết
- [README.md](README.md) - Documentation chính của project

---

## ✅ CHECKLIST

### Trước khi commit:

- [ ] Code chạy được không lỗi
- [ ] Đã test các chức năng chính
- [ ] Đã format code đẹp
- [ ] Đã viết commit message rõ ràng
- [ ] Đã push lên đúng branch

### Trước khi merge:

- [ ] Cả 3 người đã test xong
- [ ] Không có conflicts lớn
- [ ] Đã backup code
- [ ] Đã thảo luận merge strategy

---

## 💡 TIPS

1. **Làm việc song song:** Mỗi người làm trên folder riêng, không ảnh hưởng lẫn nhau
2. **Test thường xuyên:** Chạy code sau mỗi thay đổi lớn
3. **Commit nhỏ:** Commit nhiều lần, mỗi lần 1 feature nhỏ
4. **Communication:** Trao đổi khi gặp vấn đề
5. **Code review:** Review code của nhau trước khi merge

---

**🚀 Chúc các bạn làm việc hiệu quả!**

Nếu có vấn đề gì, hãy check CODE_SUMMARY.md hoặc hỏi trong group.
