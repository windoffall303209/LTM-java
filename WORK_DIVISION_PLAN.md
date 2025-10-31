# 📋 KẾ HOẠCH PHÂN CHIA CÔNG VIỆC THEO MODULE - 3 THÀNH VIÊN

## 👥 Phân Công Nhóm (Theo Module)

### **Thành Viên 1: Config & Admin Frontend**
**Trách nhiệm:** 
- Backend: Tất cả file config (Security, WebSocket, WebConfig, DataInitializer)
- Frontend: 3 màn hình admin (admin/dashboard, admin/auctions, admin/users)

### **Thành Viên 2: Backend Modules & Basic Frontend**  
**Trách nhiệm:** 
- Backend: Các module (model, repository, service, controller theo module)
- Frontend: 4 màn hình cơ bản (index, login, register, dashboard)

### **Thành Viên 3: Backend Modules & User Frontend**
**Trách nhiệm:** 
- Backend: Các module còn lại (model, repository, service, controller theo module)
- Frontend: 3 màn hình user (watchlist, my-bids, auction-detail)

---

## 📅 TIMELINE & COMMITS (Module-Based)

### **GIAI ĐOẠN 1: Khởi tạo dự án** (1 commit chung)

#### Commit 1: [Tất cả cùng làm] - Khởi tạo sườn project
**Thời gian:** Ngày 1  
**Nội dung:**
```
- Tạo cấu trúc thư mục đầy đủ:
  * source/client/public/ (css/, js/, admin/)
  * source/server/src/main/java/com/auction/ (model/, repository/, service/, controller/, config/, dto/, websocket/)
  * source/server/src/main/resources/
- Tạo README.md với:
  * Thông tin nhóm (3 thành viên)
  * Mô tả dự án Auction System
  * Công nghệ: Spring Boot, MySQL, HTML/CSS/JS
  * Cấu trúc thư mục
- Tạo .gitignore (Java + Node.js)
- Tạo source/server/pom.xml với tất cả dependencies
- Tạo source/client/package.json
- Tạo INSTRUCTION (1).md (copy từ đề bài)
```
**Commit message:** `chore: Initialize project skeleton with folder structure and documentation`

---

### **GIAI ĐOẠN 2: Backend Config Layer** (3-4 commits - Thành viên 1)

#### Commit 2: [Thành viên 1] - Application main và base entities
**Thời gian:** Ngày 2  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/AuctionSystemApplication.java
- Tạo source/server/src/main/resources/application.properties (cấu hình MySQL, port 8000)
- Tạo 4 entity classes cơ bản trong model/:
  * User.java (chỉ fields + annotations, chưa có methods)
  * Auction.java
  * Bid.java
  * Watchlist.java
```
**Commit message:** `feat(config): Add main application class and base entities`

#### Commit 3: [Thành viên 1] - Security Configuration
**Thời gian:** Ngày 2-3  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/config/:
  * SecurityConfig.java (đầy đủ BCrypt, CORS, authentication)
  * CustomUserDetailsService.java (trong service/)
- Update pom.xml nếu thiếu Spring Security dependency
```
**Commit message:** `feat(config): Implement Spring Security with BCrypt password encoding`

#### Commit 4: [Thành viên 1] - WebSocket & Web Configuration
**Thời gian:** Ngày 3  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/config/:
  * WebSocketConfig.java (STOMP configuration)
  * WebConfig.java (CORS configuration cho frontend)
```
**Commit message:** `feat(config): Configure WebSocket (STOMP) and CORS settings`

#### Commit 5: [Thành viên 1] - Data Initializer
**Thời gian:** Ngày 4  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/config/:
  * DataInitializer.java
    - Tạo admin account (admin/admin123)
    - Tạo 2 demo users (user1, user2)
    - Khởi tạo số dư 2 tỷ cho mỗi user
```
**Commit message:** `feat(config): Add data initializer with default accounts`

---

