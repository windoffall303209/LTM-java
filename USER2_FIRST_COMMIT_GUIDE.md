# 🚀 USER2 - COMMIT ĐẦU TIÊN (Foundation)

## 📋 **TÌNH HUỐNG**

```
Main branch hiện tại: ✅ Đã có FULL CODE (toàn bộ project)
Mục tiêu: Giả lập quá trình phát triển incremental
User2 làm gì: Tạo branch mới và commit từng phần một
```

---

## 🎯 **CHIẾN LƯỢC**

### **Option 1: Tạo branch từ commit rỗng (DEMO CHO THẦY)**

```bash
# Tạo branch orphan (không có history)
git checkout --orphan feature/user2-foundation

# Xóa tất cả files hiện tại (để bắt đầu từ đầu)
git rm -rf .

# Bây giờ bắt đầu add files từ user2 folder từng commit một
```

### **Option 2: Tạo branch từ main hiện tại (THỰC TẾ HƠN)**

```bash
# Tạo branch từ main
git checkout -b feature/user2-foundation

# User2 folder đã có sẵn code
# Chỉ cần copy và commit
```

---

## 📦 **USER2 COMMIT 1: Foundation (Entities, DTOs, Repositories)**

### **🔸 Files cần commit:**

```
📂 source/server/src/main/java/com/auction/

model/ (4 files - Entities)
├── User.java
├── Auction.java
├── Bid.java
└── Watchlist.java

dto/ (7 files - Data Transfer Objects)
├── ApiResponse.java
├── UserDTO.java
├── AuctionDTO.java
├── BidDTO.java
├── WatchlistDTO.java
├── LoginRequest.java
└── RegisterRequest.java

repository/ (4 files - JPA Repositories)
├── UserRepository.java
├── AuctionRepository.java
├── BidRepository.java
└── WatchlistRepository.java
```

### **🔧 CÁCH THỰC HIỆN:**

#### **Bước 1: Copy files từ user2 folder**

```bash
# Đảm bảo đang ở branch feature/user2-foundation
git checkout -b feature/user2-foundation

# Tạo cấu trúc thư mục nếu chưa có
mkdir -p source/server/src/main/java/com/auction/model
mkdir -p source/server/src/main/java/com/auction/dto
mkdir -p source/server/src/main/java/com/auction/repository

# Copy Entities
cp basic-project-user2/source/server/src/main/java/com/auction/model/*.java \
   source/server/src/main/java/com/auction/model/

# Copy DTOs
cp basic-project-user2/source/server/src/main/java/com/auction/dto/*.java \
   source/server/src/main/java/com/auction/dto/

# Copy Repositories
cp basic-project-user2/source/server/src/main/java/com/auction/repository/*.java \
   source/server/src/main/java/com/auction/repository/
```

#### **Bước 2: Copy pom.xml và application.properties**

```bash
# Copy pom.xml (dependencies)
cp basic-project-user2/source/server/pom.xml \
   source/server/pom.xml

# Copy application.properties
mkdir -p source/server/src/main/resources
cp basic-project-user2/source/server/src/main/resources/application.properties \
   source/server/src/main/resources/application.properties
```

#### **Bước 3: Copy Main Application class**

```bash
mkdir -p source/server/src/main/java/com/auction
cp basic-project-user2/source/server/src/main/java/com/auction/AuctionSystemApplication.java \
   source/server/src/main/java/com/auction/
```

#### **Bước 4: Test build**

```bash
cd source/server
mvn clean compile

# Nếu thành công → Sẵn sàng commit
```

#### **Bước 5: Git add và commit**

