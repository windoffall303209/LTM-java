# 🔧 GIẢI THÍCH CƠ CHẾ TỰ ĐỘNG TẠO DATABASE - HIBERNATE JPA

## 📚 **1. ORM LÀ GÌ?**

**ORM (Object-Relational Mapping)** = Ánh xạ Object sang Relational Database

```
┌─────────────────┐         ┌──────────────┐         ┌─────────────────┐
│   Java Class    │   ←→    │  Hibernate   │   ←→    │  MySQL Table    │
│   (Entity)      │         │  JPA (ORM)   │         │  (Relational)   │
├─────────────────┤         ├──────────────┤         ├─────────────────┤
│ User.java       │         │  Mapping     │         │ users table     │
│ - userId        │   ←→    │  Metadata    │   ←→    │ - user_id       │
│ - username      │         │  Conversion  │         │ - username      │
│ - email         │         │              │         │ - email         │
└─────────────────┘         └──────────────┘         └─────────────────┘
```

**Không cần ORM (cách cũ):**
```java
// Phải viết SQL thủ công
String sql = "INSERT INTO users (username, email) VALUES (?, ?)";
PreparedStatement stmt = conn.prepareStatement(sql);
stmt.setString(1, user.getUsername());
stmt.setString(2, user.getEmail());
stmt.executeUpdate();
```

**Có ORM (Hibernate JPA):**
```java
// Hibernate tự động tạo SQL
userRepository.save(user);
// → Hibernate tự động: INSERT INTO users (username, email) VALUES ('admin', 'admin@...')
```

---

## 🏗️ **2. ANNOTATIONS - CHỈ DẪN CHO HIBERNATE**

### **Ví dụ thực tế:**

```java
@Entity                                    // ← Chỉ dẫn 1: "Đây là table trong DB"
@Table(name = "users")                     // ← Chỉ dẫn 2: "Tên table là 'users'"
public class User {

    @Id                                    // ← Chỉ dẫn 3: "Đây là Primary Key"
    @GeneratedValue(strategy = IDENTITY)   // ← Chỉ dẫn 4: "Auto increment"
    @Column(name = "user_id")              // ← Chỉ dẫn 5: "Tên cột là 'user_id'"
    private Long userId;

    @Column(unique = true, nullable = false, length = 50)
    //      ↑            ↑               ↑
    //      UNIQUE       NOT NULL        VARCHAR(50)
    private String username;

    @Column(precision = 15, scale = 2)
    //      ↑                    ↑
    //      DECIMAL(15, 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)           // ← Lưu enum dạng VARCHAR (không phải số)
    private Role role;

    @CreationTimestamp                     // ← Tự động set timestamp khi tạo
    private LocalDateTime createdAt;

    @ManyToOne                             // ← Quan hệ N-1 (Foreign Key)
    @JoinColumn(name = "created_by")       // ← Tên cột FK
    private User createdBy;
}
```

---

## ⚙️ **3. QUÁ TRÌNH TỰ ĐỘNG TẠO DATABASE**

### **Bước 1: Cấu hình trong `application.properties`**

```properties
# BƯỚC 1: Kết nối MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/auction_db
spring.datasource.username=root
spring.datasource.password=admin

# BƯỚC 2: CHỈ DẪN TỰ ĐỘNG TẠO SCHEMA ← ĐÂY LÀ KEY!
spring.jpa.hibernate.ddl-auto=update
#                             ↑
#                             Chế độ tự động tạo/update tables
```

### **Các chế độ `ddl-auto`:**

| Giá trị | Hành vi | Khi nào dùng |
|---------|---------|--------------|
| **`none`** | ❌ Không làm gì | Production (manual migration) |
| **`validate`** | ✅ Chỉ kiểm tra schema có khớp không | Production (safe) |
| **`update`** | ✅ Tự động tạo/cập nhật tables | **Development** ← ĐANG DÙNG |
| **`create`** | ⚠️ Xóa hết + tạo mới mỗi lần start | Testing (clean state) |
| **`create-drop`** | ⚠️ Tạo khi start, xóa khi stop | Integration tests |

---

### **Bước 2: Hibernate đọc Entity Classes**

Khi Spring Boot khởi động:

```
1. Spring Boot scan package "com.auction.model"
   → Tìm thấy: User.java, Auction.java, Bid.java, Watchlist.java

2. Hibernate đọc @Entity annotations
   → "À, có 4 Entity classes = cần 4 tables"

3. Hibernate phân tích từng field + annotation
   User.java:
   - @Id userId → PRIMARY KEY user_id BIGINT AUTO_INCREMENT
   - @Column username → username VARCHAR(50) UNIQUE NOT NULL
   - @Column email → email VARCHAR(100) UNIQUE NOT NULL
   - @ManyToOne createdBy → FOREIGN KEY created_by → users(user_id)

4. Hibernate tạo SQL CREATE TABLE
```

