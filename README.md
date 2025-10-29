# 🔨 Auction System - Hệ Thống Đấu Giá Trực Tuyến

Hệ thống đấu giá trực tuyến real-time với Spring Boot, WebSocket và MySQL.

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

---

## 📖 Giới Thiệu

**Auction System** là một ứng dụng web đấu giá trực tuyến với các tính năng:

- Đấu giá real-time qua WebSocket
- Đặt giá tự động (Auto Bid)
- Quản lý danh sách theo dõi (Watchlist)
- Phân quyền Admin/User
- Giao diện responsive với Bootstrap 5

---

## ✨ Tính Năng

### 👤 **Chức năng USER:**

- ✅ Đăng ký/Đăng nhập (Spring Security + BCrypt)
- ✅ Xem danh sách đấu giá đang diễn ra
- ✅ Xem chi tiết đấu giá với cập nhật real-time
- ✅ Đặt giá thủ công và Quick Bid
- ✅ **Đặt giá tự động** (Auto Bid) - Tự động đặt giá khi bị vượt
- ✅ Xem lịch sử đặt giá của mình (My Bids)
- ✅ Quản lý danh sách theo dõi (Watchlist)
- ✅ Xem số dư tài khoản

### 👑 **Chức năng ADMIN:**

- ✅ Dashboard quản trị với thống kê tổng quan
- ✅ Tạo/Sửa/Xóa đấu giá (CRUD)
- ✅ Start/Stop đấu giá
- ✅ Quản lý người dùng
- ✅ Ban/Unban user
- ✅ Cập nhật số dư user

---

## 🛠️ Công Nghệ Sử Dụng

- **Backend:** Spring Boot 3.2.0
- **Security:** Spring Security + BCrypt
- **Database:** MySQL 8.0
- **ORM:** Spring Data JPA / Hibernate
- **Frontend:** Thymeleaf + Bootstrap 5
- **Real-time:** WebSocket (SockJS + STOMP)
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
Password: admin
Số dư: 100,000,000 VND
Quyền: ADMIN (Toàn quyền quản trị)
```

### **Demo User Accounts:**

```
Username: user1
Password: 123456
Số dư: 20,000,000 VND
Quyền: USER