```bash
# Add tất cả files vừa copy
git add source/server/pom.xml
git add source/server/src/main/java/com/auction/AuctionSystemApplication.java
git add source/server/src/main/java/com/auction/model/
git add source/server/src/main/java/com/auction/dto/
git add source/server/src/main/java/com/auction/repository/
git add source/server/src/main/resources/application.properties

# Check xem có đúng 16 files không
git status

# Commit với message rõ ràng
git commit -m "feat(foundation): add domain models, DTOs, and repositories

ENTITIES (4 files):
- User.java: User account with balance, role (USER/ADMIN)
- Auction.java: Auction item with pricing, timing, status
- Bid.java: Bid history with amount and timestamp
- Watchlist.java: User watchlist for tracking auctions

DTOs (7 files):
- ApiResponse.java: Standard API response wrapper
- UserDTO.java: User data transfer object
- AuctionDTO.java: Auction data transfer object
- BidDTO.java: Bid data transfer object
- WatchlistDTO.java: Watchlist data transfer object
- LoginRequest.java: Login request payload
- RegisterRequest.java: Registration request payload

REPOSITORIES (4 files):
- UserRepository.java: JPA repository for User
- AuctionRepository.java: JPA repository for Auction
- BidRepository.java: JPA repository for Bid
- WatchlistRepository.java: JPA repository for Watchlist

SETUP:
- pom.xml: Maven dependencies (Spring Boot, JPA, MySQL, Security)
- application.properties: Database config (MySQL, Hibernate DDL auto-create)
- AuctionSystemApplication.java: Spring Boot main class

DATABASE:
✅ Database schema will be auto-created by Hibernate JPA
✅ Tables: users, auctions, bids, watchlist
✅ Foreign keys and indexes auto-generated

BUILD STATUS:
✅ mvn clean compile → SUCCESS

NEXT STEPS:
- Add Spring Security configuration
- Add data initializer for sample data
- Add services and controllers"

# Push lên remote
git push origin feature/user2-foundation
```

---

## 📊 **KIỂM TRA SAU COMMIT 1**

### **✅ Checklist:**

```bash
# 1. Check files đã commit
git log --stat -1

# Should show:
# 16 files changed
# - 4 entities
# - 7 DTOs
# - 4 repositories
# - 1 pom.xml
# - 1 application.properties
# - 1 AuctionSystemApplication.java

# 2. Check build
cd source/server
mvn clean compile

# Should see: BUILD SUCCESS

# 3. Check không có files thừa
git status

# Should be clean: "nothing to commit, working tree clean"
```

### **✅ Có thể test gì?**

```bash
# Build OK
mvn clean compile → ✅ SUCCESS

# Entities được nhận diện
mvn clean test-compile → ✅ SUCCESS

# Hibernate sẽ tạo schema (khi chạy server - commit sau)
```

---

## 📦 **USER2 COMMIT 2: Configuration (Security, Data Initializer)**

### **🔸 Files cần commit:**

```
config/ (4 files)
├── SecurityConfig.java       → Spring Security, BCrypt password
├── WebConfig.java            → CORS, WebMvc configuration
├── WebSocketConfig.java      → WebSocket cho real-time
└── DataInitializer.java      → Sample data (admin, users, auctions)

service/ (1 file - cần cho Security)
└── CustomUserDetailsService.java
```

### **🔧 CÁCH THỰC HIỆN:**

```bash
# Copy config files
mkdir -p source/server/src/main/java/com/auction/config
cp basic-project-user2/source/server/src/main/java/com/auction/config/*.java \
   source/server/src/main/java/com/auction/config/

# Copy CustomUserDetailsService (cần cho SecurityConfig)
mkdir -p source/server/src/main/java/com/auction/service
cp basic-project-user2/source/server/src/main/java/com/auction/service/CustomUserDetailsService.java \
   source/server/src/main/java/com/auction/service/

# Test build
cd source/server
mvn clean compile

# Commit
git add source/server/src/main/java/com/auction/config/
git add source/server/src/main/java/com/auction/service/CustomUserDetailsService.java

git commit -m "feat(config): add Spring Security and application configuration

SECURITY:
- SecurityConfig.java: Spring Security with BCrypt password encoding
- Form-based authentication with role-based access control
- Configure public endpoints: /api/auth/**, /css/**, /js/**
- Configure protected endpoints: /api/admin/** (ADMIN only)

WEB CONFIG:
- WebConfig.java: CORS configuration for frontend
- Allow origins: localhost:3000, localhost:5500, localhost:8080
- Allow credentials for session-based auth

WEBSOCKET:
- WebSocketConfig.java: WebSocket STOMP configuration
- Endpoint: /ws for real-time bidding updates
- Message broker: /topic for broadcasting

DATA INITIALIZER:
- DataInitializer.java: Create sample data on startup
- Creates 3 users: admin, user1, user2 (all with 2 billion VND balance)
- Creates 5 sample auctions (iPhone, MacBook, PS5, Apple Watch, iPad)
- Runs only once (checks if data exists)

SERVICE:
- CustomUserDetailsService.java: Spring Security UserDetailsService
- Load user by username for authentication

DEPENDENCIES:
✅ Entities (from commit 1)
✅ Repositories (from commit 1)

BUILD STATUS:
✅ mvn clean compile → SUCCESS

WHAT'S NEW:
✅ Spring Security configured
✅ Authentication endpoints ready
✅ Sample data will be created on first run
✅ Ready for service layer"

git push origin feature/user2-foundation
```