---

### **Bước 3: Hibernate tự động sinh SQL**

**Hibernate tự động tạo SQL như sau:**

```sql
-- Từ User.java annotation:
CREATE TABLE users (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    balance DECIMAL(15,2) DEFAULT 0.00,
    role VARCHAR(20) DEFAULT 'USER',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Từ Auction.java annotation:
CREATE TABLE auctions (
    auction_id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    starting_price DECIMAL(15,2) NOT NULL,
    current_price DECIMAL(15,2) NOT NULL,
    highest_bidder_id BIGINT,
    created_by BIGINT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- ... more columns ...
    PRIMARY KEY (auction_id),
    FOREIGN KEY (highest_bidder_id) REFERENCES users(user_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Từ Bid.java annotation:
CREATE TABLE bids (
    bid_id BIGINT NOT NULL AUTO_INCREMENT,
    auction_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    bid_amount DECIMAL(15,2) NOT NULL,
    bid_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (bid_id),
    FOREIGN KEY (auction_id) REFERENCES auctions(auction_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_auction_bid_time (auction_id, bid_time DESC),
    INDEX idx_user_bid_time (user_id, bid_time DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Từ Watchlist.java annotation:
CREATE TABLE watchlist (
    watchlist_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    auction_id BIGINT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (watchlist_id),
    UNIQUE KEY uk_user_auction (user_id, auction_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (auction_id) REFERENCES auctions(auction_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### **Bước 4: Hibernate thực thi SQL**

```
1. Hibernate kết nối MySQL: jdbc:mysql://localhost:3306/auction_db

2. Kiểm tra tables đã tồn tại chưa?
   - Nếu CHƯA → Chạy CREATE TABLE
   - Nếu ĐÃ CÓ + ddl-auto=update → So sánh schema:
     * Có cột mới trong Entity? → ALTER TABLE ADD COLUMN
     * Có cột cũ bị xóa? → KHÔNG XÓA (an toàn)
     * Có FK mới? → ALTER TABLE ADD CONSTRAINT

3. Log ra console (nếu show-sql=true):
   Hibernate: CREATE TABLE users (...)
   Hibernate: CREATE TABLE auctions (...)
   Hibernate: ALTER TABLE auctions ADD CONSTRAINT FK_...
```

---

## 📋 **4. VÍ DỤ CHI TIẾT: TỪ ANNOTATION → SQL**

### **Java Entity:**

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role = Role.USER;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
}
```

### **↓ Hibernate chuyển thành SQL:**

```sql
CREATE TABLE users (
    -- @Id + @GeneratedValue
    user_id BIGINT NOT NULL AUTO_INCREMENT,

    -- @Column(unique=true, nullable=false, length=50)
    username VARCHAR(50) NOT NULL,

    -- @Column(precision=15, scale=2) + default value
    balance DECIMAL(15,2) DEFAULT 0.00,

    -- @Enumerated(STRING) + @Column(length=20) + default
    role VARCHAR(20) DEFAULT 'USER',

    -- @CreationTimestamp + @Column(updatable=false)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- @ManyToOne + @JoinColumn
    created_by BIGINT,

    -- Constraints
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_username (username),
    FOREIGN KEY fk_created_by (created_by) REFERENCES users(user_id)
);
```

---

## 🔄 **5. QUÁ TRÌNH RUNTIME (KHI CHẠY SERVER)**

### **Timeline khi `mvn spring-boot:run`:**

```
[0s] Spring Boot starts
      ↓
[1s] 🔍 Scan @Entity classes
      → Found: User, Auction, Bid, Watchlist
      ↓
[2s] 🔌 Connect to MySQL: jdbc:mysql://localhost:3306/auction_db
      ↓
[3s] 📊 Hibernate reads Entity metadata
      → Analyze @Table, @Column, @Id, @ManyToOne...
      ↓
[4s] 🏗️ Generate DDL SQL
      ✅ ddl-auto=update
      → Check if tables exist
      → Generate CREATE TABLE / ALTER TABLE statements
      ↓
[5s] 🚀 Execute SQL on MySQL
      Hibernate: CREATE TABLE users (...)
      Hibernate: CREATE TABLE auctions (...)
      Hibernate: CREATE TABLE bids (...)
      Hibernate: CREATE TABLE watchlist (...)
      ↓
[6s] ✅ Tables created successfully
      ↓
[7s] 🎯 Run @Component classes
      → DataInitializer.java executes
      → Insert sample data (admin, user1, user2, 5 auctions)
      ↓
[8s] 🌐 Server ready at http://localhost:8000
      ✅ Database fully initialized!
```

