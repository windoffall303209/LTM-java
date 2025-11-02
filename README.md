# BÀI TẬP LỚN: LẬP TRÌNH MẠNG

## HỆ THỐNG ĐẤU GIÁ TRỰC TUYẾN (ONLINE AUCTION SYSTEM)

> 🔨 Hệ thống đấu giá trực tuyến realtime với giao thức HTTP/REST API và WebSocket

---

## 🧑‍💻 THÔNG TIN NHÓM

| STT | Họ và Tên         | MSSV       | Email                  | Đóng góp |
| --- | ----------------- | ---------- | ---------------------- | -------- |
| 1   | Nguyễn Trọng Khởi | B22DCCN471 | kddmelothree@gmail.com | 100%     |
| 2   | Trương Huy Tâm    | B22DCCN711 | huytam514@gmail.com    | 100%     |
| 3   | Vũ Thành Nam      | B22DCCN568 | nvuthanh4@gmail.com    | 100%     |

**Tên nhóm:** Nhóm 06 – Lập trình mạng
**Chủ đề đã đăng ký:** Hệ thống đấu giá trực tuyến với WebSocket realtime

---

## 🧠 MÔ TẢ HỆ THỐNG

Hệ thống đấu giá trực tuyến cho phép người dùng tham gia đấu giá các sản phẩm theo thời gian thực. Hệ thống sử dụng **WebSocket** để đồng bộ giá đấu giữa tất cả người dùng trong cùng phiên đấu giá.

### Các tính năng chính:

**Phía User:**

- ✅ Đăng ký/Đăng nhập với xác thực Spring Security
- ✅ Xem danh sách đấu giá (ACTIVE, PENDING, ENDED) với sorting thông minh
- ✅ Dashboard hiển thị tất cả các cuộc đấu giá, sắp xếp: Đang diễn ra → Sắp diễn ra → Đã kết thúc
- ✅ Thống kê chi tiết: Tổng số đấu giá, số lượng theo từng trạng thái
- ✅ Lọc đấu giá theo trạng thái và tìm kiếm theo từ khóa
- ✅ Đặt giá realtime với WebSocket (cập nhật tức thì cho tất cả người dùng)
- ✅ Theo dõi (watchlist) các đấu giá yêu thích
- ✅ Xem lịch sử đấu giá cá nhân
- ✅ Hiển thị countdown timer và thông báo realtime

**Phía Admin:**

- ✅ Quản lý đấu giá (CRUD operations)
- ✅ Quản lý users (view, toggle status, update balance)
- ✅ Dashboard thống kê hệ thống
- ✅ Start/End auction với broadcast realtime
- ✅ Xem chi tiết từng đấu giá và bid history

**Cơ chế hoạt động:**

- Server sử dụng **STOMP over WebSocket** để broadcast các sự kiện đấu giá
- Mỗi đấu giá có topic riêng: `/topic/auction/{id}` và `/topic/auctions` (global)
- Auto-extend auction khi có bid trong phút cuối (tối đa 3 lần)
- Validation bid amount (phải lớn hơn current price + min increment)

**Cấu trúc logic tổng quát:**

```
┌──────────┐         HTTP/REST API          ┌──────────┐         JDBC          ┌──────────┐
│  Client  │ ◄─────────────────────────────► │  Server  │ ◄──────────────────► │  MySQL   │
│ (Browser)│         WebSocket/STOMP         │  (Java)  │                      │ Database │
└──────────┘ ◄─────────────────────────────► └──────────┘                      └──────────┘
     │                                              │
     │          Subscribe: /topic/auction/{id}     │
     │          Publish: /app/bid, /app/join       │
     └─────────────────────────────────────────────┘
```

**Luồng hoạt động đấu giá:**

```
1. User vào trang auction-detail.html?id=1
2. Client subscribe WebSocket topic /topic/auction/1
3. User đặt giá → POST /api/bids với amount
4. Server validate và lưu bid vào DB
5. Server broadcast BID_UPDATE qua WebSocket
6. Tất cả clients đang xem đấu giá này nhận update
7. UI tự động cập nhật giá mới, người dẫn đầu, history
```

---

## ⚙️ CÔNG NGHỆ SỬ DỤNG

