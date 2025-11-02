# =Ë K¾ HO CH CHIA CÔNG VIÆC - NHÓM 06

## <¯ TÔNG QUAN

**Thông tin nhóm:**
- **Nhóm:** Nhóm 06  L­p trình m¡ng
- **Thành viên:**
  1. NguyÅn TrÍng Khßi (B22DCCN471)
  2. Tr°¡ng Huy Tâm (B22DCCN711)
  3. Vi Thành Nam (B22DCCN568)

**Base Code (ã có trên main - basic-project):**
- Login/Register
- Basic config + WebSocket setup
- Health check

**C§n chia thêm:**
- Chéc nng Admin ’ **1 ng°Ýi (Khßi ho·c Nam)**
- Chéc nng User ’ **2 ng°Ýi (Tâm + 1 ng°Ýi)**

---

## =d NG¯ÜI 1: ADMIN FEATURES

**Ng°Ýi £m nhiÇm:** Khßi ho·c Nam

### Backend - Java Files (12 files)

#### **Models/Entities** (2 files)
- `Auction.java` - Entity auction vÛi §y ç fields
  - auctionId, title, description, startingPrice, currentPrice
  - highestBidder, status (PENDING/ACTIVE/ENDED)
  - startTime, endTime, totalBids, extendCount

- `User.java` - Update entity user (admin role)
  - userId, username, password, email, fullName
  - balance, role (USER/ADMIN), isActive

#### **Repositories** (2 files)
- `AuctionRepository.java` - JPA repository cho Auction
  ```java
  findByStatus(AuctionStatus status)
  findByCreatedBy(User user)
  findByStatusAndEndTimeAfter(...)
  ```

- `UserRepository.java` - JPA repository cho User
  ```java
  findByUsername(String username)
  findByEmail(String email)
  findAll()
  ```

#### **Services** (3 files)
- `AdminAuctionService.java` (10.2KB)
  - createAuction() - T¡o auction mÛi
  - updateAuction() - C­p nh­t auction
  - deleteAuction() - Xóa auction
  - startAuction() - B¯t §u auction + broadcast
  - endAuction() - K¿t thúc auction + broadcast
  - getAllAuctions() - L¥y t¥t c£ auctions

- `AdminUserService.java` (4.2KB)
  - getAllUsers() - L¥y danh sách users
  - updateUserBalance() - C­p nh­t sÑ d°
  - toggleUserStatus() - B­t/t¯t tr¡ng thái user
  - deleteUser() - Xóa user

- `AdminStatisticsService.java` (10.8KB)
  - getSystemStatistics() - ThÑng kê hÇ thÑng
  - getTotalUsers(), getTotalAuctions()
  - getActiveAuctions(), getRevenueStats()

#### **Controllers** (1 file)
- `AdminController.java` (9KB) - REST API endpoints:

  **Auction Management:**
  - POST `/api/admin/auctions` - T¡o auction
  - PUT `/api/admin/auctions/{id}` - C­p nh­t
  - DELETE `/api/admin/auctions/{id}` - Xóa
  - POST `/api/admin/auctions/{id}/start` - B¯t §u
  - POST `/api/admin/auctions/{id}/end` - K¿t thúc
  - GET `/api/admin/auctions/all` - L¥y t¥t c£

  **User Management:**
  - GET `/api/admin/users` - Danh sách users
  - PUT `/api/admin/users/{id}/balance` - C­p nh­t sÑ d°
  - PUT `/api/admin/users/{id}/status` - B­t/t¯t tr¡ng thái

  **Statistics:**
  - GET `/api/admin/statistics` - ThÑng kê hÇ thÑng

#### **DTOs** (2 files)
- `AuctionDTO.java` - Data transfer object cho Auction
- `UserDTO.java` - Data transfer object cho User

#### **Config** (1 file)
- `SecurityConfig.java` - Spring Security vÛi admin role check
  - Form-based login
  - BCrypt password encoding
  - Role-based authorization (ADMIN only)

#### **Other** (1 file)
- `ApiResponse.java` - Generic response wrapper
  ```java
  {
    "success": boolean,
    "message": string,
    "data": T,
    "timestamp": ISO-8601
  }
  ```

### Frontend - HTML + JS Files (9 files)

#### **HTML Pages** (3 files)
- `admin/dashboard.html` - Admin dashboard
  - Statistics cards (total users, auctions, revenue)
  - Charts/graphs
  - Recent activities

- `admin/auctions.html` - Qu£n lý auction
  - Auction table (title, price, status, actions)
  - CRUD forms (Create, Edit)
  - Start/End buttons
  - Delete confirmation