---

## 🎯 **6. TẠI SAO CẦN TỰ ĐỘNG?**

### **So sánh: Manual vs Auto**

#### **❌ CÁCH CŨ (Manual SQL):**

```java
// 1. Viết Entity
public class User { ... }

// 2. Viết SQL thủ công
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    ...
);

// 3. Thêm field mới vào Entity
public class User {
    private String phoneNumber; // ← Field mới
}

// 4. Phải nhớ viết SQL migration
ALTER TABLE users ADD COLUMN phone_number VARCHAR(20);

// 5. Dễ quên, dễ sai khác giữa code vs DB
```

#### **✅ CÁCH MỚI (Hibernate Auto):**

```java
// 1. Viết Entity
@Entity
public class User { ... }

// 2. Hibernate tự động tạo table
// → KHÔNG CẦN VIẾT SQL!

// 3. Thêm field mới
@Entity
public class User {
    @Column(length = 20)
    private String phoneNumber; // ← Field mới
}

// 4. Hibernate tự động ALTER TABLE
// → mvn spring-boot:run
// → Hibernate: ALTER TABLE users ADD COLUMN phone_number VARCHAR(20)

// 5. Luôn đồng bộ giữa code và DB
```

---

## 📝 **7. XEM LOG HIBERNATE TẠO SQL**

Trong `application.properties`:

```properties
# Bật log SQL
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
```

**Output trong console khi chạy server:**

```sql
Hibernate:
    CREATE TABLE users (
        user_id BIGINT NOT NULL AUTO_INCREMENT,
        username VARCHAR(50) NOT NULL,
        email VARCHAR(100) NOT NULL,
        password VARCHAR(255) NOT NULL,
        balance DECIMAL(15,2) DEFAULT 0.00,
        role VARCHAR(20) DEFAULT 'USER',
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (user_id),
        UNIQUE KEY uk_username (username)
    ) ENGINE=InnoDB

Hibernate:
    CREATE TABLE auctions (
        auction_id BIGINT NOT NULL AUTO_INCREMENT,
        ...
    ) ENGINE=InnoDB

Hibernate:
    ALTER TABLE auctions
    ADD CONSTRAINT fk_highest_bidder
    FOREIGN KEY (highest_bidder_id)
    REFERENCES users(user_id)
```

---

## 🎓 **8. TÓM TẮT**

### **Tại sao tự động được?**

1. **ORM (Hibernate JPA)** đọc Java classes với `@Entity`
2. **Metadata annotations** (`@Table`, `@Column`, `@Id`) chỉ dẫn cấu trúc table
3. **`ddl-auto=update`** ra lệnh Hibernate tự động tạo/cập nhật schema
4. **Hibernate sinh SQL** dựa trên annotations
5. **Thực thi SQL** lên MySQL khi server start

### **Lợi ích:**

✅ Không cần viết SQL thủ công
✅ Luôn đồng bộ giữa code Java và Database
✅ Tự động tạo Foreign Keys, Indexes
✅ Dễ thay đổi schema (thêm/sửa field)
✅ Database-agnostic (MySQL, PostgreSQL, H2...)

### **Lưu ý:**

⚠️ `ddl-auto=update` chỉ dùng cho **Development**
⚠️ Production nên dùng **migration tools** (Flyway, Liquibase)
⚠️ Hibernate không tự động XÓA cột (chỉ thêm cột mới)

---

## 🔍 **9. KIỂM TRA THỰC TẾ**

Sau khi chạy server, check MySQL:

```sql
-- Xem các tables đã tạo
SHOW TABLES;
-- Output: auctions, bids, users, watchlist

-- Xem cấu trúc table users
DESC users;
-- Output:
-- user_id | bigint | PK | auto_increment
-- username | varchar(50) | UNIQUE
-- email | varchar(100) | UNIQUE
-- ...

-- Xem foreign keys
SHOW CREATE TABLE auctions;
-- Output sẽ có:
-- CONSTRAINT fk_highest_bidder FOREIGN KEY (highest_bidder_id) REFERENCES users(user_id)
```

---

**Kết luận:** Hibernate JPA = "Phép màu" tự động chuyển Java code → SQL DDL! 🎩✨