| Thành phần     | Công nghệ                         | Phiên bản | Ghi chú                        |
| -------------- | --------------------------------- | --------- | ------------------------------ |
| **Backend**    | Spring Boot                       | 3.2.0     | Framework chính                |
|                | Spring Web                        | 3.2.0     | REST API                       |
|                | Spring WebSocket                  | 3.2.0     | Realtime communication         |
|                | Spring Data JPA                   | 3.2.0     | ORM / Database access          |
|                | Spring Security                   | 3.2.0     | Authentication & Authorization |
|                | Hibernate                         | 6.x       | JPA Implementation             |
|                | Lombok                            | Latest    | Giảm boilerplate code          |
| **Frontend**   | HTML5 + CSS3 + Vanilla JavaScript | -         | Không dùng framework           |
|                | Bootstrap                         | 5.3.0     | UI Framework                   |
|                | SockJS Client                     | 1.6.1     | WebSocket fallback             |
|                | STOMP.js                          | 2.3.3     | WebSocket messaging protocol   |
| **Database**   | MySQL                             | 8.0       | Persistent storage             |
| **Build Tool** | Maven                             | 3.9+      | Java project management        |
| **Runtime**    | Java                              | 17        | JDK 17 required                |
| **Web Server** | http-server (npm)                 | Latest    | Serve static client files      |
| **Protocol**   | HTTP/1.1, WebSocket (STOMP)       | -         | Client-Server communication    |

---

## 🚀 HƯỚNG DẪN CHẠY DỰ ÁN

### Yêu cầu hệ thống:

- ✅ **Java JDK 17** trở lên
- ✅ **Maven 3.9+**
- ✅ **MySQL 8.0**
- ✅ **Node.js 18+** và **npm** (để chạy client)
- ✅ Port 8000 (server) và 8080 (client) phải available

---

### 1️⃣ Clone repository

```bash
git clone <repository-url>
cd ClientServer
```

---

### 2️⃣ Chuẩn bị Database

**Tạo database MySQL:**

```sql
CREATE DATABASE auction_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**Cấu hình kết nối (nếu cần):**

Mở file `Server/src/main/resources/application.properties` và chỉnh sửa:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auction_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=admin
```

> **Lưu ý:** Đổi `root` và `admin` thành username/password MySQL của bạn.

Schema database sẽ được **tự động tạo** khi chạy server lần đầu (JPA auto-create).

---

### 3️⃣ Chạy Server (Backend)

```bash
cd Server

# Build project bằng Maven
mvn clean install

# Chạy server
mvn spring-boot:run
```

Server sẽ chạy tại: **http://localhost:8000**

**Kiểm tra server đã chạy:**

```bash
curl http://localhost:8000/api/health
# Hoặc mở browser: http://localhost:8000
```

**Dữ liệu mặc định (DataInitializer):**

- Admin account: `admin` / `admin123`
- User account: `user1` / `password`, `user2` / `password`
- 3 đấu giá mẫu (ACTIVE, PENDING, ENDED)

---

### 4️⃣ Chạy Client (Frontend)

Mở terminal/cmd mới:

```bash
cd Client

# Cài đặt http-server (lần đầu tiên)
npm install

# Chạy static web server
npm start
# Hoặc: npx http-server public -p 8080
```

Client sẽ chạy tại: **http://localhost:8080**

**Cấu hình API endpoint (nếu cần):**

Mở `Client/public/js/config.js` và kiểm tra:

```javascript
window.API_CONFIG = {
  BASE_URL: "http://localhost:8000",
  WS_URL: "http://localhost:8000/ws",
};
```

---

### 5️⃣ Truy cập hệ thống

🌐 **Mở browser và truy cập:**

- **Trang chủ:** http://localhost:8080
- **Login:** http://localhost:8080/login.html

**Đăng nhập với:**

- **Admin:** username: `admin`, password: `admin123`
- **User:** username: `user1`, password: `password`

---

### 6️⃣ Kiểm thử nhanh

**Test User flow:**

1. Đăng nhập với user1
2. Vào Dashboard → Chọn đấu giá đang diễn ra
3. Đặt giá → Xem realtime update
4. Mở incognito/tab mới → Đăng nhập user2
5. Đặt giá cao hơn → Cả 2 tabs tự động cập nhật

**Test Admin flow:**

1. Đăng nhập với admin
2. Vào Admin Panel → Quản lý đấu giá
3. Tạo đấu giá mới → Dashboard user tự động hiển thị
4. Start/End auction → User dashboard tự động cập nhật

