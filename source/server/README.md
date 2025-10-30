# Server Module - Backend

Server backend cho hệ thống đấu giá trực tuyến sử dụng Java Socket + WebSocket.

## Công Nghệ

- **Java 17+**
- **Java Socket (TCP)**
- **Java-WebSocket library** 
- **MySQL 8.0+ (JDBC)**
- **Multi-threading (ExecutorService)**

## Cấu Trúc

```
server/
├── pom.xml                      # Maven config
├── lib/                         # External JARs
│   ├── mysql-connector-j.jar
│   └── Java-WebSocket.jar
├── logs/                        # Log files
└── src/main/
    ├── java/com/auction/server/
    │   ├── AuctionServer.java          # Main entry point
    │   ├── controller/
    │   │   ├── ClientHandler.java      # Handle mỗi client
    │   │   ├── AuctionController.java
    │   │   └── AdminController.java
    │   ├── service/
    │   │   ├── AuthService.java        # Authentication
    │   │   ├── AuctionService.java     # Auction logic
    │   │   ├── BidService.java         # Bid processing
    │   │   ├── UserService.java
    │   │   └── BroadcastService.java   # Realtime broadcast
    │   ├── model/
    │   │   ├── User.java
    │   │   ├── Auction.java
    │   │   ├── Bid.java
    │   │   └── Product.java
    │   ├── database/
    │   │   ├── DatabaseConnection.java
    │   │   ├── UserDAO.java
    │   │   ├── AuctionDAO.java
    │   │   └── BidDAO.java
    │   └── util/
    │       ├── Logger.java
    │       ├── PasswordUtil.java
    │       └── ConfigLoader.java
    └── resources/
        └── config.properties
```

## Cài Đặt & Chạy

### Bước 1: Setup Database

```bash
mysql -u root -p < ../database/schema.sql
mysql -u root -p < ../database/sample_data.sql
```

### Bước 2: Cấu Hình

Chỉnh sửa `src/main/resources/config.properties`:

```properties
db.host=localhost
db.port=3306
db.name=auction_db
db.username=root
db.password=YOUR_PASSWORD

server.port=8888
server.max.threads=100
```

### Bước 3: Download Dependencies

Download JARs vào `lib/`:
- [MySQL Connector/J 8.x](https://dev.mysql.com/downloads/connector/j/)
- [Java-WebSocket 1.5.x](https://github.com/TooTallNate/Java-WebSocket/releases)

### Bước 4: Compile

#### Option A: Maven
```bash
mvn clean compile
```

#### Option B: javac
```bash
javac -d bin -cp "lib/*:../common/common.jar" src/main/java/com/auction/server/**/*.java
```

### Bước 5: Run

#### Option A: Maven
```bash
mvn exec:java -Dexec.mainClass="com.auction.server.AuctionServer"
```

#### Option B: java
```bash
java -cp "bin:lib/*:../common/common.jar" com.auction.server.AuctionServer
```

Output:
```
[INFO] Loading configuration...
[INFO] Connecting to database...
[INFO] Database connected successfully
[INFO] WebSocket Server started on port 8888
[INFO] Server IP: 192.168.1.100
[INFO] Waiting for clients...
```

## Kiến Trúc

### Multi-threading Model

```
[AuctionServer Main Thread]
        ↓
[WebSocket Server] (Port 8888)
        ↓
    Accept Client
        ↓
[ExecutorService Thread Pool]
    ├── ClientHandler Thread 1 (Alice)
    ├── ClientHandler Thread 2 (Bob)
    ├── ClientHandler Thread 3 (Charlie)
    └── ...
```

### Message Flow

```
Client → WebSocket → ClientHandler → Service → DAO → Database
Client ← WebSocket ← Broadcast   ← Service ←
```

## Protocol

Xem chi tiết trong `../common/README.md`

Format: `COMMAND|param1|param2|...`

## Troubleshooting

### Lỗi: Port already in use
```bash
# Windows
netstat -ano | findstr :8888
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8888
kill -9 <PID>
```

### Lỗi: Cannot connect to database
- Kiểm tra MySQL đang chạy
- Kiểm tra config.properties
- Kiểm tra mysql-connector-j.jar trong lib/

### Lỗi: ClassNotFoundException
- Đảm bảo tất cả JARs trong lib/
- Kiểm tra classpath khi compile và run