### **GIAI ĐOẠN 3: Backend Module - User & Auth** (3-4 commits - Thành viên 2)

#### Commit 6: [Thành viên 2] - User Module (Repository + Service)
**Thời gian:** Ngày 3-4  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/repository/:
  * UserRepository.java
- Tạo source/server/src/main/java/com/auction/service/:
  * UserService.java (CRUD user, update balance)
- Tạo source/server/src/main/java/com/auction/dto/:
  * UserDTO.java
  * LoginRequest.java
  * RegisterRequest.java
```
**Commit message:** `feat(user): Implement User repository, service and DTOs`

#### Commit 7: [Thành viên 2] - Auth Controller
**Thời gian:** Ngày 4-5  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/controller/:
  * AuthController.java
    - POST /api/auth/register
    - POST /api/auth/login (handled by Security)
    - GET /api/auth/profile
- Tạo source/server/src/main/java/com/auction/dto/:
  * ApiResponse.java
```
**Commit message:** `feat(auth): Add authentication endpoints (register, login, profile)`

#### Commit 8: [Thành viên 2] - User Controller & Admin User Management
**Thời gian:** Ngày 5  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/controller/:
  * UserController.java
    - GET /api/users/{id}
- Update AdminController với user management:
  * GET /api/admin/users
  * POST /api/admin/users/{id}/toggle-status
  * POST /api/admin/users/{id}/update-balance
```
**Commit message:** `feat(user): Add user controller and admin user management APIs`

---

### **GIAI ĐOẠN 4: Backend Module - Auction** (3-4 commits - Thành viên 3)

#### Commit 9: [Thành viên 3] - Auction Module (Repository + Service)
**Thời gian:** Ngày 4-5  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/repository/:
  * AuctionRepository.java (với custom queries)
- Tạo source/server/src/main/java/com/auction/service/:
  * AuctionService.java (CRUD auctions, start/end logic)
  * AuctionSchedulerService.java (auto start/end với @Scheduled)
- Tạo source/server/src/main/java/com/auction/dto/:
  * AuctionDTO.java
```
**Commit message:** `feat(auction): Implement Auction repository, service and scheduler`

#### Commit 10: [Thành viên 3] - Auction Controller
**Thời gian:** Ngày 5-6  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/controller/:
  * AuctionController.java
    - GET /api/auctions (list ACTIVE + PENDING)
    - GET /api/auctions/{id}
    - GET /api/auctions/search
```
**Commit message:** `feat(auction): Add auction listing and detail endpoints`

#### Commit 11: [Thành viên 3] - Admin Auction Management
**Thời gian:** Ngày 6  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/controller/:
  * AdminController.java
    - POST /api/admin/auctions (create)
    - PUT /api/admin/auctions/{id} (update)
    - DELETE /api/admin/auctions/{id}
    - POST /api/admin/auctions/{id}/start
    - POST /api/admin/auctions/{id}/end
    - GET /api/admin/statistics
    - GET /api/admin/auctions/all
```
**Commit message:** `feat(auction): Implement admin auction management (CRUD, start, end)`

---

### **GIAI ĐOẠN 5: Backend Module - Bidding** (2-3 commits - Thành viên 2)

#### Commit 12: [Thành viên 2] - Bid Module
**Thời gian:** Ngày 6-7  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/repository/:
  * BidRepository.java
- Tạo source/server/src/main/java/com/auction/service/:
  * BidService.java (place bid với validation, extension logic)
- Tạo source/server/src/main/java/com/auction/dto/:
  * BidDTO.java
  * BidRequest.java
```
**Commit message:** `feat(bid): Implement bidding system with validation and extension`

#### Commit 13: [Thành viên 2] - Bid Controller & WebSocket
**Thời gian:** Ngày 7  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/controller/:
  * BidController.java
    - POST /api/bids
    - GET /api/bids/auction/{id}
    - GET /api/bids/user
- Tạo source/server/src/main/java/com/auction/websocket/:
  * WebSocketController.java
- Update BidService để broadcast bid updates qua WebSocket
```
**Commit message:** `feat(bid): Add bid controller and WebSocket real-time updates`