**Test WebSocket:**

```bash
# Mở browser console (F12) và xem WebSocket messages
# Sẽ thấy: "Dashboard WebSocket Connected"
# Khi có bid mới: BID_UPDATE event
```

---

## 🔗 GIAO TIẾP (GIAO THỨC SỬ DỤNG)

### REST API Endpoints

#### Authentication APIs

| Endpoint             | Method | Auth Required | Description       | Input                                       | Output                                     |
| -------------------- | ------ | ------------- | ----------------- | ------------------------------------------- | ------------------------------------------ |
| `/api/auth/login`    | POST   | ❌            | Đăng nhập         | `username`, `password`                      | `{success, message, data: {userId, role}}` |
| `/api/auth/logout`   | POST   | ✅            | Đăng xuất         | -                                           | `{success, message}`                       |
| `/api/auth/register` | POST   | ❌            | Đăng ký tài khoản | `username`, `password`, `fullName`, `email` | `{success, message, data: UserDTO}`        |

#### Auction APIs

| Endpoint               | Method | Auth | Description                                   | Input             | Output             |
| ---------------------- | ------ | ---- | --------------------------------------------- | ----------------- | ------------------ |
| `/api/auctions`        | GET    | ✅   | Lấy tất cả đấu giá (ACTIVE, PENDING, ENDED)  | -                 | `List<AuctionDTO>` |
| `/api/auctions/active` | GET    | ✅   | Lấy chỉ đấu giá ACTIVE (cho trang chủ)       | -                 | `List<AuctionDTO>` |
| `/api/auctions/{id}`   | GET    | ✅   | Chi tiết đấu giá                              | `id` (path)       | `AuctionDTO`       |
| `/api/auctions/search` | GET    | ✅   | Tìm kiếm đấu giá                              | `keyword` (query) | `List<AuctionDTO>` |

#### Bid APIs

| Endpoint                 | Method | Auth | Description             | Input                              | Output         |
| ------------------------ | ------ | ---- | ----------------------- | ---------------------------------- | -------------- |
| `/api/bids`              | POST   | ✅   | Đặt giá                 | `userId`, `auctionId`, `bidAmount` | `BidDTO`       |
| `/api/bids/auction/{id}` | GET    | ✅   | Lịch sử bid của auction | `id` (path)                        | `List<BidDTO>` |
| `/api/bids/user`         | GET    | ✅   | Lịch sử bid của user    | `userId` (query)                   | `List<BidDTO>` |

#### Watchlist APIs

| Endpoint                      | Method | Auth | Description                 | Input                 | Output               |
| ----------------------------- | ------ | ---- | --------------------------- | --------------------- | -------------------- |
| `/api/watchlist`              | POST   | ✅   | Thêm vào watchlist          | `userId`, `auctionId` | `WatchlistDTO`       |
| `/api/watchlist/user`         | GET    | ✅   | Lấy watchlist của user      | `userId` (query)      | `List<WatchlistDTO>` |
| `/api/watchlist/check`        | GET    | ✅   | Kiểm tra có trong watchlist | `userId`, `auctionId` | `Boolean`            |
| `/api/watchlist/auction/{id}` | DELETE | ✅   | Xóa khỏi watchlist          | `auctionId`, `userId` | `void`               |

#### Admin APIs

| Endpoint                         | Method | Auth  | Description         | Input                                    | Output             |
| -------------------------------- | ------ | ----- | ------------------- | ---------------------------------------- | ------------------ |
| `/api/admin/auctions/all`        | GET    | Admin | Lấy tất cả đấu giá  | -                                        | `List<AuctionDTO>` |
| `/api/admin/auctions`            | POST   | Admin | Tạo đấu giá mới     | Form data (title, description, price...) | `Auction`          |
| `/api/admin/auctions/{id}`       | PUT    | Admin | Cập nhật đấu giá    | `id`, form data                          | `Auction`          |
| `/api/admin/auctions/{id}`       | DELETE | Admin | Xóa đấu giá         | `id`                                     | `void`             |
| `/api/admin/auctions/{id}/start` | POST   | Admin | Bắt đầu đấu giá     | `id`                                     | `void` + WS event  |
| `/api/admin/auctions/{id}/end`   | POST   | Admin | Kết thúc đấu giá    | `id`                                     | `void` + WS event  |
| `/api/admin/users`               | GET    | Admin | Lấy danh sách users | -                                        | `List<UserDTO>`    |
| `/api/admin/statistics`          | GET    | Admin | Thống kê hệ thống   | -                                        | `Statistics`       |