### **✅ Có thể test gì?**

```bash
# Build OK
mvn clean compile → ✅ SUCCESS

# Chạy server lần đầu
mvn spring-boot:run

# Kiểm tra console:
✅ "Started AuctionSystemApplication"
✅ "Created admin account: username=admin, password=admin123"
✅ "Created demo user: username=user1, password=123456"
✅ "Created sample auction: iPhone 15 Pro Max"

# Kiểm tra MySQL:
mysql -u root -p
use auction_db;
show tables;
# Should see: users, auctions, bids, watchlist

select * from users;
# Should see: admin, user1, user2

select * from auctions;
# Should see: 5 auctions
```

---

## 📦 **USER2 COMMIT 3: User Service & Controller**

### **🔸 Files cần commit:**

```
service/ (2 files)
├── UserService.java          → User business logic
└── WatchlistService.java     → Watchlist business logic (placeholder)

controller/ (2 files)
├── UserController.java       → /api/users/* endpoints
└── AuthController.java       → /api/auth/* (login, register)
```

### **🔧 CÁCH THỰC HIỆN:**

```bash
# Copy services
cp basic-project-user2/source/server/src/main/java/com/auction/service/UserService.java \
   source/server/src/main/java/com/auction/service/
cp basic-project-user2/source/server/src/main/java/com/auction/service/WatchlistService.java \
   source/server/src/main/java/com/auction/service/

# Copy controllers
mkdir -p source/server/src/main/java/com/auction/controller
cp basic-project-user2/source/server/src/main/java/com/auction/controller/UserController.java \
   source/server/src/main/java/com/auction/controller/
cp basic-project-user2/source/server/src/main/java/com/auction/controller/AuthController.java \
   source/server/src/main/java/com/auction/controller/

# Test
cd source/server
mvn clean compile

# Commit
git add source/server/src/main/java/com/auction/service/UserService.java
git add source/server/src/main/java/com/auction/service/WatchlistService.java
git add source/server/src/main/java/com/auction/controller/UserController.java
git add source/server/src/main/java/com/auction/controller/AuthController.java

git commit -m "feat(user): add user service and authentication

SERVICES:
- UserService.java: User business logic
  * Register new user with encrypted password
  * Update user profile
  * Manage user balance (add/subtract)
  * Check user balance

- WatchlistService.java: Watchlist business logic (foundation)
  * Will be implemented in next commit

CONTROLLERS:
- AuthController.java: Authentication endpoints
  * POST /api/auth/register - Register new user
  * POST /api/auth/login - Login with username/password
  * POST /api/auth/logout - Logout current user
  * Role-based response (ADMIN vs USER)

- UserController.java: User management endpoints
  * GET /api/users/{id} - Get user by ID
  * PUT /api/users/{id} - Update user profile
  * GET /api/users/{id}/balance - Get user balance

FEATURES:
✅ User registration with validation
✅ Login with Spring Security
✅ Password encryption (BCrypt)
✅ Session-based authentication
✅ User profile management
✅ Balance tracking

DEPENDENCIES:
✅ UserRepository (from commit 1)
✅ SecurityConfig (from commit 2)
✅ CustomUserDetailsService (from commit 2)

BUILD STATUS:
✅ mvn clean compile → SUCCESS

TEST:
✅ POST /api/auth/register → 201 Created
✅ POST /api/auth/login → 200 OK (returns user data with role)
✅ GET /api/users/1 → 200 OK (returns user profile)"

git push origin feature/user2-foundation
```

### **✅ Có thể test gì?**

```bash
# Start server
cd source/server
mvn spring-boot:run

# Test với curl hoặc Postman:

# 1. Register new user
curl -X POST http://127.0.0.1:8000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "123456",
    "fullName": "Test User"
  }'
# Expected: 201 Created

# 2. Login
curl -X POST http://127.0.0.1:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
# Expected: 200 OK with user data and role: ADMIN

# 3. Get user profile
curl http://127.0.0.1:8000/api/users/1
# Expected: 200 OK with user details
```

---

## 📦 **USER2 COMMIT 4: Watchlist Feature (Service + Controller)**