- `admin/users.html` - Qu£n lý user
  - User table (username, email, balance, status)
  - Update balance form
  - Toggle status button
  - Delete confirmation

#### **JavaScript** (5 files)
- `js/admin-config.js` - Admin API endpoints config
  ```javascript
  ADMIN_API = {
    auctions: '/api/admin/auctions',
    users: '/api/admin/users',
    statistics: '/api/admin/statistics'
  }
  ```

- `js/admin-dashboard.js` - Load statistics, charts
  - loadStatistics()
  - displayMetrics()
  - renderCharts()
  - connectWebSocket()

- `js/admin-auctions.js` - CRUD auctions logic
  - loadAuctions()
  - createAuction()
  - updateAuction()
  - deleteAuction()
  - startAuction()
  - endAuction()
  - displayAuctions()

- `js/admin-users.js` - User management logic
  - loadUsers()
  - updateUserBalance()
  - toggleUserStatus()
  - deleteUser()
  - displayUsers()

- `js/admin-websocket.js` - WebSocket cho admin
  - connectAdminWebSocket()
  - subscribeToAuctionUpdates()
  - handleAuctionCreated()
  - handleAuctionStarted()
  - handleAuctionEnded()

#### **CSS** (1 file)
- `css/admin-style.css` - Admin UI styling
  - Sidebar navigation
  - Dashboard metrics cards
  - Data tables
  - Modal dialogs
  - Form styling

### **TÔNG: 12 Java files + 9 Frontend files = 21 files**

### Commits Á xu¥t:
```bash
git commit -m "feat(admin): add auction and user entities with repositories"
git commit -m "feat(admin): add admin services (auction, user, statistics)"
git commit -m "feat(admin): add admin controller with REST API endpoints"
git commit -m "feat(admin): add admin dashboard UI with statistics"
git commit -m "feat(admin): add auction management CRUD interface"
git commit -m "feat(admin): add user management interface"
git commit -m "feat(admin): add real-time updates via WebSocket"
```

---

## =d NG¯ÜI 2: USER CORE FEATURES

**Ng°Ýi £m nhiÇm:** Tâm

### Backend - Java Files (9 files)

#### **Models/Entities** (1 file)
- `Bid.java` - Entity cho bid
  - bidId, auction, user, bidAmount, bidTime
  - Immutable (không thÃ sía sau khi t¡o)
  - Indexed by (auction_id, bid_time DESC)

#### **Repositories** (1 file)
- `BidRepository.java` - JPA repository cho Bid
  ```java
  findByAuctionOrderByBidTimeDesc(Auction auction)
  findByUserOrderByBidTimeDesc(User user)
  save(Bid bid)
  ```

#### **Services** (2 files)
- `AuctionService.java` (6.9KB)
  - getAllAuctions() - L¥y t¥t c£ auctions
  - getActiveAuctions() - ChÉ l¥y ACTIVE
  - getAuctionById() - Chi ti¿t 1 auction
  - searchAuctions() - Tìm ki¿m theo keyword
  - getAuctionsByStatus() - LÍc theo status

- `BidService.java` (5.7KB) P **CORE SERVICE**
  ```java
  @Transactional
  public synchronized BidDTO placeBid(User user, Long auctionId, BigDecimal bidAmount) {
    // 1. Validate auction status (ACTIVE, not ended)
    // 2. Validate bid amount >= (currentPrice + increment)
    // 3. Check user balance >= bidAmount
    // 4. Save bid to database
    // 5. Update auction (currentPrice, highestBidder, totalBids)
    // 6. BROADCAST via WebSocket -> /topic/auction/{id}
    // 7. Check auto-extend (if < 60s remaining && extendCount < 3)
    // 8. Return BidDTO
  }

  getBidHistory(auctionId)
  getUserBids(userId)
  ```

#### **Controllers** (2 files)
- `AuctionController.java` - REST API:
  - GET `/api/auctions` - T¥t c£ auctions
  - GET `/api/auctions/active` - ChÉ ACTIVE
  - GET `/api/auctions/{id}` - Chi ti¿t
  - GET `/api/auctions/search?keyword=...` - Tìm ki¿m

- `BidController.java` - REST API:
  - POST `/api/bids` - ·t giá
    - Input: {auctionId, bidAmount}, userId param
    - Process: Validate ’ Save ’ Broadcast
  - GET `/api/bids/auction/{auctionId}` - Bid history
  - GET `/api/bids/user?userId=...` - My bids