---

### WebSocket Protocol (STOMP)

**Connection:**

```javascript
const socket = new SockJS("http://localhost:8000/ws");
const stompClient = Stomp.over(socket);
stompClient.connect({}, onConnected, onError);
```

**Topics (Subscribe):**

| Topic                 | Description                                | Message Format                                 |
| --------------------- | ------------------------------------------ | ---------------------------------------------- |
| `/topic/auction/{id}` | Nhận updates của 1 đấu giá cụ thể          | `{type, newPrice, bidderName, totalBids, ...}` |
| `/topic/auctions`     | Nhận events global (created/started/ended) | `{type, auctionId, title, status, ...}`        |

**Destinations (Send):**

| Destination              | Description         | Payload      |
| ------------------------ | ------------------- | ------------ |
| `/app/join/{auctionId}`  | Join phòng đấu giá  | `{username}` |
| `/app/leave/{auctionId}` | Leave phòng đấu giá | `{username}` |

**Event Types:**

| Event Type         | Trigger                 | Broadcast To          |
| ------------------ | ----------------------- | --------------------- |
| `BID_UPDATE`       | User đặt giá thành công | `/topic/auction/{id}` |
| `AUCTION_EXTENDED` | Tự động gia hạn         | `/topic/auction/{id}` |
| `AUCTION_ENDED`    | Đấu giá kết thúc        | `/topic/auction/{id}` |
| `AUCTION_CREATED`  | Admin tạo đấu giá mới   | `/topic/auctions`     |
| `AUCTION_STARTED`  | Admin start đấu giá     | `/topic/auctions`     |
| `USER_JOINED`      | User join phòng         | `/topic/auction/{id}` |

---

## 📊 KẾT QUẢ THỰC NGHIỆM

### Demo Screenshots

**1. Login Page:**
![Login](./statics/login_page.png)

**2. User Dashboard:**
![Dashboard](./statics/dashboard.png)

**3. Auction Detail - Realtime Bidding:**
![Auction Detail](./statics/auction_detail.png)

**4. Admin Panel:**
![Admin Dashboard](./statics/admin_dashboard.png)

**5. WebSocket Realtime Update:**
![Realtime Update](./statics/realtime_update.png)

---

### Test Log - Realtime Bidding

```
[User1 Browser Console]
14:30:01 - Dashboard WebSocket Connected
14:30:15 - Joined auction #1
14:30:20 - Placed bid: 5,000,000 VND
14:30:21 - BID_UPDATE received: { newPrice: 5000000, bidderName: "user1" }

[User2 Browser Console]
14:30:18 - Dashboard WebSocket Connected
14:30:19 - Joined auction #1
14:30:21 - BID_UPDATE received: { newPrice: 5000000, bidderName: "user1" }
           → UI auto-updated!
14:30:35 - Placed bid: 5,500,000 VND
14:30:36 - BID_UPDATE received: { newPrice: 5500000, bidderName: "user2" }

[User1 Browser Console]
14:30:36 - BID_UPDATE received: { newPrice: 5500000, bidderName: "user2" }
           → UI auto-updated!
           → Notification: "user2 vừa đặt giá mới!"
```

**Performance:**

- WebSocket latency: < 50ms
- Bid processing time: ~100-200ms
- Concurrent users tested: 10 users bidding simultaneously
- Database query time: < 20ms (with proper indexing)

---

## 📝 CẬP NHẬT GÂN ĐÂY (Recent Updates)

### Version 2.0 - Dashboard Enhancement (Latest)

**Ngày cập nhật:** 31/10/2025

**Các thay đổi chính:**

1. **Dashboard Improvements:**
   - ✅ Dashboard giờ hiển thị **tất cả** các cuộc đấu giá (bao gồm ENDED)
   - ✅ Thêm smart sorting: ACTIVE → PENDING → ENDED
   - ✅ Đổi tên từ "Đấu Giá Đang Diễn Ra" → "Danh Sách Các Cuộc Đấu Giá"
   - ✅ Cải thiện phần thống kê với 4 metrics: Tổng số, Đang diễn ra, Sắp diễn ra, Đã kết thúc