### **🔸 Files cần commit:**

```
service/ (update)
└── WatchlistService.java     → Complete implementation

controller/ (1 file)
└── WatchlistController.java  → /api/watchlist/* endpoints
```

**LƯU Ý:** WatchlistService đã được tạo ở commit 3 (placeholder), bây giờ update với implementation đầy đủ.

### **🔧 CÁCH THỰC HIỆN:**

```bash
# Update WatchlistService (overwrite với version đầy đủ)
cp basic-project-user2/source/server/src/main/java/com/auction/service/WatchlistService.java \
   source/server/src/main/java/com/auction/service/

# Copy WatchlistController
cp basic-project-user2/source/server/src/main/java/com/auction/controller/WatchlistController.java \
   source/server/src/main/java/com/auction/controller/

# Copy dependencies (nếu chưa có)
# WatchlistController cần AuctionController và BidController
cp basic-project-user2/source/server/src/main/java/com/auction/controller/AuctionController.java \
   source/server/src/main/java/com/auction/controller/ 2>/dev/null || true
cp basic-project-user2/source/server/src/main/java/com/auction/controller/BidController.java \
   source/server/src/main/java/com/auction/controller/ 2>/dev/null || true

# Copy services dependencies
cp basic-project-user2/source/server/src/main/java/com/auction/service/AuctionService.java \
   source/server/src/main/java/com/auction/service/ 2>/dev/null || true
cp basic-project-user2/source/server/src/main/java/com/auction/service/BidService.java \
   source/server/src/main/java/com/auction/service/ 2>/dev/null || true

# Test
cd source/server
mvn clean compile

# Commit
git add source/server/src/main/java/com/auction/service/WatchlistService.java
git add source/server/src/main/java/com/auction/controller/WatchlistController.java
git add source/server/src/main/java/com/auction/controller/AuctionController.java
git add source/server/src/main/java/com/auction/controller/BidController.java
git add source/server/src/main/java/com/auction/service/AuctionService.java
git add source/server/src/main/java/com/auction/service/BidService.java

git commit -m "feat(watchlist): add watchlist functionality with dependencies

WATCHLIST FEATURE:
- WatchlistService.java: Complete implementation
  * Add auction to watchlist
  * Remove auction from watchlist
  * Get user's watchlist
  * Check if auction is in watchlist
  * Prevent duplicate entries

- WatchlistController.java: Watchlist endpoints
  * POST   /api/watchlist - Add to watchlist
  * DELETE /api/watchlist/auction/{auctionId} - Remove from watchlist
  * GET    /api/watchlist/user - Get user's watchlist
  * GET    /api/watchlist/check/{auctionId} - Check if in watchlist

DEPENDENCIES ADDED:
- AuctionService.java: Auction business logic (needed for watchlist)
- BidService.java: Bidding logic (needed for auction details)
- AuctionController.java: /api/auctions/* endpoints
- BidController.java: /api/bids/* endpoints

Note: These dependencies are required for watchlist to display
auction details and will be used by User1 for core bidding features.

FEATURES:
✅ Add/remove auctions from watchlist
✅ View watchlist with auction details
✅ Prevent duplicate watchlist entries
✅ Check watchlist status for any auction

BUILD STATUS:
✅ mvn clean compile → SUCCESS

TEST:
✅ POST /api/watchlist (userId=1, auctionId=1) → 201 Created
✅ GET /api/watchlist/user?userId=1 → 200 OK (list of auctions)
✅ DELETE /api/watchlist/auction/1?userId=1 → 200 OK
✅ GET /api/watchlist/check/1?userId=1 → 200 OK (true/false)"

git push origin feature/user2-foundation
```

---

## 📦 **USER2 COMMIT 5: Frontend (Watchlist UI + Shared)**

### **🔸 Files cần commit:**

```
📂 source/client/public/

Pages:
├── watchlist.html        → Watchlist page
├── index.html            → Landing page
├── login.html            → Login page
├── register.html         → Register page
└── dashboard.html        → User dashboard (shared với User1)

JavaScript:
js/
├── config.js            → API config
├── auth.js              → Authentication utils
├── header.js            → Shared header component
├── main.js              → Landing page logic
└── dashboard.js         → Dashboard logic (shared với User1)

CSS:
css/
└── style.css            → Shared styles
```

### **🔧 CÁCH THỰC HIỆN:**

