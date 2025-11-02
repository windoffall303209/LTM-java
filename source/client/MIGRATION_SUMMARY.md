● 📋 KẾ HOẠCH CHIA VIỆC CHO 3 NGƯỜI

🎯 BƯỚC 1: PUSH CÁC FILE CƠ BẢN (FOUNDATION) LÊN GIT TRƯỚC

Đây là các file BẮT BUỘC cần có để project có thể chạy được cơ bản:

A. Configuration & Setup Files:

```
  📦 ROOT
  ├── source/server/pom.xml                          # Maven dependencies
  ├── source/server/src/main/resources/
  │   └── application.properties                     # Database config, server port
  ├── source/client/package.json                     # NPM dependencies
  ├── .gitignore                                     # Git ignore rules
  └── README.md                                      # Project documentation

  B. Backend Core Files:

  📦 Backend Foundation
  ├── AuctionSystemApplication.java                  # Spring Boot entry point
  │
  ├── config/
  │   ├── SecurityConfig.java                        # Security & authentication
  │   ├── WebConfig.java                            # CORS configuration
  │   ├── WebSocketConfig.java                      # WebSocket setup
  │   └── DataInitializer.java                      # Initial data (admin/user accounts)
  │
  ├── model/
  │   ├── User.java                                 # User entity (ADMIN/USER roles)
  │   └── Auction.java                              # Auction entity
  │
  ├── repository/
  │   ├── UserRepository.java                       # User database access
  │   └── AuctionRepository.java                    # Auction database access
  │
  ├── dto/
  │   ├── ApiResponse.java                          # Standard
  │   ├── UserDTO.java
  │   ├── AuctionDTO.java
  │   ├── LoginRequest.java
  │   └── RegisterRequest.java
  │
  └── service/
      ├── CustomUserDetailsService.java             # Spring Security user details
      └── (Interfaces/basic structure)

  C. Frontend Core Files:

  📦 Frontend Foundation
  ├── public/
  │   ├── index.html                                # Landing page
  │   ├── login.html                                # Login page
  │   ├── register.html                             # Register page
  │   │
  │   ├── css/
  │   │   └── style.css                             # Shared styles
  │   │
  │   └── js/
  │       ├── config.js                             # API endpoints config
  │       └── auth.js                               # Authentication utilities
```

---

👥 BƯỚC 2: CHIA VIỆC CHO 3 NGƯỜI

👤 NGƯỜI 1: ADMIN FEATURES (Quản lý hệ thống)

Branch: feature/admin-panel

Nhiệm vụ:

- Backend:
  - AdminController.java - API endpoints cho admin
  - Các methods admin trong UserService.java và AuctionService.java
- Frontend:
  - admin/dashboard.html - Trang dashboard admin
  - admin/auctions.html - Quản lý đấu giá
  - admin/users.html - Quản lý người dùng
  - js/admin-header.js - Header component

Chức năng cần implement:

- ✅ CRUD đấu giá (Create, Update, Delete)
- ✅ Start/End đấu giá manually
- ✅ Quản lý users (ban/unban)
- ✅ Cập nhật số dư tài khoản
- ✅ Xem thống kê hệ thống

---

👤 NGƯỜI 2: USER FEATURES (Tính năng người dùng)

Branch: feature/user-bidding

Nhiệm vụ:

- Backend:
  - BidController.java - API đấu giá
  - BidService.java - Logic đấu giá
  - WatchlistController.java - API watchlist
  - WatchlistService.java - Logic watchlist
  - model/Bid.java - Entity
  - model/Watchlist.java - Entity
  - repository/BidRepository.java
  - repository/WatchlistRepository.java
  - dto/BidDTO.java, BidRequest.java, WatchlistDTO.java
- Frontend:
  - my-bids.html - Lịch sử đấu giá
  - watchlist.html - Danh sách theo dõi
  - js/header.js - User header

Chức năng cần implement:

- ✅ Đặt giá (place bid)
- ✅ Xem lịch sử đấu giá của mình
- ✅ Thêm/xóa watchlist
- ✅ Kiểm tra số dư trước khi đấu giá

---

👤 NGƯỜI 3: SHARED FEATURES (Tính năng chung)

Branch: feature/core-functionality

Nhiệm vụ:

- Backend:
  - AuthController.java - Login/Register/Logout
  - UserController.java - Thông tin user
  - AuctionController.java - Xem đấu giá
  - UserService.java - User management
  - AuctionService.java - Auction management
  - AuctionSchedulerService.java - Auto-end auctions
  - WebSocketController.java - Real-time updates
- Frontend:
  - dashboard.html - Dashboard chính
  - auction-detail.html - Chi tiết đấu giá
  - js/main.js - Login/Register logic
  - js/dashboard.js - Dashboard logic
  - js/auction.js - Auction detail + WebSocket

Chức năng cần implement:

- ✅ Authentication (login/register/logout)
- ✅ Xem danh sách đấu giá
- ✅ Xem chi tiết đấu giá
- ✅ Tìm kiếm đấu giá
- ✅ WebSocket real-time updates
- ✅ Auto-extend auction
- ✅ Scheduled tasks

---

🔄 BƯỚC 3: GIT WORKFLOW

# 1. Người chịu trách nhiệm chính (Project Lead) push foundation code

git checkout main # hoặc master
git add [các file foundation ở trên]
git commit -m "chore: setup project foundation with core models, config, and structure"
git push origin main

# 2. Mỗi người pull code về và tạo branch riêng

git pull origin main

# Người 1:

git checkout -b feature/admin-panel

# Người 2:

git checkout -b feature/user-bidding

# Người 3:

git checkout -b feature/core-functionality

# 3. Làm việc trên branch của mình

git add .
git commit -m "feat: implement [tên chức năng]"
git push origin [tên-branch]

# 4. Thường xuyên sync với main để tránh conflict

git checkout main
git pull origin main
git checkout [tên-branch]
git merge main # hoặc git rebase main

# 5. Khi hoàn thành, tạo Pull Request để merge vào main

---

⚠️ LƯU Ý QUAN TRỌNG:

1. Dependencies giữa các tasks:


    - Người 3 nên hoàn thành AuthController và AuctionService trước
    - Người 2 cần AuctionService đã có sẵn để implement bidding
    - Người 1 có thể làm độc lập nhất

2. Files có thể conflict:


    - SecurityConfig.java - Cần phối hợp về permissions
    - application.properties - Merge cẩn thận
    - style.css - Có thể conflict nếu cùng sửa
    - DataInitializer.java - Nếu cùng thêm test data

3. Chiến lược merge:


    - Merge theo thứ tự: Người 3 → Người 2 → Người 1
    - Vì Người 3 làm core, Người 2 depend on core, Người 1 depend on cả 2

4. Communication:


    - Họp ngắn hàng ngày để sync progress
    - Thông báo trước khi sửa shared files
    - Document API endpoints để người khác biết format

---

📝 DANH SÁCH FILE FOUNDATION CẦN PUSH TRƯỚC:

Bạn có muốn tôi tạo một file markdown chi tiết liệt kê CHÍNH XÁC tất cả các file path cần push không? Điều này sẽ
giúp bạn dễ dàng git add từng file một cách có tổ chức.