#### **WebSocket** (1 file)
- `WebSocketController.java` - STOMP message handling
  ```java
  @MessageMapping("/join/{auctionId}")
  public void joinAuction(@DestinationVariable Long auctionId) {
    // Broadcast USER_JOINED event
  }

  @MessageMapping("/leave/{auctionId}")
  public void leaveAuction(@DestinationVariable Long auctionId) {
    // Broadcast USER_LEFT event
  }

  // Events:
  // - BID_UPDATE {newPrice, bidderName, totalBids, timeRemaining}
  // - AUCTION_EXTENDED {newEndTime}
  // - AUCTION_ENDED {auctionId}
  ```

#### **Scheduler** (1 file)
- `AuctionSchedulerService.java` (4.9KB)
  ```java
  @Scheduled(fixedDelay = 30000) // Every 30 seconds
  public void checkAuctionStatus() {
    // Check PENDING ’ ACTIVE (if now >= startTime)
    // Check ACTIVE ’ ENDED (if now >= endTime)
    // Broadcast status changes
  }
  ```

#### **Config** (1 file)
- `DataInitializer.java` - Sample data
  - Admin: admin/admin123
  - Users: user1/password, user2/password
  - 3 sample auctions

#### **DTOs** (2 files)
- `BidDTO.java` - Data transfer object cho Bid
- `BidRequest.java` - Request object cho place bid

### Frontend - HTML + JS Files (8 files)

#### **HTML Pages** (3 files)
- `dashboard.html` - User dashboard
  - All auctions grid
  - Filter by status (ALL/ACTIVE/PENDING/ENDED)
  - Search by keyword
  - Statistics (total auctions, active, pending, ended)
  - Sort: ACTIVE ’ PENDING ’ ENDED

- `auction-detail.html` - Auction detail
  - Auction info (title, description, image)
  - Current price, highest bidder
  - Countdown timer (real-time)
  - Bidding panel (input + place bid button)
  - Bid history table (real-time updates)
  - Add to watchlist button

- `my-bids.html` - Bid history
  - Table of user's bids
  - Columns: Auction, Bid Amount, Time, Status

#### **JavaScript** (4 files)
- `js/dashboard.js` (300+ lines) P IMPORTANT
  ```javascript
  init() {
    checkAuth()
    loadCurrentUser()
    loadAuctions()           // GET /api/auctions
    loadStatistics()         // Calculate stats
    connectWebSocket()       // Subscribe /topic/auctions
  }

  filterAuctions(status)     // ALL/ACTIVE/PENDING/ENDED
  searchAuctions(keyword)    // Filter by keyword
  sortAuctions()             // ACTIVE ’ PENDING ’ ENDED
  renderAuctions()           // Display grid
  handleAuctionEvent()       // WebSocket events
  getCountdownText(endTime)  // Format countdown
  ```

- `js/auction.js` (400+ lines) P CORE
  ```javascript
  init() {
    getAuctionId()           // From URL param
    loadAuction()            // GET /api/auctions/{id}
    loadBidHistory()         // GET /api/bids/auction/{id}
    connectWebSocket()       // Subscribe /topic/auction/{id}
    startCountdown()         // Update every 1 second
  }

  placeBid(amount) {
    // Validate amount
    // POST /api/bids
    // Show success/error toast
  }

  handleBidUpdate(event) {
    // Update current price
    // Update highest bidder
    // Add to bid history
    // Update countdown
  }

  handleAuctionEnded() {
    // Show "Auction Ended" message
    // Disable bid button
  }

  updateCountdown()          // Every 1 second
  addToWatchlist()           // POST /api/watchlist
  displayBidHistory()        // Render bids table
  ```

- `js/my-bids.js` (150+ lines)
  ```javascript
  loadUserBids()             // GET /api/bids/user?userId=...
  displayBids()              // Render in table
  formatCurrency()
  formatDate()
  ```

- `js/header.js` (200+ lines)
  ```javascript
  renderHeader(currentPage) {
    // Navbar with logo
    // Navigation menu (Dashboard, My Bids, Watchlist)
    // User info (username, balance)
    // Logout button
  }

  updateHeaderBalance()      // Update balance display
  logout()                   // Clear localStorage + redirect
  ```

#### **CSS Updates** (1 file)
- `css/style.css` - Add styles:
  - Dashboard layout (grid, filters, search)
  - Auction cards (image, price, countdown)
  - Auction detail page
  - Bidding panel
  - Countdown timer styling
  - Bid history table
  - Toast notifications

### **TÔNG: 9 Java files + 8 Frontend files = 17 files**

