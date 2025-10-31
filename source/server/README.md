# SERVER - Backend API

Backend của hệ thống đấu giá trực tuyến, sử dụng Spring Boot framework.

## 🛠️ Công nghệ

- **Spring Boot 3.2.0** - Framework chính
- **Spring Web** - REST API
- **Spring WebSocket** - Realtime communication
- **Spring Data JPA** - Database access
- **Spring Security** - Authentication & Authorization
- **Hibernate 6.x** - JPA implementation
- **MySQL 8.0** - Database
- **Lombok** - Giảm boilerplate code
- **Maven 3.9+** - Build tool
- **Java 17** - Runtime

## 📁 Cấu trúc

```
server/
├── pom.xml                          # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/auction/
│   │   │   ├── AuctionSystemApplication.java  # Main class
│   │   │   ├── config/                        # Configurations
│   │   │   │   ├── SecurityConfig.java        # Spring Security
│   │   │   │   ├── WebSocketConfig.java       # WebSocket setup
│   │   │   │   ├── WebConfig.java             # CORS, etc.
│   │   │   │   └── DataInitializer.java       # Sample data
│   │   │   ├── controller/                    # REST Controllers
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── AuctionController.java
│   │   │   │   ├── BidController.java
│   │   │   │   ├── WatchlistController.java
│   │   │   │   ├── UserController.java
│   │   │   │   └── AdminController.java
│   │   │   ├── model/                         # JPA Entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Auction.java
│   │   │   │   ├── Bid.java
│   │   │   │   └── Watchlist.java
│   │   │   ├── repository/                    # JPA Repositories
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── AuctionRepository.java
│   │   │   │   ├── BidRepository.java
│   │   │   │   └── WatchlistRepository.java
│   │   │   ├── service/                       # Business Logic
│   │   │   │   ├── UserService.java
│   │   │   │   ├── AuctionService.java
│   │   │   │   ├── BidService.java
│   │   │   │   ├── WatchlistService.java
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   └── AuctionSchedulerService.java
│   │   │   ├── dto/                           # Data Transfer Objects
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── AuctionDTO.java
│   │   │   │   ├── BidDTO.java
│   │   │   │   ├── UserDTO.java
│   │   │   │   └── WatchlistDTO.java
│   │   │   └── websocket/                     # WebSocket handlers
│   │   │       └── WebSocketController.java
│   │   └── resources/
│   │       └── application.properties         # App configuration
│   └── test/                                  # Unit tests
└── target/                                    # Build output
```

## 🚀 Cài đặt và Chạy

### Yêu cầu:
- ✅ Java JDK 17+
- ✅ Maven 3.9+
- ✅ MySQL 8.0

### 1. Tạo Database

```sql
CREATE DATABASE auction_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Cấu hình Database

Mở `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auction_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=admin
```

Đổi username/password thành MySQL của bạn.

### 3. Build Project

```bash
mvn clean install
```

### 4. Chạy Server

```bash
mvn spring-boot:run
```

Server sẽ chạy tại: **http://localhost:8000**

### 5. Kiểm tra

```bash
curl http://localhost:8000/api/health
```

## 📊 Database Schema

Schema được tự động tạo bởi JPA (Hibernate DDL auto-update).

### Entities:

**User:**
- userId (PK, AUTO_INCREMENT)
- username (UNIQUE)
- password (BCrypt hashed)
- fullName
- email
- role (USER/ADMIN)
- balance
- isActive

**Auction:**
- auctionId (PK, AUTO_INCREMENT)
- title
- description
- imageUrl
- startingPrice
- currentPrice
- status (PENDING/ACTIVE/ENDED)
- startTime
- endTime
- totalBids
- extendCount
- seller (FK → User)
- highestBidder (FK → User)

**Bid:**
- bidId (PK, AUTO_INCREMENT)
- bidAmount
- bidTime
- isAutoBid
- user (FK → User)
- auction (FK → Auction)

**Watchlist:**
- watchlistId (PK, AUTO_INCREMENT)
- addedAt
- user (FK → User)
- auction (FK → Auction)

## 🔗 API Endpoints

### Authentication:
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/logout` - Đăng xuất
- `POST /api/auth/register` - Đăng ký

### Auctions:
- `GET /api/auctions` - Danh sách tất cả đấu giá (ACTIVE + PENDING + ENDED)
- `GET /api/auctions/active` - Danh sách đấu giá đang ACTIVE (cho trang chủ)
- `GET /api/auctions/{id}` - Chi tiết đấu giá
- `GET /api/auctions/search?keyword=...` - Tìm kiếm

