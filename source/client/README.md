# CLIENT - Frontend Application

Frontend của hệ thống đấu giá trực tuyến, sử dụng HTML5, CSS3, Vanilla JavaScript và WebSocket.

## 🛠️ Công nghệ

- **HTML5 + CSS3 + Vanilla JavaScript** - Không sử dụng framework
- **Bootstrap 5.3.0** - UI Framework
- **SockJS 1.6.1** - WebSocket fallback
- **STOMP.js 2.3.3** - WebSocket messaging protocol
- **http-server** - Static file server

## 📁 Cấu trúc

```
client/
├── package.json              # npm dependencies
├── public/                   # Web root directory
│   ├── index.html           # Landing page
│   ├── login.html           # Login page
│   ├── register.html        # Register page
│   ├── dashboard.html       # User dashboard
│   ├── auction-detail.html  # Auction detail & bidding
│   ├── my-bids.html         # User bid history
│   ├── watchlist.html       # Watchlist page
│   ├── admin/               # Admin pages
│   │   ├── dashboard.html   # Admin dashboard
│   │   ├── auctions.html    # Auction management
│   │   └── users.html       # User management
│   ├── css/
│   │   └── style.css        # Custom styles
│   └── js/
│       ├── config.js        # API endpoint config
│       ├── auth.js          # Authentication helpers
│       ├── header.js        # Shared header component
│       ├── admin-header.js  # Admin header component
│       ├── dashboard.js     # Dashboard logic + WebSocket
│       └── auction.js       # Auction detail + WebSocket
└── README.md                # This file
```

## 🚀 Cài đặt và Chạy

### 1. Cài đặt dependencies

```bash
npm install
```

### 2. Chạy development server

```bash
npm start
```

Server sẽ chạy tại: **http://localhost:3000**

### 3. Hoặc chạy thủ công với http-server

```bash
npx http-server public -p 3000
```

## ⚙️ Cấu hình

### API Endpoints

Mở file `public/js/config.js` để cấu hình:

```javascript
window.API_CONFIG = {
  BASE_URL: "http://localhost:8000", // Backend API URL
  WS_URL: "http://localhost:8000/ws", // WebSocket URL
};
```

## 📱 Các trang chính

### User Pages:

- **/** - Landing page
- **/login.html** - Đăng nhập
- **/register.html** - Đăng ký
- **/dashboard.html** - Dashboard với danh sách đấu giá
- **/auction-detail.html?id={id}** - Chi tiết đấu giá + đặt giá realtime
- **/my-bids.html** - Lịch sử đấu giá
- **/watchlist.html** - Danh sách theo dõi

### Admin Pages:

- **/admin/dashboard.html** - Admin dashboard
- **/admin/auctions.html** - Quản lý đấu giá
- **/admin/users.html** - Quản lý users

## 🔌 WebSocket Integration

Client sử dụng SockJS + STOMP để kết nối WebSocket với server:

```javascript
// Kết nối WebSocket
const socket = new SockJS("http://localhost:8000/ws");
const stompClient = Stomp.over(socket);

// Subscribe topic
stompClient.subscribe("/topic/auction/1", (message) => {
  const data = JSON.parse(message.body);
  // Handle realtime update
});
```

## 🎨 Features

### Dashboard:

- ✅ Hiển thị tất cả các cuộc đấu giá (ACTIVE, PENDING, ENDED)
- ✅ Smart sorting: Đang diễn ra → Sắp diễn ra → Đã kết thúc
- ✅ Lọc theo trạng thái: Tất cả / Đang diễn ra / Sắp diễn ra / Đã kết thúc
- ✅ Tìm kiếm realtime theo từ khóa (title, description)
- ✅ Thống kê chi tiết: Tổng số, Đang diễn ra, Sắp diễn ra, Đã kết thúc
- ✅ Visual indicators: Ribbon "Sắp diễn ra" cho PENDING auctions

### Realtime Updates:

- ✅ Tự động cập nhật giá đấu giá
- ✅ Thông báo toast khi có bid mới
- ✅ Countdown timer
- ✅ Auto-reload danh sách khi admin tạo đấu giá mới
- ✅ Console logging để debug

### UI/UX:

- ✅ Responsive design (Bootstrap 5)
- ✅ Shared header component (đồng bộ across pages)
- ✅ Toast notifications
- ✅ Loading states
- ✅ Form validation
- ✅ Màu sắc phân biệt trạng thái

## 📦 Dependencies

```json
{
  "dependencies": {
    "http-server": "^14.1.1"
  }
}
```

## 🔒 Authentication

Client lưu thông tin đăng nhập trong `localStorage`:

```javascript
localStorage.setItem("userId", userId);
localStorage.setItem("username", username);
localStorage.setItem("userRole", role);
```

Session được quản lý bởi cookie từ server (Spring Security).

## 🐛 Troubleshooting

**Port 8080 đã được sử dụng:**

```bash
npx http-server public -p 3000
```

**WebSocket không kết nối:**

- Kiểm tra server đã chạy chưa
- Kiểm tra URL trong `config.js`
- Xem console log (F12)

**CORS error:**

- Server đã config CORS cho `http://localhost:8080`
- Nếu đổi port, cần update CORS config ở server

## 📝 Development Notes

- Không sử dụng framework (React, Vue, Angular)
- Vanilla JavaScript để giảm complexity
- WebSocket cho realtime communication
- Bootstrap cho UI nhanh
- Modular JavaScript (tách file theo chức năng)

## 📚 Tài liệu tham khảo

- [SockJS Documentation](https://github.com/sockjs/sockjs-client)
- [STOMP.js Documentation](https://stomp-js.github.io/stomp-websocket/)
- [Bootstrap 5 Docs](https://getbootstrap.com/docs/5.3/)