---

### **GIAI ĐOẠN 6: Backend Module - Watchlist** (2 commits - Thành viên 3)

#### Commit 14: [Thành viên 3] - Watchlist Module
**Thời gian:** Ngày 7-8  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/repository/:
  * WatchlistRepository.java
- Tạo source/server/src/main/java/com/auction/service/:
  * WatchlistService.java
- Tạo source/server/src/main/java/com/auction/dto/:
  * WatchlistDTO.java
```
**Commit message:** `feat(watchlist): Implement watchlist repository and service`

#### Commit 15: [Thành viên 3] - Watchlist Controller
**Thời gian:** Ngày 8  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/controller/:
  * WatchlistController.java
    - POST /api/watchlist
    - GET /api/watchlist/user
    - DELETE /api/watchlist/{id}
    - DELETE /api/watchlist/auction/{auctionId}
    - GET /api/watchlist/check
```
**Commit message:** `feat(watchlist): Add watchlist controller with CRUD operations`

---

### **GIAI ĐOẠN 7: Frontend - Basic Pages** (4-5 commits - Thành viên 2)

#### Commit 16: [Thành viên 2] - Frontend base setup
**Thời gian:** Ngày 8  
**Nội dung:**
```
- Tạo source/client/public/css/:
  * style.css (tất cả styles cho toàn bộ app)
- Tạo source/client/public/js/:
  * config.js (API_BASE_URL = 'http://localhost:8000')
  * auth.js (login/logout/session helpers)
```
**Commit message:** `feat(frontend): Add base CSS and authentication utilities`

#### Commit 17: [Thành viên 2] - Index page
**Thời gian:** Ngày 8-9  
**Nội dung:**
```
- Tạo source/client/public/index.html
  * Landing page với giới thiệu hệ thống
  * Links đến login/register
  * Navbar cơ bản
```
**Commit message:** `feat(frontend): Create index landing page`

#### Commit 18: [Thành viên 2] - Login & Register pages
**Thời gian:** Ngày 9  
**Nội dung:**
```
- Tạo source/client/public/login.html
  * Form đăng nhập
  * Call API POST /api/auth/login (thông qua Spring Security)
- Tạo source/client/public/register.html
  * Form đăng ký
  * Call API POST /api/auth/register
- Update auth.js với login/register functions
```
**Commit message:** `feat(frontend): Add login and register pages with API integration`

#### Commit 19: [Thành viên 2] - User Dashboard
**Thời gian:** Ngày 9-10  
**Nội dung:**
```
- Tạo source/client/public/dashboard.html
  * Hiển thị ACTIVE + PENDING auctions
  * Auction cards với thông tin
  * WebSocket connection để listen updates
- Tạo source/client/public/js/:
  * header.js (user menu, balance, logout)
  * dashboard.js (fetch auctions, display, WebSocket)
```
**Commit message:** `feat(frontend): Implement user dashboard with auction listings`

---

### **GIAI ĐOẠN 8: Frontend - User Pages** (3-4 commits - Thành viên 3)

#### Commit 20: [Thành viên 3] - Auction Detail page
**Thời gian:** Ngày 10  
**Nội dung:**
```
- Tạo source/client/public/auction-detail.html
  * Hiển thị chi tiết auction
  * Form đặt giá (nếu ACTIVE)
  * Watchlist toggle button
  * Real-time updates qua WebSocket
- Tạo source/client/public/js/:
  * auction.js (fetch detail, place bid, watchlist, WebSocket)
```
**Commit message:** `feat(frontend): Create auction detail page with real-time bidding`