### Commits Á xu¥t:
```bash
git commit -m "feat(user): add bid entity and repository"
git commit -m "feat(user): add auction service with search and filter"
git commit -m "feat(user): add bid service with WebSocket broadcast"
git commit -m "feat(user): add auction and bid controllers"
git commit -m "feat(user): add WebSocket controller for real-time events"
git commit -m "feat(core): add auction scheduler for auto start/end"
git commit -m "feat(user): add user dashboard with search and filter"
git commit -m "feat(user): add auction detail page with real-time bidding"
git commit -m "feat(user): add bid history page"
git commit -m "feat(user): add countdown timer and auto-extend logic"
```

---

## =d NG¯ÜI 3: USER SUPPORTING FEATURES

**Ng°Ýi £m nhiÇm:** Ng°Ýi còn l¡i (Khßi ho·c Nam)

### Backend - Java Files (7 files)

#### **Models/Entities** (1 file)
- `Watchlist.java` - Entity cho watchlist
  - watchlistId, user, auction, addedTime
  - UNIQUE constraint (user_id, auction_id)
  - Ordered by addedTime DESC

#### **Repositories** (1 file)
- `WatchlistRepository.java` - JPA repository
  ```java
  findByUserOrderByAddedTimeDesc(User user)
  findByUserAndAuction(User user, Auction auction)
  findByAuction(Auction auction)
  delete(Watchlist item)
  ```

#### **Services** (2 files)
- `WatchlistService.java` (3.6KB)
  - addToWatchlist() - Thêm vào watchlist
  - removeFromWatchlist() - Xóa khÏi watchlist
  - getUserWatchlist() - L¥y danh sách watchlist
  - isInWatchlist() - Check xem có trong watchlist không

- `UserService.java` (4.5KB)
  - getUserById() - L¥y thông tin user
  - updateUser() - C­p nh­t profile
  - updateBalance() - C­p nh­t sÑ d°
  - toggleStatus() - B­t/t¯t tr¡ng thái

#### **Controllers** (2 files)
- `WatchlistController.java` - REST API:
  - POST `/api/watchlist` - Thêm vào watchlist
    - Input: {userId, auctionId}
  - DELETE `/api/watchlist/auction/{auctionId}?userId=...` - Xóa
  - GET `/api/watchlist/user?userId=...` - Danh sách
  - GET `/api/watchlist/check?userId=...&auctionId=...` - Check

- `UserController.java` - REST API:
  - GET `/api/users/{id}` - L¥y profile
  - PUT `/api/users/{id}` - C­p nh­t profile
    - Input: {fullName, email, ...}

#### **Security** (1 file)
- `CustomUserDetailsService.java` (2.2KB)
  - Spring Security UserDetailsService
  - loadUserByUsername() - Load user të DB
  - Password validation vÛi BCrypt

### Frontend - HTML + JS Files (6 files)

#### **HTML Pages** (2 files)
- `watchlist.html` - Watchlist page
  - Grid of watchlist auctions
  - Remove button for each item
  - Navigate to auction detail
  - Empty state message

- `index.html` - Landing page
  - Hero section
  - 5 active auctions preview
  - Login/Register buttons
  - Quick search
  - Navigate to dashboard

#### **JavaScript** (3 files)
- `js/watchlist.js` (150+ lines)
  ```javascript
  init() {
    checkAuth()
    loadWatchlist()          // GET /api/watchlist/user
  }

  loadWatchlist() {
    // GET watchlist items
    // Display in grid
  }

  removeFromWatchlist(auctionId) {
    // DELETE /api/watchlist/auction/{id}
    // Reload watchlist
  }

  displayWatchlist()         // Render items
  navigateToAuction()        // Go to auction-detail
  ```

- `js/main.js` (100+ lines)
  ```javascript
  init() {
    loadActiveAuctions()     // GET /api/auctions/active
  }

  loadActiveAuctions() {
    // Fetch 5 active auctions
    // Display in hero section
  }

  displayAuctions()          // Render auction cards
  searchAuctions()           // Quick search
  ```

- `js/admin-header.js` (150+ lines)
  ```javascript
  renderAdminHeader(currentPage) {
    // Admin navbar
    // Admin menu (Dashboard, Auctions, Users)
    // Admin info
    // Logout
  }

  logout()                   // Admin logout
  ```

#### **CSS Updates** (1 file)
- `css/style.css` - Add styles:
  - Landing page hero section
  - Active auctions preview
  - Watchlist grid layout
  - Empty state styling
  - Buttons and hover effects

### **TÔNG: 7 Java files + 6 Frontend files = 13 files**

### Commits Á xu¥t:
```bash
git commit -m "feat(user): add watchlist entity and repository"
git commit -m "feat(user): add watchlist service and controller"
git commit -m "feat(user): add user service and controller"
git commit -m "feat(auth): add Spring Security user details service"
git commit -m "feat(user): add watchlist page UI"
git commit -m "feat(user): add landing page with active auctions"
git commit -m "feat(ui): add admin header component"
```