```bash
# Copy frontend files
mkdir -p source/client/public/js
mkdir -p source/client/public/css

# Copy HTML pages
cp basic-project-user2/source/client/public/watchlist.html source/client/public/
cp basic-project-user2/source/client/public/index.html source/client/public/
cp basic-project-user2/source/client/public/login.html source/client/public/
cp basic-project-user2/source/client/public/register.html source/client/public/
cp basic-project-user2/source/client/public/dashboard.html source/client/public/

# Copy JavaScript
cp basic-project-user2/source/client/public/js/*.js source/client/public/js/

# Copy CSS
cp basic-project-user2/source/client/public/css/*.css source/client/public/css/

# Commit
git add source/client/public/

git commit -m "feat(frontend): add user interface for watchlist and authentication

PAGES:
- watchlist.html: User watchlist page
  * Display user's watchlist with auction cards
  * Remove from watchlist button
  * Real-time auction status
  * Empty state message

- index.html: Landing page
  * Hero section with call-to-action
  * Featured auctions
  * How it works section
  * Footer with links

- login.html: Login page
  * Username/password form
  * Role-based redirect (ADMIN → admin panel, USER → dashboard)
  * Error handling and validation
  * Link to register page

- register.html: Registration page
  * User registration form
  * Form validation
  * Success message and redirect
  * Link to login page

- dashboard.html: User dashboard (shared)
  * Will show auction list (User1 will implement logic)
  * Header with navigation
  * Logout button

JAVASCRIPT:
- config.js: API configuration
  * API_BASE_URL: http://127.0.0.1:8000
  * WebSocket URL
  * Helper functions

- auth.js: Authentication utilities
  * Auth object with isLoggedIn(), getUserId(), etc.
  * requireAuth() for protected pages
  * Logout functionality

- header.js: Shared header component
  * Navigation menu
  * User profile dropdown
  * Balance display
  * Logout button

- main.js: Landing page logic
  * Featured auctions carousel
  * Smooth scrolling
  * CTA buttons

- dashboard.js: Dashboard logic (placeholder for User1)
  * Will be implemented by User1

CSS:
- style.css: Shared styles
  * Bootstrap customization
  * Custom components
  * Responsive design

FEATURES:
✅ Complete watchlist UI
✅ User authentication pages
✅ Landing page for marketing
✅ Shared components (header, auth)
✅ Responsive design (Bootstrap 5)
✅ Real-time updates ready (WebSocket)

DEPENDENCIES:
✅ Backend APIs (from commits 3, 4)
✅ Bootstrap 5 (CDN)
✅ Bootstrap Icons (CDN)

TEST:
✅ Open http://127.0.0.1:5500/login.html → Login page works
✅ Login as user1/123456 → Redirect to dashboard
✅ Navigate to watchlist → Watchlist page loads
✅ Add auction to watchlist → Works
✅ Remove from watchlist → Works"

git push origin feature/user2-foundation
```

---

## 🎉 **HOÀN THÀNH USER2 - 5 COMMITS**

```
✅ Commit 1: Foundation (Entities, DTOs, Repositories)
✅ Commit 2: Configuration (Security, Data Initializer)
✅ Commit 3: User Service & Authentication
✅ Commit 4: Watchlist Feature
✅ Commit 5: Frontend UI

Sẵn sàng merge vào main!
```

---

## 🔀 **MERGE VÀO MAIN**

```bash
# Review tất cả commits
git log --oneline

# Tạo Pull Request hoặc merge trực tiếp
git checkout main
git merge feature/user2-foundation

# Tag version
git tag -a v0.1-user2-complete -m "User2 foundation complete"

# Push
git push origin main --tags
```

---

## 📊 **TÓM TẮT**

| Commit | Files | Có thể test gì? |
|--------|-------|-----------------|
| **1. Foundation** | 16 files (entities, DTOs, repos, pom.xml) | mvn compile → OK |
| **2. Configuration** | 5 files (configs, CustomUserDetailsService) | mvn spring-boot:run → DB tables created |
| **3. User Service** | 4 files (UserService, controllers) | POST /api/auth/login → OK |
| **4. Watchlist** | 6 files (WatchlistService, controllers + deps) | POST /api/watchlist → OK |
| **5. Frontend** | ~15 files (HTML, JS, CSS) | Browser UI works |

**Total:** 5 commits, ~46 files, Foundation hoàn chỉnh cho User1 và Admin! 🚀
