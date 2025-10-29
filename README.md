# 🔨 Auction System - Hệ Thống Đấu Giá Trực Tuyến

Hệ thống đấu giá trực tuyến real-time với Spring Boot, WebSocket, MySQL và tính năng lên lịch tự động.

## 📋 Mục Lục

- [Giới Thiệu](#giới-thiệu)
- [Tính Năng](#tính-năng)
- [Công Nghệ Sử Dụng](#công-nghệ-sử-dụng)
- [Yêu Cầu Hệ Thống](#yêu-cầu-hệ-thống)
- [Hướng Dẫn Cài Đặt](#hướng-dẫn-cài-đặt)
- [Hướng Dẫn Chạy Project](#hướng-dẫn-chạy-project)
- [Tài Khoản Mặc Định](#tài-khoản-mặc-định)
- [Cấu Trúc Project](#cấu-trúc-project)
- [API Endpoints](#api-endpoints)
- [Tính Năng Nổi Bật](#tính-năng-nổi-bật)

---

## 📖 Giới Thiệu

**Auction System** là một ứng dụng web đấu giá trực tuyến với các tính năng hiện đại:

- 🔴 Đấu giá real-time qua WebSocket
- ⏰ Lên lịch đấu giá tự động (Auto Scheduling)
- 🚀 Tự động kết thúc khi không có người đặt giá
- ⭐ Quản lý danh sách theo dõi (Watchlist)
- 🔐 Phân quyền Admin/User với Spring Security
- 📱 Giao diện responsive với Bootstrap 5

---

## ✨ Tính Năng

### 👤 **Chức năng USER:**

- ✅ Đăng ký/Đăng nhập (Spring Security + BCrypt)
- ✅ Xem danh sách đấu giá **đang diễn ra** và **sắp diễn ra** (PENDING)
- ✅ Xem chi tiết đấu giá với cập nhật real-time
- ✅ Đặt giá thủ công và Quick Bid
- ✅ Xem lịch sử đặt giá của mình (My Bids)
- ✅ Thêm đấu giá vào danh sách theo dõi (Watchlist) - **Cả PENDING auctions**
- ✅ Nhận thông báo real-time khi đấu giá bắt đầu/kết thúc
- ✅ Xem số dư tài khoản (mặc định 2 tỷ VND)

### 👑 **Chức năng ADMIN:**

- ✅ Dashboard quản trị với thống kê tổng quan
- ✅ Tạo/Sửa/Xóa đấu giá (CRUD)
- ✅ **Thiết lập thời gian bắt đầu và kết thúc** cho đấu giá
- ✅ **Bắt đầu đấu giá thủ công** (Manual Start)
- ✅ Kết thúc đấu giá bất cứ lúc nào
- ✅ Quản lý người dùng (Ban/Unban)
- ✅ Cập nhật số dư user

### 🤖 **Tính năng tự động:**

- ⏰ **Auto Start:** Đấu giá tự động bắt đầu khi đến `startTime`
- 🏁 **Auto End:** Đấu giá tự động kết thúc khi hết `endTime`
- ⚡ **Smart End:** Tự động kết thúc sớm sau **20 phút không có người tăng giá**
- 🔔 **Real-time Notifications:** Thông báo WebSocket khi đấu giá bắt đầu/kết thúc

---

## 🛠️ Công Nghệ Sử Dụng

- **Backend:** Spring Boot 3.2.0
- **Security:** Spring Security + BCrypt
- **Database:** MySQL 8.0
- **ORM:** Spring Data JPA / Hibernate
- **Frontend:** Thymeleaf + Bootstrap 5 + Bootstrap Icons
- **Real-time:** WebSocket (SockJS + STOMP)
- **Scheduling:** Spring Scheduler (@Scheduled)
- **Build Tool:** Maven
- **Java Version:** 17

---

## 💻 Yêu Cầu Hệ Thống

### **Phần mềm cần thiết:**

| Phần mềm           | Phiên bản       | Link Download                                                                                                                          |
| ------------------ | --------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| **Java JDK**       | 17 hoặc cao hơn | [Oracle JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) hoặc [OpenJDK 17](https://adoptium.net/) |
| **Maven**          | 3.8+            | [Apache Maven](https://maven.apache.org/download.cgi)                                                                                  |
| **MySQL**          | 8.0+            | [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)                                                                       |
| **IDE (Tùy chọn)** | -               | Xem bảng dưới                                                                                                                          |

### **IDE Được Khuyến Nghị:**

| IDE                         | Miễn phí | Link Download                                        | Ghi chú                  |
| --------------------------- | -------- | ---------------------------------------------------- | ------------------------ |
| **IntelliJ IDEA Community** | ✅       | [Download](https://www.jetbrains.com/idea/download/) | Tốt nhất cho Spring Boot |
| **Eclipse IDE for Java EE** | ✅       | [Download](https://www.eclipse.org/downloads/)       | Hỗ trợ tốt Maven         |
| **Visual Studio Code**      | ✅       | [Download](https://code.visualstudio.com/)           | Cần cài extension Java   |
| **NetBeans**                | ✅       | [Download](https://netbeans.apache.org/download/)    | Tích hợp Maven sẵn       |

### **Extensions cho VS Code (nếu dùng):**

- [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
- [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=pivotal.vscode-boot-dev-pack)

---

## 📦 Hướng Dẫn Cài Đặt

### **Bước 1: Cài đặt Java JDK 17**

#### Windows:

1. Download Oracle JDK 17 hoặc OpenJDK 17
2. Chạy file installer (.exe)
3. Thiết lập biến môi trường:
   - Mở **System Properties** → **Environment Variables**
   - Thêm `JAVA_HOME` = `C:\Program Files\Java\jdk-17`
   - Thêm `%JAVA_HOME%\bin` vào `PATH`
4. Kiểm tra:

```bash
java -version
```

#### macOS:

```bash
brew install openjdk@17
```

#### Linux (Ubuntu/Debian):

```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

### **Bước 2: Cài đặt Maven**

#### Windows:

1. Download Maven từ [Apache Maven](https://maven.apache.org/download.cgi)
2. Giải nén vào `C:\Program Files\Apache\maven`
3. Thêm vào PATH: `C:\Program Files\Apache\maven\bin`
4. Kiểm tra:

```bash
mvn -version
```

#### macOS:

```bash
brew install maven
```

#### Linux:

```bash
sudo apt install maven
```

### **Bước 3: Cài đặt MySQL**

1. Download MySQL Community Server 8.0+
2. Cài đặt với password: `admin` (hoặc tùy chỉnh trong `application.properties`)
3. Khởi động MySQL service

#### Windows:

- MySQL sẽ tự động start sau khi cài

#### macOS:

```bash
brew install mysql
brew services start mysql
```

#### Linux:

```bash
sudo apt install mysql-server
sudo systemctl start mysql
```

### **Bước 4: Tạo Database**

Mở MySQL command line hoặc MySQL Workbench và chạy:

```sql
CREATE DATABASE auction_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**Hoặc sử dụng terminal:**

```bash
mysql -u root -p
# Nhập password (mặc định: admin)
```

```sql
CREATE DATABASE auction_db;
EXIT;
```

### **Bước 5: Clone/Download Project**

```bash
# Nếu có Git
git clone <repository-url>

# Hoặc download ZIP và giải nén
```

### **Bước 6: Cấu hình Database (Nếu cần)**

Mở file `src/main/resources/application.properties` và kiểm tra:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auction_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=admin
```

**Nếu bạn dùng password khác cho MySQL, hãy thay `admin` bằng password của bạn.**

---

## 🚀 Hướng Dẫn Chạy Project

### **Cách 1: Chạy bằng Maven (Command Line)**

```bash
# Di chuyển vào thư mục project
cd path/to/auction-system

# Compile project
mvn clean compile

# Chạy ứng dụng
mvn spring-boot:run
```

### **Cách 2: Chạy bằng IntelliJ IDEA**

1. Mở IntelliJ IDEA
2. **File** → **Open** → Chọn thư mục project
3. Đợi Maven import dependencies
4. Tìm file `AuctionSystemApplication.java`
5. Click chuột phải → **Run 'AuctionSystemApplication'**

### **Cách 3: Chạy bằng Eclipse**

1. **File** → **Import** → **Existing Maven Projects**
2. Chọn thư mục project
3. Đợi Maven build
4. Click chuột phải vào project → **Run As** → **Spring Boot App**

### **Cách 4: Chạy JAR file**

```bash
# Build JAR
mvn clean package -DskipTests

# Chạy JAR
java -jar target/auction-system-1.0.0.jar
```

---

## 🌐 Truy Cập Ứng Dụng

Sau khi chạy thành công, mở trình duyệt và truy cập:

| Trang              | URL                             |
| ------------------ | ------------------------------- |
| **Trang chủ**      | http://localhost:8080           |
| **Đăng nhập**      | http://localhost:8080/login     |
| **Đăng ký**        | http://localhost:8080/register  |
| **Dashboard User** | http://localhost:8080/dashboard |
| **Admin Panel**    | http://localhost:8080/admin     |

---

## 🔐 Tài Khoản Mặc Định

Hệ thống tự động tạo các tài khoản sau khi khởi động lần đầu:

### **Admin Account:**

```
Username: admin
Password: admin123
Số dư: 2,000,000,000 VND (2 tỷ)
Quyền: ADMIN (Toàn quyền quản trị)
```

### **Demo User Accounts:**

```
Username: user1
Password: 123456
Số dư: 2,000,000,000 VND (2 tỷ)
Quyền: USER

Username: user2
Password: 123456
Số dư: 2,000,000,000 VND (2 tỷ)
Quyền: USER
```

### **Đăng ký tài khoản mới:**

- Truy cập: http://localhost:8080/register
- Tài khoản mới sẽ nhận **2,000,000,000 VND (2 tỷ)** ban đầu

---

## 📁 Cấu Trúc Project

```
auction-system/
├── src/
│   ├── main/
│   │   ├── java/com/auction/
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java      # Khởi tạo dữ liệu ban đầu
│   │   │   │   ├── SecurityConfig.java       # Cấu hình Spring Security
│   │   │   │   └── WebSocketConfig.java      # Cấu hình WebSocket
│   │   │   ├── controller/
│   │   │   │   ├── AdminController.java      # REST API cho Admin
│   │   │   │   ├── AuctionController.java    # REST API Auctions
│   │   │   │   ├── AuthController.java       # Đăng ký/Profile
│   │   │   │   ├── BidController.java        # REST API Bidding
│   │   │   │   ├── ViewController.java       # Render HTML pages
│   │   │   │   └── WatchlistController.java  # REST API Watchlist
│   │   │   ├── dto/
│   │   │   │   ├── ApiResponse.java          # Generic API response
│   │   │   │   ├── AuctionDTO.java
│   │   │   │   ├── BidDTO.java
│   │   │   │   ├── UserDTO.java
│   │   │   │   └── WatchlistDTO.java
│   │   │   ├── model/
│   │   │   │   ├── Auction.java              # Entity với lastBidTime
│   │   │   │   ├── Bid.java
│   │   │   │   ├── User.java
│   │   │   │   └── Watchlist.java
│   │   │   ├── repository/
│   │   │   │   ├── AuctionRepository.java
│   │   │   │   ├── BidRepository.java
│   │   │   │   ├── UserRepository.java
│   │   │   │   └── WatchlistRepository.java
│   │   │   ├── service/
│   │   │   │   ├── AuctionSchedulerService.java  # 🆕 Auto scheduling
│   │   │   │   ├── AuctionService.java
│   │   │   │   ├── BidService.java
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── WatchlistService.java
│   │   │   └── AuctionSystemApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── admin/
│   │       │   │   ├── dashboard.html        # Admin overview
│   │       │   │   ├── auctions.html         # Quản lý auctions
│   │       │   │   └── users.html            # Quản lý users
│   │       │   ├── auction-detail.html       # Chi tiết đấu giá
│   │       │   ├── dashboard.html            # User dashboard
│   │       │   ├── login.html
│   │       │   ├── register.html
│   │       │   ├── my-bids.html              # Lịch sử đặt giá
│   │       │   └── watchlist.html            # Danh sách theo dõi
│   │       ├── static/
│   │       │   ├── css/
│   │       │   └── js/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

---

## 🌟 Các Trang Chính

### **User Pages:**

| Trang            | Đường dẫn       | Mô tả                                                |
| ---------------- | --------------- | ---------------------------------------------------- |
| Trang chủ        | `/`             | Landing page                                         |
| Đăng nhập        | `/login`        | Form đăng nhập                                       |
| Đăng ký          | `/register`     | Form đăng ký tài khoản mới                           |
| Dashboard        | `/dashboard`    | Danh sách đấu giá **ACTIVE** và **PENDING**          |
| Chi tiết đấu giá | `/auction/{id}` | Xem chi tiết và đặt giá (hoặc theo dõi nếu PENDING)  |
| Lịch sử đấu giá  | `/my-bids`      | Xem lịch sử đặt giá của mình                         |
| Theo dõi         | `/watchlist`    | Danh sách đấu giá đang theo dõi (bao gồm cả PENDING) |

### **Admin Pages:**

| Trang              | Đường dẫn         | Mô tả                                    |
| ------------------ | ----------------- | ---------------------------------------- |
| Admin Dashboard    | `/admin`          | Tổng quan hệ thống                       |
| Quản lý Đấu Giá    | `/admin/auctions` | CRUD, Start/End auctions, set thời gian  |
| Quản lý Người Dùng | `/admin/users`    | Quản lý users, ban/unban, cập nhật số dư |

---

## 📡 API Endpoints

### **Authentication:**

- `POST /api/auth/register` - Đăng ký user mới
- `POST /api/auth/login` - Đăng nhập (Spring Security)
- `POST /api/auth/logout` - Đăng xuất
- `GET /api/auth/profile` - Lấy thông tin user hiện tại

### **Auctions:**

- `GET /api/auctions` - Lấy danh sách đấu giá **ACTIVE và PENDING**
- `GET /api/auctions/{id}` - Lấy chi tiết đấu giá
- `GET /api/auctions/search?keyword=...` - Tìm kiếm đấu giá

### **Bidding:**

- `POST /api/bids?userId={userId}` - Đặt giá
  ```json
  {
    "auctionId": 1,
    "bidAmount": 5000000
  }
  ```
- `GET /api/bids/auction/{auctionId}` - Lấy lịch sử bid của auction
- `GET /api/bids/user?userId={userId}` - Lấy bids của user

### **Watchlist:**

- `POST /api/watchlist?userId={userId}&auctionId={auctionId}` - Thêm vào watchlist
- `GET /api/watchlist/user?userId={userId}` - Lấy watchlist của user
- `DELETE /api/watchlist/{id}` - Xóa khỏi watchlist (từ trang watchlist)
- `DELETE /api/watchlist/auction/{auctionId}?userId={userId}` - Xóa khỏi watchlist (từ auction detail)
- `GET /api/watchlist/check?userId={userId}&auctionId={auctionId}` - Kiểm tra đã thêm chưa

### **Admin Auctions:**

- `POST /api/admin/auctions` - Tạo đấu giá mới
  ```
  title, description, startingPrice, startTime, endTime, imageUrl
  ```
- `PUT /api/admin/auctions/{id}` - Cập nhật đấu giá
- `DELETE /api/admin/auctions/{id}` - Xóa đấu giá
- `POST /api/admin/auctions/{id}/start` - **Bắt đầu đấu giá thủ công**
- `POST /api/admin/auctions/{id}/end` - **Kết thúc đấu giá thủ công**
- `GET /api/admin/auctions/all` - Lấy tất cả auctions (bao gồm ENDED)

### **Admin Users:**

- `GET /api/admin/users` - Lấy danh sách tất cả users
- `POST /api/admin/users/{id}/toggle-status` - Ban/Unban user
- `POST /api/admin/users/{id}/update-balance?amount={amount}` - Cập nhật số dư

### **Admin Statistics:**

- `GET /api/admin/statistics` - Lấy thống kê hệ thống

---

## 🎯 Tính Năng Nổi Bật

### 1. **Real-time Bidding với WebSocket**

**Mô tả:**

- Sử dụng WebSocket (SockJS + STOMP) để giao tiếp real-time
- Tất cả users đang xem auction sẽ nhận update ngay lập tức
- Không cần refresh trang

**Các events được broadcast:**

- `BID_UPDATE`: Khi có người đặt giá mới
- `AUCTION_EXTENDED`: Khi đấu giá được gia hạn (bid trong 60s cuối)
- `AUCTION_STARTED`: Khi đấu giá bắt đầu
- `AUCTION_ENDED`: Khi đấu giá kết thúc

**WebSocket Endpoints:**

```javascript
// Subscribe to specific auction
stompClient.subscribe("/topic/auction/{auctionId}", callback);

// Subscribe to all auctions (for dashboard)
stompClient.subscribe("/topic/auctions", callback);
```

---

### 2. **Auction Scheduling System** 🆕

**Mô tả:**
Hệ thống lên lịch tự động quản lý vòng đời của auctions:

#### **a) Manual Start by Admin:**

- Admin có thể thiết lập `startTime` và `endTime` khi tạo auction
- Status ban đầu: `PENDING`
- Admin click nút **"Start"** để bắt đầu ngay lập tức

#### **b) Auto Start:**

- `AuctionSchedulerService` chạy mỗi **30 giây** (có thể điều chỉnh)
- Tự động tìm các PENDING auctions đã đến `startTime`
- Chuyển status → `ACTIVE`
- Broadcast thông báo đến tất cả clients

#### **c) Auto End - Time Expired:**

- Tự động kết thúc khi `LocalDateTime.now() > endTime`
- Chuyển status → `ENDED`

#### **d) Smart End - No Activity:** ⚡

- Track `lastBidTime` mỗi khi có bid mới
- Nếu không có bid trong **20 phút**, tự động kết thúc
- Lý do: "Không có người tăng giá trong 20 phút"

**Code Example:**

```java
@Scheduled(fixedRate = 30000) // 30 seconds
public void checkAuctions() {
    // Check và start PENDING auctions
    // Check và end ACTIVE auctions (time expired hoặc no activity)
}
```

---

### 3. **Watchlist - Theo dõi Đấu Giá**

**Mô tả:**

- User có thể thêm bất kỳ auction nào vào watchlist
- **Hỗ trợ cả PENDING auctions** - Theo dõi đấu giá sắp diễn ra
- Dễ dàng quay lại và không bỏ lỡ

**Tính năng:**

- Thêm/Xóa khỏi watchlist
- Hiển thị danh sách theo dõi với đầy đủ thông tin
- Badge "Đã Theo Dõi" trên auction detail page
- Toggle button để thêm/xóa

---

### 4. **PENDING Auctions Display** 🆕

**Mô tả:**
Dashboard hiển thị cả auctions **ACTIVE** và **PENDING**

**Visual Differences:**

**PENDING Card:**

- 🏷️ Ribbon vàng "Sắp diễn ra" ở góc trên
- 🟡 Border màu vàng (warning)
- 🎨 Hình ảnh có opacity 85%
- 📅 Hiển thị thời gian bắt đầu và kết thúc
- 💬 Alert: "Chưa có ai đặt giá. Hãy theo dõi để không bỏ lỡ!"
- 🔘 Nút: "Xem chi tiết & Theo dõi" (outline)

**ACTIVE Card:**

- 🟢 Badge "Đang diễn ra"
- 💰 Giá hiện tại, người dẫn đầu
- ⏱️ Countdown timer
- 🔵 Nút: "Xem chi tiết & Đấu giá" (solid)

**Detail Page:**

- PENDING: Ẩn form đặt giá, hiển thị thời gian bắt đầu
- ACTIVE: Hiển thị form đặt giá đầy đủ
- Watchlist button luôn hiển thị

---

### 5. **Security & Authentication**

**Mô tả:**

- Password được mã hóa bằng **BCrypt**
- Session management với Spring Security
- Phân quyền rõ ràng: **USER** / **ADMIN**

**Protected Routes:**

- User pages: Yêu cầu authentication
- Admin pages: Yêu cầu role `ADMIN`
- API endpoints: Kiểm tra quyền tương ứng

**Custom UserDetailsService:**

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    // Load user from database và map sang Spring Security UserDetails
}
```

---

### 6. **Responsive UI với Bootstrap 5**

**Mô tả:**

- Giao diện đẹp, hiện đại
- Hoạt động tốt trên mobile, tablet, desktop
- Bootstrap Icons cho icons
- Sticky header với shrink effect
- Sticky sidebar cho bidding panel

**CSS Features:**

- Navbar shrink on scroll
- Sticky sidebar (auction detail)
- Ribbon effect cho PENDING cards
- Smooth transitions

---

### 7. **Bid Extension System**

**Mô tả:**

- Nếu có bid trong **60 giây cuối**, auction được gia hạn thêm **60 giây**
- Tối đa gia hạn **3 lần** (có thể điều chỉnh)
- Broadcast thông báo: "⏰ Đấu giá được gia hạn thêm 60 giây!"

**Lý do:**

- Tránh "sniping" (đặt giá vào giây cuối)
- Đảm bảo công bằng cho tất cả người đấu giá

---

## 🔧 Troubleshooting

### **Lỗi: Port 8080 already in use**

```bash
# Đổi port trong application.properties
server.port=8081
```

### **Lỗi: Access denied for user 'root'@'localhost'**

- Kiểm tra password MySQL trong `application.properties`
- Đảm bảo MySQL đang chạy
- Thử reset password MySQL

```bash
# Windows
net start MySQL80

# Linux/macOS
sudo systemctl start mysql
```

### **Lỗi: Table doesn't exist**

- Kiểm tra `spring.jpa.hibernate.ddl-auto=update` trong `application.properties`
- Database sẽ tự động tạo tables khi chạy lần đầu
- Nếu vẫn lỗi, xóa database và tạo lại:

```sql
DROP DATABASE IF EXISTS auction_db;
CREATE DATABASE auction_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### **Lỗi: Java version**

```bash
# Kiểm tra version Java
java -version

# Phải là Java 17 trở lên
# Nếu sai version, cài lại JDK 17
```

### **Lỗi: Maven command not found**

```bash
# Kiểm tra Maven đã cài chưa
mvn -version

# Kiểm tra PATH environment variable
echo %PATH% # Windows
echo $PATH  # Linux/macOS
```

### **Lỗi: WebSocket connection failed**

- Kiểm tra firewall không block port 8080
- Kiểm tra browser hỗ trợ WebSocket
- Kiểm tra console log:

```javascript
// Trong browser console
// Nên thấy: "Connected to WebSocket"
```

### **Lỗi: Scheduled tasks không chạy**

- Kiểm tra `@EnableScheduling` trong `AuctionSystemApplication.java`
- Kiểm tra log console có thông báo auto-start/end không
- Điều chỉnh `fixedRate` trong `AuctionSchedulerService.java` nếu cần

---

## 📚 Tài Liệu Tham Khảo

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Scheduling](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling)
- [Thymeleaf](https://www.thymeleaf.org/documentation.html)
- [Bootstrap 5](https://getbootstrap.com/docs/5.3/)
- [Bootstrap Icons](https://icons.getbootstrap.com/)
- [WebSocket với Spring](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket)
- [SockJS Client](https://github.com/sockjs/sockjs-client)
- [STOMP Protocol](https://stomp.github.io/)

---

## 🚦 Workflow Đấu Giá

### **Từ Admin:**

1. **Tạo Auction:**

   - Login admin → Vào `/admin/auctions`
   - Click "Tạo Đấu Giá Mới"
   - Nhập: Title, Description, Giá khởi điểm
   - Chọn thời gian bắt đầu và kết thúc (hoặc nhập số phút)
   - Submit → Auction status: `PENDING`

2. **Start Auction:**

   - **Option 1:** Click nút "Start" ngay lập tức
   - **Option 2:** Đợi hệ thống tự động start khi đến `startTime`

3. **Monitor:**
   - Xem real-time updates
   - Kết thúc bất cứ lúc nào nếu cần (nút "End")

### **Từ User:**

1. **Xem Danh Sách:**

   - Login user → Vào `/dashboard`
   - Thấy cả ACTIVE và PENDING auctions

2. **Theo Dõi PENDING:**

   - Click vào PENDING auction
   - Click "⭐ Thêm vào Danh Sách Theo Dõi"
   - Đợi auction bắt đầu

3. **Đấu Giá:**

   - Khi auction ACTIVE, click "Xem chi tiết & Đấu giá"
   - Nhập số tiền, click "Đặt Giá Ngay"
   - Xem real-time updates

4. **Kết Thúc:**
   - Auction tự động kết thúc khi:
     - Hết thời gian
     - Hoặc 20 phút không có bid
   - Nhận thông báo người thắng

---

## 📊 Database Schema

### **Các bảng chính:**

- `users` - Thông tin người dùng
- `auctions` - Thông tin đấu giá (có `last_bid_time`)
- `bids` - Lịch sử đặt giá
- `watchlist` - Danh sách theo dõi

### **Quan hệ:**

```
User (1) -----> (N) Auction (created_by)
User (1) -----> (N) Bid
User (1) -----> (N) Watchlist
Auction (1) --> (N) Bid
Auction (1) --> (N) Watchlist
```

---

## 🎨 Screenshots Gợi Ý

_(Có thể thêm screenshots của các trang sau:)_

1. Dashboard với PENDING và ACTIVE auctions
2. Admin tạo auction với datetime picker
3. Auction detail page cho PENDING auction
4. Real-time bidding với WebSocket
5. Admin panel với nút Start/End

---

## 📝 License

This project is for educational purposes.

---

## 👨‍💻 Support & Contact

Nếu gặp vấn đề, hãy kiểm tra:

1. ✅ Java version (phải >= 17)
2. ✅ MySQL đang chạy
3. ✅ Database `auction_db` đã được tạo
4. ✅ Port 8080 không bị chiếm bởi app khác
5. ✅ Dependencies đã được Maven download

**Debug Steps:**

```bash
# 1. Kiểm tra Java
java -version

# 2. Kiểm tra Maven
mvn -version

# 3. Kiểm tra MySQL
mysql -u root -p

# 4. Rebuild project
mvn clean install -DskipTests

# 5. Chạy với log verbose
mvn spring-boot:run -X
```

---

## 🎓 Kiến Thức Học Được

Project này cover các khái niệm quan trọng trong Java Spring Boot:

- ✅ **Spring Boot REST API**
- ✅ **Spring Security với BCrypt**
- ✅ **Spring Data JPA/Hibernate**
- ✅ **WebSocket Real-time Communication**
- ✅ **Spring Scheduler (@Scheduled)**
- ✅ **Thymeleaf Template Engine**
- ✅ **Repository Pattern**
- ✅ **Service Layer Architecture**
- ✅ **DTO Pattern**
- ✅ **Session Management**
- ✅ **Role-based Authorization**

---

## 🚀 Happy Coding!

**Developed with ❤️ using Spring Boot**

### **Author Notes:**

Hệ thống này được thiết kế để demo các tính năng hiện đại của Spring Boot:

- Real-time với WebSocket
- Scheduling với Spring Scheduler
- Security với Spring Security
- Clean Architecture với Service/Repository layers

Hy vọng project này hữu ích cho việc học tập và nghiên cứu! 🎉

---

**Version:** 1.0.0  
**Last Updated:** October 2025  
**Spring Boot Version:** 3.2.0  
**Java Version:** 17
