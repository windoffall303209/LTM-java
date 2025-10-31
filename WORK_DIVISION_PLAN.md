# 📋 KẾ HOẠCH PHÂN CHIA CÔNG VIỆC - 3 THÀNH VIÊN

## 👥 Phân Công Nhóm

### **Thành Viên 1: Backend Core Developer**
**Trách nhiệm:** Xây dựng nền tảng backend, database, và API cơ bản

### **Thành Viên 2: Backend Features Developer**  
**Trách nhiệm:** Phát triển tính năng nâng cao, WebSocket, và tích hợp

### **Thành Viên 3: Frontend Developer**
**Trách nhiệm:** Xây dựng giao diện người dùng và tích hợp với backend

---

## 📅 TIMELINE & COMMITS

### **GIAI ĐOẠN 1: Khởi tạo dự án & Cấu trúc cơ bản** (3-4 commits)

#### Commit 1: [Thành viên 1] - Khởi tạo cấu trúc dự án
**Thời gian:** Ngày 1  
**Nội dung:**
```
- Tạo cấu trúc thư mục: source/client, source/server
- Thêm README.md chính với thông tin nhóm
- Tạo .gitignore cho Java và Node.js
- Thêm source/server/pom.xml với dependencies cơ bản:
  * Spring Boot Starter Web
  * Spring Boot Starter Data JPA
  * MySQL Connector
  * Lombok
```
**Commit message:** `feat: Initialize project structure with client-server architecture`

#### Commit 2: [Thành viên 1] - Setup entities và database
**Thời gian:** Ngày 1-2  
**Nội dung:**
```
- Tạo các entity classes trong source/server/src/main/java/com/auction/model/:
  * User.java
  * Auction.java
  * Bid.java
  * Watchlist.java
- Tạo application.properties với cấu hình MySQL
- Tạo AuctionSystemApplication.java (main class)
```
**Commit message:** `feat: Add database entities (User, Auction, Bid, Watchlist)`

#### Commit 3: [Thành viên 2] - Setup Spring Security và WebSocket config
**Thời gian:** Ngày 2  
**Nội dung:**
```
- Thêm dependencies: Spring Security, WebSocket vào pom.xml
- Tạo source/server/src/main/java/com/auction/config/:
  * SecurityConfig.java (cấu hình authentication)
  * WebSocketConfig.java (cấu hình STOMP)
  * WebConfig.java (CORS configuration)
```
**Commit message:** `feat: Configure Spring Security and WebSocket support`

#### Commit 4: [Thành viên 3] - Initialize frontend structure
**Thời gian:** Ngày 2  
**Nội dung:**
```
- Tạo source/client/package.json
- Tạo cấu trúc thư mục: public/, public/css/, public/js/, public/admin/
- Thêm source/client/public/index.html (landing page)
- Thêm source/client/public/css/style.css (basic styles)
- Thêm source/client/public/js/config.js (API endpoint config)
- Tạo source/client/README.md với hướng dẫn chạy
```
**Commit message:** `feat: Initialize frontend structure with HTML, CSS, and config`

---

### **GIAI ĐOẠN 2: Backend Core APIs** (4-5 commits)

#### Commit 5: [Thành viên 1] - Implement repositories
**Thời gian:** Ngày 3  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/repository/:
  * AuctionRepository.java
  * BidRepository.java
  * UserRepository.java
  * WatchlistRepository.java
- Thêm custom query methods
```
**Commit message:** `feat: Implement JPA repositories for all entities`

#### Commit 6: [Thành viên 1] - Create DTOs and basic services
**Thời gian:** Ngày 3-4  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/dto/:
  * AuctionDTO.java
  * BidDTO.java
  * UserDTO.java
  * ApiResponse.java
- Tạo UserService.java với basic CRUD
- Tạo AuctionService.java với basic CRUD
```
**Commit message:** `feat: Add DTOs and implement basic User/Auction services`

#### Commit 7: [Thành viên 1] - Authentication & User Management
**Thời gian:** Ngày 4  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/dto/:
  * LoginRequest.java
  * RegisterRequest.java
- Tạo source/server/src/main/java/com/auction/service/:
  * CustomUserDetailsService.java
- Tạo source/server/src/main/java/com/auction/controller/:
  * AuthController.java (login, register, profile)
  * UserController.java (get user info)
```
**Commit message:** `feat: Implement authentication (login, register) and user management`

#### Commit 8: [Thành viên 2] - Auction Management APIs
**Thời gian:** Ngày 4-5  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/controller/:
  * AuctionController.java
    - GET /api/auctions (list active & pending)
    - GET /api/auctions/{id} (detail)
    - GET /api/auctions/search (search)
```
**Commit message:** `feat: Implement Auction APIs (list, detail, search)`

#### Commit 9: [Thành viên 2] - Bidding System
**Thời gian:** Ngày 5  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/dto/:
  * BidRequest.java
- Complete BidService.java với bid logic
- Tạo source/server/src/main/java/com/auction/controller/:
  * BidController.java
    - POST /api/bids (place bid)
    - GET /api/bids/auction/{id} (bid history)
    - GET /api/bids/user (user bids)