---

## =Ê TÔNG K¾T PHÂN CHIA

| Ng°Ýi | Chéc nng | Backend Files | Frontend Files | TÕng | Ù khó |
|-------|-----------|---------------|----------------|------|--------|
| **Ng°Ýi 1** | Admin Features | 12 Java | 9 HTML/JS/CSS | **21** | PPP (Trung bình) |
| **Ng°Ýi 2** | User Core (Bidding) | 9 Java | 8 HTML/JS/CSS | **17** | PPPP (Khó - WebSocket) |
| **Ng°Ýi 3** | User Support | 7 Java | 6 HTML/JS/CSS | **13** | PP (DÅ) |
| **TÔNG** | | **28** | **23** | **51** | |

---

## = WORKFLOW À XU¤T

### **B°Ûc 1: Chu©n bË Database Schema**

```sql
-- File: database-schema.sql
-- C£ 3 ng°Ýi cùng t¡o database schema

CREATE DATABASE auction_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE auction_db;

-- Users table
CREATE TABLE users (
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  full_name VARCHAR(100),
  balance DECIMAL(15, 2) DEFAULT 10000000,
  role VARCHAR(50) DEFAULT 'USER',
  is_active BOOLEAN DEFAULT true,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP
);

-- Auctions table
CREATE TABLE auctions (
  auction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  starting_price DECIMAL(15, 2) NOT NULL,
  current_price DECIMAL(15, 2) NOT NULL,
  reserve_price DECIMAL(15, 2),
  bid_increment DECIMAL(15, 2) DEFAULT 100000,
  highest_bidder_id BIGINT,
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  status VARCHAR(20) DEFAULT 'PENDING',
  total_bids INT DEFAULT 0,
  extend_count INT DEFAULT 0,
  duration_minutes INT,
  last_bid_time DATETIME,
  created_by BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  image_url VARCHAR(255),
  FOREIGN KEY (highest_bidder_id) REFERENCES users(user_id),
  FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- Bids table
CREATE TABLE bids (
  bid_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  auction_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  bid_amount DECIMAL(15, 2) NOT NULL,
  bid_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (auction_id) REFERENCES auctions(auction_id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  INDEX idx_auction_bid_time (auction_id, bid_time DESC),
  INDEX idx_user_bid_time (user_id, bid_time DESC)
);

-- Watchlist table
CREATE TABLE watchlist (
  watchlist_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  auction_id BIGINT NOT NULL,
  added_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (auction_id) REFERENCES auctions(auction_id) ON DELETE CASCADE,
  UNIQUE KEY unique_user_auction (user_id, auction_id)
);
```

### **B°Ûc 2: Shared Code (Làm tr°Ûc - Ng°Ýi 3 ho·c c£ 3 cùng làm)**

**File c§n t¡o tr°Ûc:**

1. **Entities c¡ b£n:**
   - `User.java`, `Auction.java`, `Bid.java`, `Watchlist.java`

2. **DTOs chung:**
   - `ApiResponse.java`
   - `UserDTO.java`
   - `AuctionDTO.java`
   - `BidDTO.java`
   - `WatchlistDTO.java`

3. **Config files:**
   - `application.properties` (DB config)
   - `WebConfig.java` (CORS)

### **B°Ûc 3: Phát triÃn song song (3 nhánh riêng biÇt)**

#### **Ng°Ýi 1 (Admin)** - Branch: `feature/admin`

```bash
# T¡o branch të test2
git checkout test2
git pull origin test2
git checkout -b feature/admin

# Develop admin features
# 1. Backend: Entities, Repositories, Services, Controllers
# 2. Frontend: HTML pages, JS modules, CSS

# Commit theo tëng feature
git add source/server/src/main/java/com/auction/model/
git commit -m "feat(admin): add auction and user entities"

git add source/server/src/main/java/com/auction/repository/
git commit -m "feat(admin): add auction and user repositories"

git add source/server/src/main/java/com/auction/service/Admin*
git commit -m "feat(admin): add admin services"

git add source/server/src/main/java/com/auction/controller/AdminController.java
git commit -m "feat(admin): add admin controller with REST API"

git add source/client/public/admin/
git commit -m "feat(admin): add admin dashboard UI"

git add source/client/public/js/admin-*
git commit -m "feat(admin): add admin JavaScript modules"

# Push to remote
git push origin feature/admin
```

#### **Ng°Ýi 2 (User Core)** - Branch: `feature/user-core`