#### Commit 21: [Thành viên 3] - My Bids page
**Thời gian:** Ngày 10-11  
**Nội dung:**
```
- Tạo source/client/public/my-bids.html
  * Hiển thị lịch sử đặt giá của user
  * Filter theo status (ACTIVE, PENDING, ENDED)
  * Link đến auction detail
- Update dashboard.js hoặc tạo my-bids.js
```
**Commit message:** `feat(frontend): Add My Bids page for bid history`

#### Commit 22: [Thành viên 3] - Watchlist page
**Thời gian:** Ngày 11  
**Nội dung:**
```
- Tạo source/client/public/watchlist.html
  * Hiển thị danh sách auctions đã follow
  * Nút remove from watchlist
  * Link đến auction detail
- Tạo source/client/public/js/:
  * watchlist.js hoặc update existing
```
**Commit message:** `feat(frontend): Implement watchlist page for tracked auctions`

---

### **GIAI ĐOẠN 9: Frontend - Admin Pages** (3-4 commits - Thành viên 1)

#### Commit 23: [Thành viên 1] - Admin Dashboard
**Thời gian:** Ngày 11-12  
**Nội dung:**
```
- Tạo source/client/public/admin/dashboard.html
  * Statistics cards (total users, auctions, bids)
  * Charts (nếu có thời gian)
  * Quick links
- Tạo source/client/public/js/:
  * admin-header.js (admin navigation)
  * admin-dashboard.js (fetch statistics)
```
**Commit message:** `feat(admin): Create admin dashboard with system statistics`

#### Commit 24: [Thành viên 1] - Admin Auction Management
**Thời gian:** Ngày 12  
**Nội dung:**
```
- Tạo source/client/public/admin/auctions.html
  * List tất cả auctions (bao gồm ENDED)
  * Create auction form (modal)
  * Edit auction (modal)
  * Delete confirmation
  * Start/End buttons
- Tạo admin-auctions.js hoặc update dashboard.js
```
**Commit message:** `feat(admin): Implement auction management (CRUD, start, end)`

#### Commit 25: [Thành viên 1] - Admin User Management
**Thời gian:** Ngày 12-13  
**Nội dung:**
```
- Tạo source/client/public/admin/users.html
  * List tất cả users
  * Ban/Unban button
  * Update balance form
  * User statistics
- Update admin-header.js nếu cần
```
**Commit message:** `feat(admin): Add user management page (ban, unban, update balance)`

---

### **GIAI ĐOẠN 10: Polish & Testing** (2-3 commits)

#### Commit 26: [Thành viên 2] - Backend testing & fixes
**Thời gian:** Ngày 13  
**Nội dung:**
```
- Test tất cả APIs
- Fix bugs backend
- Optimize queries
- Add error handling
```
**Commit message:** `fix(backend): Bug fixes and optimization`

#### Commit 27: [Thành viên 3] - Frontend polish & fixes
**Thời gian:** Ngày 13-14  
**Nội dung:**
```
- Test UI flows
- Fix responsive design
- Improve error messages
- Add loading states
```
**Commit message:** `fix(frontend): UI improvements and bug fixes`

#### Commit 28: [Thành viên 1 hoặc tất cả] - Documentation & Screenshots
**Thời gian:** Ngày 14  
**Nội dung:**
```
- Update README.md đầy đủ:
  * Thông tin nhóm chi tiết
  * Hướng dẫn cài đặt MySQL
  * Hướng dẫn chạy server (cd source/server && mvn spring-boot:run)
  * Hướng dẫn chạy client (cd source/client && npm start)
  * API documentation
  * Tài khoản mặc định
- Tạo thư mục statics/ với screenshots
- Tạo source/server/README.md
- Tạo source/client/README.md
```
**Commit message:** `docs: Complete documentation with screenshots and setup guide`

---

## 📊 TỔNG KẾT PHÂN CÔNG