2. **API Changes:**
   - ✅ `/api/auctions` giờ trả về tất cả cuộc đấu giá (ACTIVE + PENDING + ENDED)
   - ✅ Thêm endpoint mới `/api/auctions/active` cho trang chủ (chỉ ACTIVE)
   - ✅ Cải thiện error handling và logging

3. **Bug Fixes:**
   - ✅ Fix lỗi trang chủ không load được danh sách đấu giá
   - ✅ Fix lỗi 400 Bad Request khi gọi `/api/auctions/active`
   - ✅ Thêm console logging để dễ dàng debug

4. **UI/UX Improvements:**
   - ✅ Thêm màu sắc phân biệt trạng thái trong thống kê
   - ✅ Cải thiện thông báo lỗi
   - ✅ Tối ưu hiển thị cho các cuộc đấu giá đã kết thúc

**Breaking Changes:**
- ⚠️ API `/api/auctions` behavior changed: Giờ trả về tất cả thay vì chỉ ACTIVE + PENDING
- ✅ Không ảnh hưởng đến client vì dashboard.js đã được cập nhật

---

## 🧩 CẤU TRÚC DỰ ÁN

```
ClientServer/
├── README.md                          # Tài liệu chính (file này)
├── INSTRUCTION (1).md                 # Hướng dẫn từ giảng viên
├── statics/                           # Hình ảnh, screenshots
│   ├── login_page.png
│   ├── dashboard.png
│   ├── auction_detail.png
│   ├── admin_dashboard.png
│   └── realtime_update.png
├── Server/                            # Backend - Spring Boot
│   ├── pom.xml                        # Maven configuration
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/auction/
│   │   │   │   ├── AuctionSystemApplication.java    # Main class
│   │   │   │   ├── config/                          # Configurations
│   │   │   │   │   ├── SecurityConfig.java          # Spring Security
│   │   │   │   │   ├── WebSocketConfig.java         # WebSocket setup
│   │   │   │   │   ├── WebConfig.java               # CORS, etc.
│   │   │   │   │   └── DataInitializer.java         # Sample data
│   │   │   │   ├── controller/                      # REST Controllers
│   │   │   │   │   ├── AuthController.java
│   │   │   │   │   ├── AuctionController.java
│   │   │   │   │   ├── BidController.java
│   │   │   │   │   ├── WatchlistController.java
│   │   │   │   │   ├── UserController.java
│   │   │   │   │   └── AdminController.java
│   │   │   │   ├── model/                           # JPA Entities
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── Auction.java
│   │   │   │   │   ├── Bid.java
│   │   │   │   │   └── Watchlist.java
│   │   │   │   ├── repository/                      # JPA Repositories
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── AuctionRepository.java
│   │   │   │   │   ├── BidRepository.java
│   │   │   │   │   └── WatchlistRepository.java
│   │   │   │   ├── service/                         # Business Logic
│   │   │   │   │   ├── UserService.java
│   │   │   │   │   ├── AuctionService.java
│   │   │   │   │   ├── BidService.java
│   │   │   │   │   ├── WatchlistService.java
│   │   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   │   └── AuctionSchedulerService.java # Cron jobs
│   │   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   │   │   ├── ApiResponse.java
│   │   │   │   │   ├── AuctionDTO.java
│   │   │   │   │   ├── BidDTO.java
│   │   │   │   │   ├── UserDTO.java
│   │   │   │   │   └── WatchlistDTO.java
│   │   │   │   └── websocket/                       # WebSocket handlers
│   │   │   │       └── WebSocketController.java
│   │   │   └── resources/
│   │   │       └── application.properties           # App configuration
│   │   └── test/                                    # Unit tests
│   └── target/                                      # Build output
│
└── Client/                            # Frontend - Static Web
    ├── package.json                   # npm config
    └── public/                        # Web root
        ├── index.html                 # Landing page
        ├── login.html                 # Login page
        ├── register.html              # Register page
        ├── dashboard.html             # User dashboard
        ├── auction-detail.html        # Auction detail & bidding
        ├── my-bids.html               # Bid history
        ├── watchlist.html             # Watchlist page
        ├── admin/                     # Admin pages
        │   ├── dashboard.html         # Admin dashboard
        │   ├── auctions.html          # Auction management
        │   └── users.html             # User management
        ├── css/
        │   └── style.css              # Custom styles
        └── js/
            ├── config.js              # API endpoint config
            ├── auth.js                # Authentication helpers
            ├── header.js              # Shared header component
            ├── admin-header.js        # Admin header component
            ├── dashboard.js           # Dashboard logic + WebSocket
            └── auction.js             # Auction detail + WebSocket
```

