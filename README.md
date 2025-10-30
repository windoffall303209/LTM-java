# 🎯 HỆ THỐNG ĐẤU GIÁ TRỰC TUYẾN - AUCTION SYSTEM

> **Bài Tập Lớn Môn Lập Trình Mạng** - Nhóm 01

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 👥 THÔNG TIN NHÓM

| STT | Họ và Tên | MSSV | Email | Đóng góp |
|-----|-----------|------|-------|----------|
| 1 | Nguyễn Trọng Khởi | B22DCCN471 | kddmelothree@gmail.com | 100% |
| 2 | Trương Huy Tâm | B22DCCN711 | huytam514@gmail.com | 100% |
| 3 | Vũ Thành Nam | B22DCCN568 | nvuthanh4@gmail.com | 100% |

**Giảng viên hướng dẫn:** TS. Đặng Ngọc Hùng  
**Khoa:** Công nghệ thông tin 1 - PTIT

---

## 📋 MÔ TẢ DỰ ÁN

Hệ thống đấu giá trực tuyến realtime với kiến trúc **Client-Server** sử dụng **Java Socket + WebSocket**, cho phép nhiều người dùng tham gia đấu giá các sản phẩm cùng lúc qua mạng.

### 🎯 Tính Năng Chính

**Server (Backend):**
- ✅ Multi-threading với Thread Pool
- ✅ WebSocket Server cho web clients  
- ✅ Hệ thống đăng ký/đăng nhập với mã hóa password
- ✅ Quản lý nhiều phiên đấu giá đồng thời
- ✅ Tự động đếm ngược thời gian
- ✅ Tự động gia hạn thời gian (chống giật giây cuối)
- ✅ Hỗ trợ đặt giá tự động (auto-bid)
- ✅ Broadcast realtime đến tất cả clients
- ✅ Kết nối MySQL database
- ✅ Admin panel (tạo/xóa đấu giá, quản lý users)
- ✅ Logging hệ thống

**Client (Frontend):**
- ✅ Giao diện web đẹp với Bootstrap 5
- ✅ WebSocket client kết nối realtime
- ✅ Dashboard với nhiều tabs
- ✅ Cập nhật giá tức thời
- ✅ Đặt giá thủ công và tự động
- ✅ Lịch sử bids
- ✅ Thông báo popup + âm thanh
- ✅ Countdown timer
- ✅ Admin panel

---

## ⚙️ CÔNG NGHỆ SỬ DỤNG

### Backend
- **Java 17+** - Ngôn ngữ lập trình
- **Java Socket (TCP)** - Network programming
- **Java-WebSocket library** - WebSocket server
- **Multi-threading** - ExecutorService, Thread Pool
- **MySQL 8.0+** - Database
- **JDBC** - Database connectivity

### Frontend
- **HTML5 + CSS3** - Markup & styling
- **Bootstrap 5** - UI framework
- **JavaScript (ES6+)** - Client logic
- **WebSocket API** - Realtime communication

### Database
- **MySQL** - 5 tables (users, products, auctions, bids, logs)
- **Views & Triggers** - Tự động hóa

---

## 🚀 HƯỚNG DẪN CHẠY DỰ ÁN

### Yêu Cầu Hệ Thống

- ✅ **JDK 17+** 
- ✅ **MySQL Server 8.0+**
- ✅ **Node.js & npm** (cho client)
- ✅ **Maven** (optional, để build server)

### Bước 1: Clone Repository

```bash
git clone <repository-url>
cd assignment-network-project
```

### Bước 2: Setup Database

```bash
# Chạy MySQL command line
mysql -u root -p

# Tạo database
mysql -u root -p < source/database/schema.sql
mysql -u root -p < source/database/sample_data.sql
```

### Bước 3: Cấu Hình Server

Chỉnh sửa `source/server/src/main/resources/config.properties`:

```properties
db.host=localhost
db.port=3306
db.name=auction_db
db.username=root
db.password=YOUR_PASSWORD
server.port=8888
```

### Bước 4: Chạy Server

#### Option A: Dùng Maven
```bash
cd source/server
mvn clean compile
mvn exec:java -Dexec.mainClass="com.auction.server.AuctionServer"
```

#### Option B: Compile thủ công
```bash
cd source/server
javac -d bin -cp "lib/*" src/main/java/com/auction/server/**/*.java
java -cp "bin:lib/*" com.auction.server.AuctionServer
```