```bash
# T¡o branch të test2
git checkout test2
git pull origin test2
git checkout -b feature/user-core

# Develop user core features
# 1. Bid entity, repository, service
# 2. Auction service, controller
# 3. WebSocket controller
# 4. Frontend: dashboard, auction-detail, my-bids

# Commit theo tëng feature
git add source/server/src/main/java/com/auction/model/Bid.java
git commit -m "feat(user): add bid entity and repository"

git add source/server/src/main/java/com/auction/service/AuctionService.java
git commit -m "feat(user): add auction service"

git add source/server/src/main/java/com/auction/service/BidService.java
git commit -m "feat(user): add bid service with WebSocket broadcast"

git add source/server/src/main/java/com/auction/controller/AuctionController.java
git add source/server/src/main/java/com/auction/controller/BidController.java
git commit -m "feat(user): add auction and bid controllers"

git add source/server/src/main/java/com/auction/websocket/
git commit -m "feat(user): add WebSocket controller"

git add source/server/src/main/java/com/auction/service/AuctionSchedulerService.java
git commit -m "feat(core): add auction scheduler"

git add source/client/public/dashboard.html
git add source/client/public/js/dashboard.js
git commit -m "feat(user): add user dashboard"

git add source/client/public/auction-detail.html
git add source/client/public/js/auction.js
git commit -m "feat(user): add auction detail with real-time bidding"

git add source/client/public/my-bids.html
git add source/client/public/js/my-bids.js
git commit -m "feat(user): add bid history page"

# Push to remote
git push origin feature/user-core
```

#### **Ng°Ýi 3 (User Support)** - Branch: `feature/user-support`

```bash
# T¡o branch të test2
git checkout test2
git pull origin test2
git checkout -b feature/user-support

# Develop supporting features
# 1. Watchlist entity, repository, service
# 2. User service
# 3. Authentication service
# 4. Frontend: watchlist, landing page

# Commit theo tëng feature
git add source/server/src/main/java/com/auction/model/Watchlist.java
git commit -m "feat(user): add watchlist entity and repository"

git add source/server/src/main/java/com/auction/service/WatchlistService.java
git add source/server/src/main/java/com/auction/controller/WatchlistController.java
git commit -m "feat(user): add watchlist service and controller"

git add source/server/src/main/java/com/auction/service/UserService.java
git add source/server/src/main/java/com/auction/controller/UserController.java
git commit -m "feat(user): add user service and controller"

git add source/server/src/main/java/com/auction/service/CustomUserDetailsService.java
git commit -m "feat(auth): add Spring Security user details service"

git add source/client/public/watchlist.html
git add source/client/public/js/watchlist.js
git commit -m "feat(user): add watchlist page"

git add source/client/public/index.html
git add source/client/public/js/main.js
git commit -m "feat(user): add landing page"

# Push to remote
git push origin feature/user-support
```

### **B°Ûc 4: Merge vào test2**

**Thé tñ merge Á xu¥t:**

1. **Merge Ng°Ýi 3 tr°Ûc** (ít conflict, foundation code):
   ```bash
   git checkout test2
   git merge feature/user-support
   # Resolve conflicts n¿u có
   git push origin test2
   ```

2. **Merge Ng°Ýi 2** (core features):
   ```bash
   git checkout test2
   git pull origin test2
   git merge feature/user-core
   # Resolve conflicts n¿u có
   git push origin test2
   ```

3. **Merge Ng°Ýi 1 cuÑi** (admin features, ít phå thuÙc):
   ```bash
   git checkout test2
   git pull origin test2
   git merge feature/admin
   # Resolve conflicts n¿u có
   git push origin test2
   ```

### **B°Ûc 5: Testing & Integration**

```bash
# Sau khi merge xong, c£ 3 cùng test
git checkout test2
git pull origin test2

# Run server
cd source/server
mvn clean install
mvn spring-boot:run

# Run client (terminal khác)
cd source/client
npm install
npm start

# Test các tính nng:
# 1. Login/Register
# 2. User dashboard - xem auctions
# 3. Place bid - real-time updates
# 4. Admin dashboard - statistics
# 5. Admin CRUD auctions
# 6. Watchlist
# 7. Bid history
```

---

##   L¯U Ý QUAN TRÌNG

### **Dependencies giïa các ph§n:**

#### **Shared Code (C§n làm tr°Ûc - Ng°Ýi 3 ho·c cùng làm):**
- `User.java`, `Auction.java` - C£ 3 ng°Ýi Áu c§n
- `ApiResponse.java`, `UserDTO.java`, `AuctionDTO.java` - DTOs chung
- `WebSocketConfig.java` - WebSocket config (ã có trong basic-project)
- `application.properties` - Database config