Username: user2
Password: 123456
Số dư: 15,000,000 VND
Quyền: USER
```

### **Đăng ký tài khoản mới:**

- Truy cập: http://localhost:8080/register
- Tài khoản mới sẽ nhận **10,000,000 VND** ban đầu

---

## 📁 Cấu Trúc Project

```
auction-system/
├── src/
│   ├── main/
│   │   ├── java/com/auction/
│   │   │   ├── config/              # Cấu hình (Security, WebSocket, DataInit)
│   │   │   ├── controller/          # REST Controllers & View Controllers
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── model/               # JPA Entities
│   │   │   ├── repository/          # JPA Repositories
│   │   │   ├── service/             # Business Logic
│   │   │   ├── websocket/           # WebSocket Handlers
│   │   │   └── AuctionSystemApplication.java
│   │   └── resources/
│   │       ├── templates/           # Thymeleaf HTML templates
│   │       │   ├── admin/           # Admin pages
│   │       │   ├── auction-detail.html
│   │       │   ├── dashboard.html
│   │       │   ├── login.html
│   │       │   ├── my-bids.html
│   │       │   ├── register.html
│   │       │   └── watchlist.html
│   │       ├── static/
│   │       │   ├── css/             # CSS files
│   │       │   └── js/              # JavaScript files
│   │       └── application.properties
│   └── test/
├── pom.xml                          # Maven dependencies
└── README.md
```

---

## 🌟 Các Trang Chính

### **User Pages:**

| Trang            | Đường dẫn       | Mô tả                           |
| ---------------- | --------------- | ------------------------------- |
| Trang chủ        | `/`             | Landing page                    |
| Đăng nhập        | `/login`        | Form đăng nhập                  |
| Đăng ký          | `/register`     | Form đăng ký tài khoản mới      |
| Dashboard        | `/dashboard`    | Danh sách đấu giá đang diễn ra  |
| Chi tiết đấu giá | `/auction/{id}` | Xem chi tiết và đặt giá         |
| Lượt đặt giá     | `/my-bids`      | Xem lịch sử đặt giá của mình    |
| Theo dõi         | `/watchlist`    | Danh sách đấu giá đang theo dõi |

### **Admin Pages:**

| Trang              | Đường dẫn         | Mô tả                    |
| ------------------ | ----------------- | ------------------------ |
| Admin Dashboard    | `/admin`          | Tổng quan hệ thống       |
| Quản lý Đấu Giá    | `/admin/auctions` | CRUD đấu giá             |
| Quản lý Người Dùng | `/admin/users`    | Quản lý users, ban/unban |

---

## 📡 API Endpoints

### **Authentication:**

- `POST /api/auth/register` - Đăng ký user mới
- `POST /api/auth/login` - Đăng nhập (Spring Security)
- `POST /api/auth/logout` - Đăng xuất

### **Auctions:**

- `GET /api/auctions` - Lấy danh sách đấu giá active
- `GET /api/auctions/{id}` - Lấy chi tiết đấu giá
- `GET /api/auctions/search?keyword=...` - Tìm kiếm

### **Bidding:**

- `POST /api/bids` - Đặt giá
- `GET /api/bids/auction/{auctionId}` - Lấy lịch sử bid
- `GET /api/bids/user?userId=...` - Lấy bids của user

### **Auto Bid:**

- `POST /api/autobids` - Tạo auto bid
- `GET /api/autobids/auction/{auctionId}` - Lấy auto bid
- `DELETE /api/autobids/{id}` - Hủy auto bid

### **Watchlist:**

- `POST /api/watchlist` - Thêm vào watchlist
- `GET /api/watchlist/user?userId=...` - Lấy watchlist
- `DELETE /api/watchlist/{id}` - Xóa khỏi watchlist

### **Admin:**

- `POST /api/admin/auctions` - Tạo đấu giá mới
- `PUT /api/admin/auctions/{id}` - Cập nhật đấu giá
- `DELETE /api/admin/auctions/{id}` - Xóa đấu giá
- `POST /api/admin/auctions/{id}/start` - Bắt đầu đấu giá
- `POST /api/admin/auctions/{id}/end` - Kết thúc đấu giá
- `GET /api/admin/users` - Lấy danh sách users
- `POST /api/admin/users/{id}/toggle-status` - Ban/Unban user
- `POST /api/admin/users/{id}/update-balance` - Cập nhật số dư

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

### **Lỗi: Table doesn't exist**

- Kiểm tra `spring.jpa.hibernate.ddl-auto=update` trong `application.properties`
- Database sẽ tự động tạo tables khi chạy lần đầu

### **Lỗi: Java version**

```bash
# Kiểm tra version Java
java -version

# Phải là Java 17 trở lên
```

### **Lỗi: Maven command not found**

```bash
# Kiểm tra Maven đã cài chưa
mvn -version

# Kiểm tra PATH environment variable
```

---

## 📚 Tài Liệu Tham Khảo

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Thymeleaf](https://www.thymeleaf.org/documentation.html)
- [Bootstrap 5](https://getbootstrap.com/docs/5.3/)
- [WebSocket với Spring](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket)

---

## 🎯 Các Tính Năng Nổi Bật

### 1. **Real-time Bidding**

- Sử dụng WebSocket (SockJS + STOMP)
- Cập nhật giá ngay lập tức cho tất cả users đang xem
- Không cần refresh trang

### 2. **Auto Bid (Đặt giá tự động)**

- User set giá tối đa và bước tăng
- Hệ thống tự động đặt giá khi bị vượt
- Tiết kiệm thời gian theo dõi

### 3. **Watchlist**

- Lưu các đấu giá quan tâm
- Dễ dàng theo dõi và quay lại

### 4. **Security**

- Password mã hóa BCrypt
- Session management
- Phân quyền rõ ràng (USER/ADMIN)

### 5. **Responsive UI**

- Bootstrap 5
- Hoạt động tốt trên mobile, tablet, desktop

---

## 📝 License

This project is for educational purposes.

---

## 👨‍💻 Support

Nếu gặp vấn đề, hãy kiểm tra:

1. Java version (phải >= 17)
2. MySQL đang chạy
3. Database `auction_db` đã được tạo
4. Port 8080 không bị chiếm bởi app khác

---

## 🚀 Happy Coding!

**Developed with ❤️ using Spring Boot**