Output:
```
[INFO] Server started on port 8888
[INFO] Waiting for clients...
```

### Bước 5: Chạy Client

```bash
cd source/client
npm install
npm start
```

Mở browser: `http://localhost:3000`

### Bước 6: Đăng Nhập & Test

**Tài khoản test:**
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | admin |
| alice | admin123 | user |
| bob | admin123 | user |
| charlie | admin123 | user |

---

## 📂 CẤU TRÚC DỰ ÁN

```
assignment-network-project/
├── README.md                    # File này
├── intro.md                     # Hướng dẫn từ GV
├── read.md                      # Tài liệu chi tiết
├── review.md                    # Các mức độ thực hiện
│
├── statics/                     # Hình ảnh, screenshots
│   └── (screenshots...)
│
└── source/                      # MÃ NGUỒN
    │
    ├── common/                  # Module dùng chung
    │   ├── src/.../protocol/    # Giao thức
    │   └── src/.../dto/         # Data Transfer Objects
    │
    ├── server/                  # Backend (Port 8888)
    │   ├── pom.xml              # Maven config
    │   ├── src/.../server/
    │   │   ├── AuctionServer.java     # Main entry
    │   │   ├── controller/
    │   │   ├── service/
    │   │   ├── model/
    │   │   ├── database/
    │   │   └── util/
    │   └── lib/                 # External JARs
    │
    ├── client/                  # Frontend (Port 3000)
    │   ├── package.json
    │   ├── index.html
    │   ├── login.html
    │   ├── dashboard.html
    │   ├── css/
    │   └── js/
    │
    └── database/                # SQL scripts
        ├── schema.sql
        ├── sample_data.sql
        └── queries.sql
```

---

## 🔌 GIAO TIẾP (Protocol)

### Text-Based Protocol

Format: `COMMAND|param1|param2|...`

### Client → Server
- `LOGIN|username|password`
- `REGISTER|username|password|email`
- `LIST_AUCTIONS`
- `GET_AUCTION|auction_id`
- `BID|auction_id|amount`
- `AUTO_BID|auction_id|max_amount`

### Server → Client
- `LOGIN_SUCCESS|user_id|username|role`
- `LOGIN_FAILED|error_message`
- `AUCTION_LIST|id:name:price:time;...`
- `BID_UPDATE|auction_id|username|amount|time_left` (Broadcast)
- `AUCTION_ENDED|auction_id|winner|final_price`

---

## 📊 DEMO

### Screenshots

_(Thêm screenshots vào thư mục `statics/`)_

### Video Demo

_(Link YouTube sẽ được cập nhật)_

---

## 🎓 ĐIỂM MẠNH VỀ LẬP TRÌNH MẠNG

✅ **Socket Programming:** ServerSocket lắng nghe, accept connections  
✅ **Multi-threading:** Thread per client, Thread Pool  
✅ **Persistent Connection:** WebSocket bidirectional  
✅ **Protocol Design:** Text-based custom protocol  
✅ **Broadcast Mechanism:** Server push to all clients  
✅ **Thread Safety:** synchronized, ConcurrentHashMap  
✅ **Connection Management:** Reconnect, heartbeat  

---

## 🐛 TROUBLESHOOTING

### Lỗi: Cannot connect to database
```bash
# Kiểm tra MySQL đang chạy
# Kiểm tra username/password trong config.properties
```

### Lỗi: Port 8888 already in use
```bash
# Windows
netstat -ano | findstr :8888
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8888
kill -9 <PID>
```

### Lỗi: WebSocket connection failed
```bash
# Kiểm tra server đã chạy chưa
# Kiểm tra SERVER_URL trong client/js/config.js
```

---

## 📚 TÀI LIỆU THAM KHẢO

- [Java Socket Programming](https://docs.oracle.com/javase/tutorial/networking/sockets/)
- [Java-WebSocket Library](https://github.com/TooTallNate/Java-WebSocket)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Bootstrap 5](https://getbootstrap.com/docs/5.3/)

---

## 📝 LICENSE

MIT License - © 2025 Nhóm 01 - Lập Trình Mạng PTIT

---

## 📞 LIÊN HỆ

- **Email nhóm:** kddmelothree@gmail.com
- **GitHub:** _(Link repository)_

---

**🎉 Chúc mừng bạn đã đọc đến đây! Happy Coding! 🚀**