### Thành viên 1 (Config + Admin Frontend): ~30% công việc
- **Commits:** 1 (chung), 2, 3, 4, 5, 23, 24, 25, 28
- **Tổng:** 8 commits riêng + 1 chung
- **Backend:** Tất cả config files (Security, WebSocket, WebConfig, DataInitializer)
- **Frontend:** 3 màn hình admin (dashboard, auctions, users)

### Thành viên 2 (User/Auth Module + Basic Frontend): ~40% công việc  
- **Commits:** 1 (chung), 6, 7, 8, 12, 13, 16, 17, 18, 19, 26
- **Tổng:** 10 commits riêng + 1 chung
- **Backend:** User module (repository, service, controller), Auth, Bid module
- **Frontend:** 4 màn hình (index, login, register, dashboard) + base setup

### Thành viên 3 (Auction/Watchlist Module + User Frontend): ~30% công việc
- **Commits:** 1 (chung), 9, 10, 11, 14, 15, 20, 21, 22, 27
- **Tổng:** 9 commits riêng + 1 chung
- **Backend:** Auction module (repository, service, controller, scheduler), Watchlist module
- **Frontend:** 3 màn hình (auction-detail, my-bids, watchlist)

---

## 💡 LƯU Ý QUAN TRỌNG

### 1. **Commit đầu tiên (Skeleton):**
- **Rất quan trọng:** Commit 1 phải tạo toàn bộ cấu trúc thư mục
- Tất cả 3 người cùng làm hoặc 1 người làm rồi các người khác review
- Đây là nền tảng để các commit sau không bị conflict

### 2. **Thứ tự commit:**
- Backend config TRƯỚC (commits 2-5)
- Backend modules song song (commits 6-15)
- Frontend sau khi backend modules xong (commits 16-25)
- Polish cuối cùng (commits 26-28)

### 3. **Module-based approach:**
- Mỗi module đầy đủ: model → repository → service → controller
- Không nên tách quá nhỏ (VD: chỉ làm model rồi commit)
- Mỗi commit nên có 1 module hoàn chỉnh hoặc 1 trang frontend hoàn chỉnh

### 4. **Frontend screens phân chia:**
```
Thành viên 2 (4 màn):
- index.html (landing)
- login.html
- register.html
- dashboard.html (user)

Thành viên 3 (3 màn):
- auction-detail.html
- my-bids.html
- watchlist.html

Thành viên 1 (3 màn admin):
- admin/dashboard.html
- admin/auctions.html
- admin/users.html
```

### 5. **Git commands:**
```bash
# Commit 1 - Skeleton (làm chung hoặc 1 người)
git add .
git commit -m "chore: Initialize project skeleton with folder structure and documentation"
git push origin main

# Các commits sau
git add source/server/src/main/java/com/auction/config/SecurityConfig.java
git add source/server/src/main/java/com/auction/service/CustomUserDetailsService.java
git commit -m "feat(config): Implement Spring Security with BCrypt password encoding"
git push origin main
```

### 6. **Testing:**
- Test API bằng Postman sau mỗi backend commit
- Test UI trong browser sau mỗi frontend commit
- Đảm bảo không conflict với code của người khác

---

## ✅ CHECKLIST TRƯỚC KHI NỘP

- [ ] Có đủ ~28 commits
- [ ] Thành viên 1: 8-9 commits (config + admin frontend)
- [ ] Thành viên 2: 10-11 commits (user/bid modules + basic frontend)
- [ ] Thành viên 3: 9-10 commits (auction/watchlist modules + user frontend)
- [ ] README.md đầy đủ theo INSTRUCTION.md
- [ ] Screenshots trong statics/
- [ ] Backend chạy được: `cd source/server && mvn spring-boot:run`
- [ ] Frontend chạy được: `cd source/client && npm start`
- [ ] Tuân thủ cấu trúc thư mục: source/client, source/server

---

## 📞 HỖ TRỢ

Nếu cần chi tiết hơn về bất kỳ commit nào (VD: code mẫu cho SecurityConfig.java), hãy hỏi tôi!

**Good luck! 🎉**
