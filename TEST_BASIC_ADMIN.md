# 🧪 HƯỚNG DẪN TEST BASIC-PROJECT-ADMIN

## ✅ CHECKLIST TRƯỚC KHI TEST

### 1. Kiểm tra MySQL đang chạy

```bash
# Windows
net start MySQL80

# Hoặc kiểm tra trong Services
services.msc
```

### 2. Tạo Database

```sql
-- Đăng nhập MySQL
mysql -u root -p

-- Tạo database
CREATE DATABASE auction_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Kiểm tra
SHOW DATABASES LIKE 'auction_db';

-- Thoát
exit;
```

### 3. Kiểm tra cấu hình database

File: `basic-project -admin/source/server/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auction_db
spring.datasource.username=root
spring.datasource.password=admin
```

**Đảm bảo password MySQL khớp với config!**

---

## 🚀 BƯỚC 1: CHẠY SERVER

```bash
# Di chuyển vào thư mục server
cd "basic-project -admin/source/server"

# Build project
mvn clean install

# Chạy server
mvn spring-boot:run
```

**Chờ server khởi động, bạn sẽ thấy:**

```
===== STARTING DATA INITIALIZATION =====
✅ Created admin account: username=admin, password=admin123
✅ Created demo user: username=user1, password=123456
✅ Created demo user: username=user2, password=123456
...
Auction System Server đã khởi động tại http://localhost:8000
```

**Nếu thấy các dòng này → Server chạy thành công!**

---

## 🧪 BƯỚC 2: TEST API BẰNG CURL/POSTMAN

### Test 1: Health Check

```bash
curl http://localhost:8000/api/health
```

**Expected:** `{"status":"UP"}`

### Test 2: Register User Mới

```bash
curl -X POST http://localhost:8000/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"testuser\",\"password\":\"123456\",\"fullName\":\"Test User\",\"email\":\"test@example.com\"}"
```

**Expected:**
```json
{
  "success": true,
  "message": "Đăng ký thành công",
  "data": {
    "userId": 4,
    "username": "testuser",
    ...
  }
}
```

### Test 3: Login với Admin

```bash
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

**Expected:**
```json
{
  "success": true,
  "message": "Đăng nhập thành công",
  "data": {
    "userId": 1,
    "username": "admin",
    "role": "ADMIN",
    ...
  }
}
```

### Test 4: Get All Users (Admin API)

```bash
curl http://localhost:8000/api/admin/users
```

**Expected:** Danh sách users

### Test 5: Get All Auctions (Admin API)

```bash
curl http://localhost:8000/api/admin/auctions/all
```

**Expected:** Danh sách auctions (hoặc rỗng nếu chưa có)

---

## 🌐 BƯỚC 3: CHẠY FRONTEND

**Mở terminal mới** (giữ server chạy):

```bash
# Di chuyển vào thư mục client
cd "basic-project -admin/source/client"

# Cài dependencies (chỉ lần đầu)
npm install

# Chạy client
npm start
```

Client sẽ chạy tại: `http://localhost:3000` hoặc `http://localhost:8080`

---

## 🔐 BƯỚC 4: LOGIN VÀO ADMIN DASHBOARD

1. **Mở trình duyệt:** `http://localhost:3000/login.html`

2. **Đăng nhập với:**
   - Username: `admin`
   - Password: `admin123`

3. **Sau khi login, navigate đến:**
   - Admin Dashboard: `http://localhost:3000/admin/dashboard.html`
   - Auction Management: `http://localhost:3000/admin/auctions.html`
   - User Management: `http://localhost:3000/admin/users.html`

---

## ❌ TROUBLESHOOTING

### Lỗi 1: Cannot connect to database

**Nguyên nhân:** MySQL chưa chạy hoặc password sai

**Giải pháp:**
```bash
# Kiểm tra MySQL
net start MySQL80

# Kiểm tra password trong application.properties
# Đảm bảo khớp với password MySQL của bạn
```

### Lỗi 2: Port 8000 already in use

**Nguyên nhân:** Có process khác đang dùng port 8000

**Giải pháp:**
```bash
# Tìm process
netstat -ano | findstr :8000

# Kill process
taskkill /PID <PID> /F
```

### Lỗi 3: Login không thành công

**Kiểm tra:**

1. **Backend có chạy không?**
   ```bash
   curl http://localhost:8000/api/health
   ```

2. **Database có user admin chưa?**
   ```sql
   USE auction_db;
   SELECT * FROM users WHERE username = 'admin';
   ```

3. **Console có lỗi không?**
   - Xem terminal nơi chạy `mvn spring-boot:run`
   - Xem browser console (F12)

4. **API endpoint đúng chưa?**
   - Kiểm tra file: `source/client/public/js/config.js`
   ```javascript
   const API_BASE_URL = 'http://localhost:8000';
   ```

### Lỗi 4: CORS error

**Kiểm tra WebConfig:**
```java
// WebConfig.java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000", "http://localhost:8080")
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowCredentials(true);
}
```

### Lỗi 5: 401 Unauthorized

**Kiểm tra SecurityConfig:**
```java
// SecurityConfig.java - phải có
.authorizeHttpRequests(auth -> auth
    .anyRequest().permitAll()  // Allow all for testing
)
```

---

## 📝 TEST CHECKLIST

- [ ] MySQL đang chạy
- [ ] Database `auction_db` đã được tạo
- [ ] Server build thành công (`mvn clean install`)
- [ ] Server chạy không lỗi (`mvn spring-boot:run`)
- [ ] Thấy log "Created admin account"
- [ ] Health check API hoạt động
- [ ] Register API hoạt động
- [ ] Login API hoạt động (test bằng curl)
- [ ] Frontend chạy được (`npm start`)
- [ ] Login frontend thành công
- [ ] Admin dashboard hiển thị
- [ ] Auction management hoạt động
- [ ] User management hoạt động

---

## 🎯 ACCOUNTS MẶC ĐỊNH

| Username | Password | Role | Balance |
|----------|----------|------|---------|
| admin | admin123 | ADMIN | 2,000,000,000 VND |
| user1 | 123456 | USER | 2,000,000,000 VND |
| user2 | 123456 | USER | 2,000,000,000 VND |

---

## 📞 CẦN HELP?

Nếu vẫn gặp lỗi, check:

1. **Server logs** - Xem terminal chạy `mvn spring-boot:run`
2. **Browser console** - Mở DevTools (F12) → Console tab
3. **Network tab** - Xem request/response từ frontend tới backend
4. **Database** - Kiểm tra table `users` có data chưa

**Logs quan trọng cần tìm:**
```
✅ Created admin account: username=admin, password=admin123
✅ DataInitializer completed successfully
🚀 Tomcat started on port(s): 8000
```

Nếu không thấy các log này → Server có lỗi!