#### **Authentication (Ng°Ýi 3 làm tr°Ûc):**
- `CustomUserDetailsService.java` - Spring Security
- `SecurityConfig.java` - Ng°Ýi 1 s½ update thêm admin role

#### **Thé tñ Merge:**
1. **Ng°Ýi 3** ’ Foundation (auth, user, watchlist)
2. **Ng°Ýi 2** ’ Core features (bidding, WebSocket)
3. **Ng°Ýi 1** ’ Admin features (ít conflict)

### **Potential Conflicts:**

#### **File có thÃ conflict:**
- `application.properties` - C£ 3 có thÃ sía
- `SecurityConfig.java` - Ng°Ýi 1 và 3 cùng sía
- `css/style.css` - Ng°Ýi 2 và 3 cùng sía
- `pom.xml` - N¿u thêm dependencies khác nhau

#### **Gi£i quy¿t conflict:**
```bash
# Khi merge g·p conflict
git status  # Xem file bË conflict

# Edit file, giï c£ 2 ph§n code
# Ho·c th£o lu­n nhóm quy¿t Ënh giï ph§n nào

git add <file>
git commit -m "merge: resolve conflicts from feature/xxx"
```

### **Code Style Guidelines:**

#### **Java:**
- Package naming: `com.auction.xxx`
- Class naming: PascalCase (e.g., `AdminController`)
- Method naming: camelCase (e.g., `getAllAuctions()`)
- Use Lombok annotations: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Use `@Transactional` cho database operations
- Exception handling: throw `RuntimeException` vÛi message rõ ràng

#### **JavaScript:**
- Function naming: camelCase (e.g., `loadAuctions()`)
- Constants: UPPER_SNAKE_CASE (e.g., `API_BASE_URL`)
- Use `async/await` cho fetch calls
- Error handling: try-catch vÛi toast notification
- Format currency: Intl.NumberFormat
- Format date: toLocaleString

#### **HTML/CSS:**
- Class naming: kebab-case (e.g., `auction-card`)
- ID naming: camelCase (e.g., `auctionDetail`)
- Consistent spacing: 2 spaces
- Mobile-first responsive design

---

## =Ý CHECKLIST CHO MÖI NG¯ÜI

### **Ng°Ýi 1 (Admin):**

#### Backend:
- [ ] `Auction.java` - Entity vÛi §y ç fields
- [ ] `User.java` - Update vÛi admin role
- [ ] `AuctionRepository.java` - JPA methods
- [ ] `UserRepository.java` - JPA methods
- [ ] `AdminAuctionService.java` - CRUD + start/end
- [ ] `AdminUserService.java` - User management
- [ ] `AdminStatisticsService.java` - Statistics
- [ ] `AdminController.java` - 11 REST endpoints
- [ ] `AuctionDTO.java`, `UserDTO.java` - DTOs
- [ ] `SecurityConfig.java` - Admin role check
- [ ] `ApiResponse.java` - Generic response

#### Frontend:
- [ ] `admin/dashboard.html` - Statistics UI
- [ ] `admin/auctions.html` - Auction CRUD
- [ ] `admin/users.html` - User management
- [ ] `js/admin-config.js` - API config
- [ ] `js/admin-dashboard.js` - Dashboard logic
- [ ] `js/admin-auctions.js` - CRUD logic
- [ ] `js/admin-users.js` - User management logic
- [ ] `js/admin-websocket.js` - Real-time updates
- [ ] `css/admin-style.css` - Admin styling

#### Testing:
- [ ] Login as admin (admin/admin123)
- [ ] View statistics dashboard
- [ ] Create new auction
- [ ] Update auction
- [ ] Delete auction
- [ ] Start auction (broadcast works)
- [ ] End auction (broadcast works)
- [ ] View users list
- [ ] Update user balance
- [ ] Toggle user status

---

### **Ng°Ýi 2 (User Core):**

#### Backend:
- [ ] `Bid.java` - Entity vÛi indexes
- [ ] `BidRepository.java` - JPA methods
- [ ] `AuctionService.java` - Get, search, filter
- [ ] `BidService.java` - Place bid + broadcast
- [ ] `AuctionController.java` - 4 endpoints
- [ ] `BidController.java` - 3 endpoints
- [ ] `WebSocketController.java` - STOMP handlers
- [ ] `AuctionSchedulerService.java` - Auto start/end
- [ ] `DataInitializer.java` - Sample data
- [ ] `BidDTO.java`, `BidRequest.java` - DTOs