### Bids:
- `POST /api/bids` - Đặt giá
- `GET /api/bids/auction/{id}` - Lịch sử bid của auction
- `GET /api/bids/user?userId=...` - Lịch sử bid của user

### Watchlist:
- `POST /api/watchlist` - Thêm vào watchlist
- `GET /api/watchlist/user?userId=...` - Lấy watchlist
- `DELETE /api/watchlist/auction/{id}` - Xóa khỏi watchlist

### Admin:
- `GET /api/admin/auctions/all` - Tất cả đấu giá (bao gồm ENDED)
- `POST /api/admin/auctions` - Tạo đấu giá mới
- `PUT /api/admin/auctions/{id}` - Cập nhật đấu giá
- `DELETE /api/admin/auctions/{id}` - Xóa đấu giá
- `POST /api/admin/auctions/{id}/start` - Bắt đầu đấu giá
- `POST /api/admin/auctions/{id}/end` - Kết thúc đấu giá
- `GET /api/admin/users` - Danh sách users
- `GET /api/admin/statistics` - Thống kê hệ thống

## 🔌 WebSocket

### Configuration:
- Endpoint: `/ws`
- Protocol: STOMP over SockJS

### Topics:
- `/topic/auction/{id}` - Updates của đấu giá cụ thể
- `/topic/auctions` - Events global (created/started/ended)

### Message Types:
- `BID_UPDATE` - User đặt giá thành công
- `AUCTION_EXTENDED` - Tự động gia hạn
- `AUCTION_ENDED` - Đấu giá kết thúc
- `AUCTION_CREATED` - Admin tạo đấu giá mới
- `AUCTION_STARTED` - Admin start đấu giá
- `USER_JOINED` - User join phòng

## 🔒 Security

### Spring Security Configuration:
- Session-based authentication
- BCrypt password encoding
- Role-based authorization (USER/ADMIN)
- CSRF disabled (for API)
- CORS enabled cho client

### Protected Endpoints:
- `/api/admin/**` - Chỉ ADMIN
- `/api/auth/logout` - Authenticated users
- Còn lại - Authenticated users

### Public Endpoints:
- `/api/auth/login`
- `/api/auth/register`
- `/ws/**` (WebSocket)

## ⚙️ Configuration

### application.properties:

```properties
# Server
server.port=8000

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/auction_db
spring.datasource.username=root
spring.datasource.password=admin

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Auction Business Logic
auction.auto-extend-seconds=60
auction.max-extend-count=3
auction.min-bid-increment=100000

# WebSocket
websocket.endpoint=/ws
websocket.allowed-origins=*
```

## 🎯 Business Logic

### Auto-extend Auction:
- Khi có bid trong phút cuối → gia hạn 60s
- Tối đa 3 lần gia hạn
- Broadcast AUCTION_EXTENDED event

### Bid Validation:
- Amount phải > currentPrice + minIncrement
- User không được bid vào auction của chính mình
- Auction phải ở trạng thái ACTIVE

### Scheduled Jobs:
- Kiểm tra và kết thúc các auction đã hết hạn (mỗi phút)

## 🧪 Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn clean test jacoco:report
```

## 📦 Build & Deploy

### Build JAR:
```bash
mvn clean package
```

File JAR: `target/auction-system-1.0.0.jar`

### Run JAR:
```bash
java -jar target/auction-system-1.0.0.jar
```

## 🐛 Troubleshooting

**Port 8000 đã được dùng:**
```properties
# application.properties
server.port=8080
```

**MySQL connection error:**
- Kiểm tra MySQL đã chạy
- Kiểm tra username/password
- Kiểm tra database đã tạo

**WebSocket không hoạt động:**
- Kiểm tra CORS config
- Kiểm tra client URL trong allowed-origins

## 📝 Development Notes

### Layered Architecture:
```
Controller → Service → Repository → Database
```

### DTO Pattern:
- Tách Entity khỏi API response
- Bảo mật (không expose sensitive fields)
- Flexibility (custom response structure)

### Lombok:
- `@Data` - Getters/Setters/ToString/EqualsHashCode
- `@RequiredArgsConstructor` - Constructor injection
- `@Slf4j` - Logger

## 📚 Tài liệu tham khảo

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring WebSocket](https://spring.io/guides/gs/messaging-stomp-websocket/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate Docs](https://hibernate.org/orm/documentation/)
