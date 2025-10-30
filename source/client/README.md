# Client Module - Web Frontend

Giao diện web cho hệ thống đấu giá trực tuyến sử dụng HTML, CSS, Bootstrap và JavaScript.

## Công Nghệ

- **HTML5** - Cấu trúc trang
- **CSS3 + Bootstrap 5** - Styling và responsive
- **JavaScript (ES6+)** - Logic và WebSocket client
- **Chart.js** - Biểu đồ (optional)

## Cấu Trúc

```
client/
├── index.html              # Landing page
├── login.html              # Đăng nhập
├── register.html           # Đăng ký
├── dashboard.html          # Main app
├── admin.html              # Admin panel
│
├── css/
│   └── style.css           # Custom styles
│
├── js/
│   ├── config.js           # Configuration
│   ├── utils.js            # Utility functions
│   ├── socket-client.js    # WebSocket client
│   ├── auth-handler.js     # Login/Register
│   ├── auction-handler.js  # Auction display
│   ├── bid-handler.js      # Bid placement
│   ├── ui-updater.js       # UI updates
│   └── admin-handler.js    # Admin functions
│
└── assets/
    ├── images/
    └── sounds/
```

## Cài Đặt

### Bước 1: Install http-server

```bash
cd source/client
npm install
```

### Bước 2: Chạy Client

```bash
npm start
# Hoặc
npx http-server -p 3000
```

### Bước 3: Mở Browser

```
http://localhost:3000
```

## Cấu Hình

Chỉnh sửa `js/config.js` để thay đổi server URL:

```javascript
const CONFIG = {
    SERVER_URL: 'ws://localhost:8888',  // Địa chỉ server
    // ...
};
```

## Tài Khoản Test

Sau khi chạy `sample_data.sql`:

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | admin |
| alice | admin123 | user |
| bob | admin123 | user |
| charlie | admin123 | user |

## Tính Năng

### User Features
- ✅ Đăng ký/Đăng nhập
- ✅ Xem danh sách đấu giá
- ✅ Đặt giá thủ công
- ✅ Đặt giá tự động (auto-bid)
- ✅ Xem lịch sử bids
- ✅ Thông báo realtime
- ✅ Countdown timer

### Admin Features
- ✅ Tạo phiên đấu giá
- ✅ Quản lý auctions
- ✅ Quản lý users
- ✅ Xem thống kê
- ✅ View logs

## Troubleshooting

### Lỗi: Cannot connect to WebSocket

- Kiểm tra server đã chạy chưa
- Kiểm tra `SERVER_URL` trong `config.js`
- Kiểm tra firewall

### Lỗi: CORS

- Sử dụng http-server thay vì mở file HTML trực tiếp
- WebSocket không có CORS issue

### Lỗi: Page không load

- Đảm bảo tất cả files JS đã được include đúng thứ tự
- Check console log trong browser (F12)