#### Frontend:
- [ ] `dashboard.html` - All auctions + filter/search
- [ ] `auction-detail.html` - Detail + bidding panel
- [ ] `my-bids.html` - Bid history
- [ ] `js/dashboard.js` - Dashboard logic + WebSocket
- [ ] `js/auction.js` - Real-time bidding + countdown
- [ ] `js/my-bids.js` - Bid history logic
- [ ] `js/header.js` - User header component
- [ ] `css/style.css` - User UI styles

#### Testing:
- [ ] Login as user (user1/password)
- [ ] View dashboard with all auctions
- [ ] Filter auctions (ACTIVE/PENDING/ENDED)
- [ ] Search auctions by keyword
- [ ] View auction detail
- [ ] Place bid successfully
- [ ] Real-time price update (open 2 browsers)
- [ ] Countdown timer works
- [ ] Auto-extend when bid < 60s
- [ ] Bid history shows all bids
- [ ] My bids page shows user's bids
- [ ] WebSocket events work

---

### **Ng°Ýi 3 (User Support):**

#### Backend:
- [ ] `Watchlist.java` - Entity vÛi unique constraint
- [ ] `WatchlistRepository.java` - JPA methods
- [ ] `WatchlistService.java` - Add/remove/list
- [ ] `WatchlistController.java` - 4 endpoints
- [ ] `UserService.java` - User CRUD
- [ ] `UserController.java` - 2 endpoints
- [ ] `CustomUserDetailsService.java` - Spring Security

#### Frontend:
- [ ] `watchlist.html` - Watchlist page
- [ ] `index.html` - Landing page
- [ ] `js/watchlist.js` - Watchlist logic
- [ ] `js/main.js` - Landing page logic
- [ ] `js/admin-header.js` - Admin header component
- [ ] `css/style.css` - Watchlist + landing styles

#### Testing:
- [ ] Login as user
- [ ] Add auction to watchlist
- [ ] View watchlist page
- [ ] Remove from watchlist
- [ ] Landing page shows 5 active auctions
- [ ] User profile update works
- [ ] Authentication works properly

---

## =€ TIMELINE À XU¤T

### **Tu§n 1: Chu©n bË & Shared Code**
- **Ngày 1-2:** T¡o database schema, entities, repositories
- **Ngày 3-4:** T¡o DTOs, config files
- **Ngày 5-7:** M×i ng°Ýi b¯t §u develop feature cça mình

### **Tu§n 2: Phát triÃn chính**
- **Ngày 8-10:** Backend services & controllers
- **Ngày 11-13:** Frontend UI & JavaScript
- **Ngày 14:** Testing & bug fixes

### **Tu§n 3: Integration & Testing**
- **Ngày 15:** Merge code, resolve conflicts
- **Ngày 16-17:** Integration testing
- **Ngày 18-19:** Bug fixes & refinements
- **Ngày 20-21:** Final testing & documentation

---

## =Þ COMMUNICATION

### **Daily Standup (10-15 phút/ngày):**
- Hôm qua làm gì?
- Hôm nay làm gì?
- Có blockers/issues gì?

### **Code Review:**
- M×i ng°Ýi review code cça ng°Ýi khác tr°Ûc khi merge
- Comment trên GitHub Pull Request
- Approve sau khi OK

### **Shared Resources:**
- **GitHub Repository:** Shared repo
- **Discord/Zalo:** Group chat
- **Google Docs:** Shared documents
- **Postman:** API testing collection

---

## <¯ EXPECTED OUTPUT

Sau khi hoàn thành, hÇ thÑng ph£i có:

### **Features hoàn chÉnh:**
 User registration/login vÛi Spring Security
 User dashboard vÛi filter/search/sort
 Real-time bidding vÛi WebSocket (< 50ms latency)
 Auto-extend auction logic
 Countdown timer
 Bid history
 Watchlist
 Admin dashboard vÛi statistics
 Admin CRUD auctions
 Admin user management
 Scheduled tasks (auto start/end)

### **Technical requirements:**
 Spring Boot 3.2.0 backend
 MySQL database vÛi 4 tables
 REST API vÛi JSON responses
 WebSocket (STOMP) real-time updates
 Spring Security authentication
 Responsive UI (Bootstrap 5)
 Clean code vÛi comments
 Git history rõ ràng

### **Documentation:**
 README.md vÛi setup instructions
 API documentation (endpoints)
 Database schema
 Architecture diagram (optional)
 Testing guide

---

**Chúc c£ nhóm làm viÇc hiÇu qu£! =€**

N¿u có v¥n Á gì, hãy trao Õi ngay trong nhóm Ã gi£i quy¿t kËp thÝi.