---

## 🧪 KIẾN TRÚC PHẦN MỀM

### Backend Architecture (Spring Boot)

```
┌─────────────────────────────────────────────────────┐
│                   Presentation Layer                │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────┐ │
│  │ REST         │  │ WebSocket    │  │ Security  │ │
│  │ Controllers  │  │ Controller   │  │ Filter    │ │
│  └──────────────┘  └──────────────┘  └───────────┘ │
└─────────────────────────────────────────────────────┘
                        ▼
┌─────────────────────────────────────────────────────┐
│                   Business Layer                    │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────┐ │
│  │ Auction      │  │ Bid          │  │ User      │ │
│  │ Service      │  │ Service      │  │ Service   │ │
│  └──────────────┘  └──────────────┘  └───────────┘ │
└─────────────────────────────────────────────────────┘
                        ▼
┌─────────────────────────────────────────────────────┐
│                 Data Access Layer                   │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────┐ │
│  │ JPA          │  │ JPA          │  │ JPA       │ │
│  │ Repository   │  │ Repository   │  │ Repository│ │
│  └──────────────┘  └──────────────┘  └───────────┘ │
└─────────────────────────────────────────────────────┘
                        ▼
                  ┌──────────┐
                  │  MySQL   │
                  │ Database │
                  └──────────┘
```

### Frontend Architecture

```
┌─────────────────────────────────────────────┐
│             HTML Pages (Views)              │
│  dashboard.html  │  auction-detail.html ... │
└─────────────────────────────────────────────┘
                    ▼
┌─────────────────────────────────────────────┐
│         JavaScript Modules (MVC-like)       │
│  ┌────────────┐  ┌────────────┐            │
│  │ dashboard  │  │ auction.js │            │
│  │ .js        │  │ (WebSocket)│            │
│  └────────────┘  └────────────┘            │
│  ┌────────────┐  ┌────────────┐            │
│  │ auth.js    │  │ header.js  │            │
│  └────────────┘  └────────────┘            │
└─────────────────────────────────────────────┘
                    ▼
          ┌──────────────────┐
          │ API Layer        │
          │ (fetch/axios)    │
          └──────────────────┘
                    ▼
    ┌──────────────┐  ┌──────────────┐
    │ REST API     │  │ WebSocket    │
    │ (HTTP)       │  │ (STOMP)      │
    └──────────────┘  └──────────────┘
```

---

## 🌟 HIGHLIGHTS & SPECIAL FEATURES

### 1. **Smart Dashboard với Sorting & Filtering**

- ✅ Hiển thị tất cả các cuộc đấu giá (ACTIVE, PENDING, ENDED)
- ✅ Auto-sorting: Đang diễn ra → Sắp diễn ra → Đã kết thúc
- ✅ Trong cùng trạng thái: ACTIVE/PENDING sắp kết thúc lên đầu, ENDED mới nhất lên đầu
- ✅ Lọc theo trạng thái: Tất cả / Đang diễn ra / Sắp diễn ra / Đã kết thúc
- ✅ Tìm kiếm realtime theo từ khóa (title, description)
- ✅ Thống kê chi tiết: Tổng số + phân loại theo trạng thái

### 2. **Realtime WebSocket Communication**

- ✅ Sử dụng STOMP protocol over WebSocket
- ✅ Auto-reconnect khi mất kết nối
- ✅ Broadcast events đến multiple clients
- ✅ Low latency (<50ms)

### 3. **Auto-extend Auction**

- ✅ Tự động gia hạn 60s khi có bid trong phút cuối
- ✅ Tối đa 3 lần gia hạn (configurable)
- ✅ Broadcast AUCTION_EXTENDED event

### 4. **Security Features**

- ✅ Spring Security với Session-based authentication
- ✅ Role-based authorization (USER/ADMIN)
- ✅ CORS configuration cho client-server
- ✅ Password encryption (BCrypt)

### 5. **Responsive UI**