```
**Commit message:** `feat: Implement bidding system with validation and history`

---

### **GIAI ĐOẠN 3: Advanced Features** (4-5 commits)

#### Commit 10: [Thành viên 2] - WebSocket Real-time Updates
**Thời gian:** Ngày 5-6  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/websocket/:
  * WebSocketController.java
- Update BidService.java để broadcast bid updates
- Thêm real-time notification cho auction events
```
**Commit message:** `feat: Add WebSocket support for real-time bidding updates`

#### Commit 11: [Thành viên 2] - Auction Scheduler
**Thời gian:** Ngày 6  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/service/:
  * AuctionSchedulerService.java
    - Auto-start auctions when startTime reached
    - Auto-end auctions when endTime reached
    - Auto-end after 20 minutes of inactivity
- Update Auction entity với lastBidTime field
```
**Commit message:** `feat: Implement auction auto-scheduler (start, end, inactivity check)`

#### Commit 12: [Thành viên 1] - Watchlist Feature
**Thời gian:** Ngày 6-7  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/dto/:
  * WatchlistDTO.java
- Complete WatchlistService.java
- Tạo source/server/src/main/java/com/auction/controller/:
  * WatchlistController.java
    - POST /api/watchlist (add)
    - GET /api/watchlist/user (list)
    - DELETE /api/watchlist/{id} (remove)
```
**Commit message:** `feat: Implement watchlist feature for tracking auctions`

#### Commit 13: [Thành viên 1] - Admin Panel APIs
**Thời gian:** Ngày 7  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/controller/:
  * AdminController.java
    - POST /api/admin/auctions (create)
    - PUT /api/admin/auctions/{id} (update)
    - DELETE /api/admin/auctions/{id} (delete)
    - POST /api/admin/auctions/{id}/start (manual start)
    - POST /api/admin/auctions/{id}/end (manual end)
    - GET /api/admin/users (list users)
    - POST /api/admin/users/{id}/toggle-status (ban/unban)
    - GET /api/admin/statistics (dashboard stats)
```
**Commit message:** `feat: Add admin panel APIs for auction and user management`

#### Commit 14: [Thành viên 1] - Data Initialization
**Thời gian:** Ngày 7-8  
**Nội dung:**
```
- Tạo source/server/src/main/java/com/auction/config/:
  * DataInitializer.java
    - Tạo admin account mặc định
    - Tạo demo users
    - Tạo sample auctions (nếu cần)
- Tạo source/server/README.md với hướng dẫn chạy backend
```
**Commit message:** `feat: Add data initializer with default admin and demo users`

---

### **GIAI ĐOẠN 4: Frontend Implementation** (6-7 commits)

#### Commit 15: [Thành viên 3] - Authentication Pages
**Thời gian:** Ngày 8  
**Nội dung:**
```
- Tạo source/client/public/login.html (full design)
- Tạo source/client/public/register.html (full design)
- Tạo source/client/public/js/auth.js:
  * Login logic với API call
  * Register logic với API call
  * Session management (localStorage)
```
**Commit message:** `feat: Create login and register pages with authentication logic`

#### Commit 16: [Thành viên 3] - Dashboard & Header Components
**Thời gian:** Ngày 8-9  
**Nội dung:**
```
- Tạo source/client/public/dashboard.html
- Tạo source/client/public/js/header.js:
  * User menu
  * Balance display
  * Logout functionality
- Tạo source/client/public/js/dashboard.js:
  * Fetch active & pending auctions
  * Display auction cards
  * WebSocket connection for updates
```
**Commit message:** `feat: Implement user dashboard with auction listings`

#### Commit 17: [Thành viên 3] - Auction Detail Page
**Thời gian:** Ngày 9  
**Nội dung:**
```
- Tạo source/client/public/auction-detail.html
- Tạo source/client/public/js/auction.js:
  * Fetch auction details
  * Display current bid
  * Bid form handling
  * WebSocket for real-time updates
  * Watchlist toggle
```
**Commit message:** `feat: Create auction detail page with real-time bidding`

#### Commit 18: [Thành viên 3] - User Features (My Bids & Watchlist)
**Thời gian:** Ngày 10  
**Nội dung:**
```
- Tạo source/client/public/my-bids.html
- Tạo source/client/public/watchlist.html
- Thêm logic để:
  * Fetch và display user's bid history
  * Fetch và display watchlist
  * Remove from watchlist
```
**Commit message:** `feat: Add My Bids and Watchlist pages for users`

#### Commit 19: [Thành viên 3] - Admin Dashboard
**Thời gian:** Ngày 10-11  
**Nội dung:**
```
- Tạo source/client/public/admin/dashboard.html:
  * Statistics cards
  * System overview
- Tạo source/client/public/js/admin-header.js:
  * Admin navigation
  * Admin menu