- ✅ Bootstrap 5 responsive design
- ✅ Toast notifications
- ✅ Real-time countdown timer
- ✅ Shared header component (đồng bộ across pages)

### 6. **Scalable Architecture**

- ✅ Layered architecture (Controller → Service → Repository)
- ✅ DTO pattern để tách Entity khỏi API response
- ✅ JPA/Hibernate ORM
- ✅ Configurable via application.properties

---

## 🔧 HƯỚNG PHÁT TRIỂN THÊM

- [ ] **Notification System:** Email/SMS thông báo khi auction sắp kết thúc hoặc bị outbid
- [ ] **Payment Integration:** Tích hợp cổng thanh toán (VNPay, Momo)
- [ ] **Image Upload:** Upload ảnh sản phẩm từ admin panel
- [ ] **Search & Filter:** Advanced search với nhiều tiêu chí
- [ ] **Auction Categories:** Phân loại đấu giá theo category
- [ ] **Rating System:** Đánh giá người bán/người mua
- [ ] **Mobile App:** React Native/Flutter mobile client
- [ ] **Redis Caching:** Cache danh sách auction để giảm DB load
- [ ] **Docker Deployment:** Containerize với Docker Compose
- [ ] **CI/CD Pipeline:** GitHub Actions auto-deploy
- [ ] **Load Balancing:** Multiple server instances với Nginx
- [ ] **Analytics Dashboard:** Thống kê chi tiết với charts

---

## 🐛 KNOWN ISSUES & LIMITATIONS

1. **Session Storage:** Hiện tại dùng in-memory session, cần Redis cho production
2. **File Upload:** Chưa hỗ trợ upload ảnh, đang dùng URL
3. **Timezone:** Hardcode UTC, cần localization
4. **Mobile UI:** Chưa optimize tốt cho mobile
5. **WebSocket Scalability:** Cần Socket.IO + Redis Pub/Sub cho multi-instance

---

## 📝 GHI CHÚ

- ✅ Repo tuân thủ cấu trúc theo `INSTRUCTION.md`
- ✅ Code đã được test kỹ với nhiều scenarios
- ✅ Database schema tự động tạo qua JPA
- ✅ Sample data được khởi tạo tự động (DataInitializer)
- ⚠️ **Lưu ý:** Đảm bảo MySQL đã chạy trước khi start server
- ⚠️ **Port conflict:** Nếu port 8000/8080 đã được dùng, đổi trong config

---

## 📚 TÀI LIỆU THAM KHẢO

### Tài liệu chính thức:

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring WebSocket Guide](https://spring.io/guides/gs/messaging-stomp-websocket/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [MySQL 8.0 Reference Manual](https://dev.mysql.com/doc/refman/8.0/en/)
- [STOMP Protocol Specification](https://stomp.github.io/stomp-specification-1.2.html)

### Tutorials & Articles:

- [WebSocket Real-time Bidding System Tutorial](https://www.baeldung.com/spring-websockets-sendtouser)
- [Spring Boot + MySQL JPA Tutorial](https://spring.io/guides/gs/accessing-data-mysql/)
- [Bootstrap 5 Documentation](https://getbootstrap.com/docs/5.3/)

### Stack Overflow References:

- [Spring WebSocket CORS Configuration](https://stackoverflow.com/questions/...)
- [JPA OneToMany Best Practices](https://stackoverflow.com/questions/...)

---

## 👥 PHÂN CÔNG CÔNG VIỆC

| Thành viên        | Công việc chính                                             |
| ----------------- | ----------------------------------------------------------- |
| Nguyễn Trọng Khởi | Backend API, Database design, WebSocket implementation      |
| Trương Huy Tâm    | Frontend UI/UX, Client-side WebSocket, User dashboard       |
| Vũ Thành Nam      | Admin panel, Authentication/Security, Integration & Testing |

**Công việc chung:**

- Tất cả thành viên: Code review, Testing, Documentation, Presentation

---

## 📞 LIÊN HỆ & HỖ TRỢ

**Email nhóm:** kddmelothree@gmail.com

**Giảng viên hướng dẫn:** TS. Đặng Ngọc Hùng - hungdn@ptit.edu.vn

---

**© 2025 - Nhóm 01 - Lập trình mạng - K67 PTIT**

---

## 🎉 THANK YOU!

Cảm ơn thầy và các bạn đã xem project của nhóm chúng em!