```
**Commit message:** `feat: Create admin dashboard with statistics`

#### Commit 20: [Thành viên 3] - Admin Auction Management
**Thời gian:** Ngày 11  
**Nội dung:**
```
- Tạo source/client/public/admin/auctions.html:
  * List all auctions (including ENDED)
  * Create auction form
  * Edit auction modal
  * Delete confirmation
  * Start/End buttons
- Thêm logic trong dashboard.js hoặc tạo admin-auctions.js
```
**Commit message:** `feat: Implement admin auction management (CRUD, start, end)`

#### Commit 21: [Thành viên 3] - Admin User Management & Polish
**Thời gian:** Ngày 11-12  
**Nội dung:**
```
- Tạo source/client/public/admin/users.html:
  * List all users
  * Ban/Unban buttons
  * Update balance form
- Tạo source/client/public/js/main.js:
  * Global utilities
  * API helpers
  * Error handling
- Polish CSS và responsive design
```
**Commit message:** `feat: Add admin user management and improve UI/UX`

---

### **GIAI ĐOẠN 5: Testing & Documentation** (2-3 commits)

#### Commit 22: [Thành viên 1] - Testing & Bug Fixes
**Thời gian:** Ngày 12-13  
**Nội dung:**
```
- Test toàn bộ backend APIs
- Fix bugs phát hiện được
- Optimize database queries
- Add validation improvements
```
**Commit message:** `fix: Backend bug fixes and query optimization`

#### Commit 23: [Thành viên 3] - Frontend Testing & Fixes
**Thời gian:** Ngày 13  
**Nội dung:**
```
- Test toàn bộ user flows
- Fix UI bugs
- Improve error messages
- Add loading states
```
**Commit message:** `fix: Frontend bug fixes and UX improvements`

#### Commit 24: [Thành viên 2] - Integration Testing & Final Polish
**Thời gian:** Ngày 13-14  
**Nội dung:**
```
- Test WebSocket connections
- Test auction scheduler
- Test concurrent bidding scenarios
- Final bug fixes
```
**Commit message:** `fix: Integration testing and WebSocket improvements`

#### Commit 25: [Tất cả] - Documentation & Screenshots
**Thời gian:** Ngày 14  
**Nội dung:**
```
- Update README.md với đầy đủ thông tin:
  * Thông tin nhóm
  * Hướng dẫn cài đặt
  * Hướng dẫn chạy
  * API documentation
  * Screenshots
- Tạo thư mục statics/ và thêm screenshots
- Finalize INSTRUCTION.md compliance
```
**Commit message:** `docs: Complete documentation with screenshots and setup guide`

---

## 📊 TỔNG KẾT PHÂN CÔNG

### Thành viên 1 (Backend Core): ~35% công việc
- **Commits:** 1, 2, 5, 6, 7, 12, 13, 14, 22
- **Tổng:** 9 commits
- **Focus:** Database, Entities, Repositories, Auth, Admin APIs, Watchlist

### Thành viên 2 (Backend Features): ~30% công việc  
- **Commits:** 3, 8, 9, 10, 11, 24
- **Tổng:** 6 commits
- **Focus:** Security, WebSocket, Auctions, Bidding, Scheduler, Integration

### Thành viên 3 (Frontend): ~35% công việc
- **Commits:** 4, 15, 16, 17, 18, 19, 20, 21, 23
- **Tổng:** 9 commits
- **Focus:** UI/UX, HTML, CSS, JavaScript, Admin Panel, User Pages

### Commit chung: 1 commit (25)
- Documentation final

---

## 💡 LƯU Ý QUAN TRỌNG

### 1. **Thứ tự commit:**
- Tuân thủ đúng thứ tự từ Commit 1 → 25
- Không commit vượt quá 2-3 commits/ngày để tự nhiên
- Mỗi người commit vào các ngày khác nhau

### 2. **Commit message format:**
```
<type>: <description>

Types: feat, fix, docs, style, refactor, test
```

### 3. **Trước mỗi commit:**
- Test code để đảm bảo không lỗi
- Chỉ commit những file liên quan
- Viết commit message rõ ràng, cụ thể

### 4. **Git commands:**
```bash
# Thêm file cụ thể
git add source/server/src/main/java/com/auction/model/User.java

# Commit với message
git commit -m "feat: Add User entity with JPA annotations"

# Push lên GitHub
git push origin main
```

### 5. **Communication:**
- Mỗi thành viên nên comment trên commit của nhau
- Review code qua Pull Request nếu có thể
- Báo cáo tiến độ trong nhóm

---

## ✅ CHECKLIST TRƯỚC KHI NỘP

- [ ] Tất cả 25 commits đã được push
- [ ] Mỗi thành viên có ít nhất 6-9 commits
- [ ] README.md đầy đủ thông tin
- [ ] Có screenshots trong statics/
- [ ] Code chạy được theo hướng dẫn
- [ ] Không có lỗi compile/runtime nghiêm trọng
- [ ] Tuân thủ cấu trúc INSTRUCTION.md

---

## 📞 HỖ TRỢ

Nếu cần thêm chi tiết về bất kỳ commit nào, hãy hỏi tôi!

**Good luck! 🎉**
