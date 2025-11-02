# Auction System - Foundation Code

This is the **FOUNDATION CODE** for the Auction System project. This code provides the basic structure and configuration needed to get started. Team members will build upon this foundation to implement complete features.

## 📋 What's Included

### Backend (Spring Boot)
- ✅ **Main Application**: `AuctionSystemApplication.java`
- ✅ **Configuration**:
  - `SecurityConfig.java` - Basic security setup (permits all for now)
  - `WebConfig.java` - CORS configuration
  - `WebSocketConfig.java` - WebSocket setup for real-time features
  - `DataInitializer.java` - Skeleton for initial data
- ✅ **Models**:
  - `User.java` - User entity with Role (USER/ADMIN)
  - `Auction.java` - Auction entity with basic fields
- ✅ **Repositories**:
  - `UserRepository.java` - Basic user queries
  - `AuctionRepository.java` - Basic auction queries
- ✅ **DTOs**:
  - `ApiResponse.java` - Standard API response wrapper
  - `UserDTO.java` - User data transfer object
  - `AuctionDTO.java` - Auction data transfer object
  - `LoginRequest.java` - Login request payload
  - `RegisterRequest.java` - Registration request payload
- ✅ **Services**:
  - `CustomUserDetailsService.java` - Spring Security user details (complete)
- ✅ **WebSocket**:
  - `WebSocketController.java` - Real-time communication infrastructure
- ✅ **Exception Handling**:
  - `GlobalExceptionHandler.java` - Centralized exception handling
  - `ResourceNotFoundException.java` - For 404 errors
  - `BadRequestException.java` - For 400 errors
  - `UnauthorizedException.java` - For 401 errors
  - `ForbiddenException.java` - For 403 errors
- ✅ **Dependencies**: All required dependencies in `pom.xml`
- ✅ **Configuration**: `application.properties` with MySQL database

### Frontend
- ✅ **HTML Pages**:
  - `index.html` - Landing page
  - `login.html` - Login page (skeleton)
  - `register.html` - Registration page (skeleton)
- ✅ **JavaScript**:
  - `config.js` - API endpoints and configuration
  - `auth.js` - Authentication utilities
  - `websocket.js` - WebSocket client utilities
- ✅ **CSS**:
  - `style.css` - Basic styling
- ✅ **Package.json**: For running live-server

### Other
- ✅ `.gitignore` - Standard Java/Node.js gitignore
- ✅ This README

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Node.js (for frontend live-server)

### Backend Setup

1. Navigate to server directory:
```bash
cd foundation-code/server
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

You can access H2 console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:auctiondb`
- Username: `sa`
- Password: (leave empty)

### Frontend Setup

1. Navigate to client directory:
```bash
cd foundation-code/client
```

2. Install dependencies:
```bash
npm install
```

3. Start live-server:
```bash
npm start
```

The frontend will open at `http://localhost:5500`

## 📁 Project Structure

```
foundation-code/
├── server/                          # Backend (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/auction/
│   │   │   │   ├── AuctionSystemApplication.java
│   │   │   │   ├── config/        # Configuration classes
│   │   │   │   ├── model/         # Entity classes
│   │   │   │   ├── repository/    # Data access layer
│   │   │   │   ├── dto/           # Data transfer objects
│   │   │   │   ├── service/       # Business logic
│   │   │   │   └── controller/    # REST API endpoints (empty - to be added)
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                  # Tests (to be added)
│   └── pom.xml
│
├── client/                          # Frontend
│   ├── public/
│   │   ├── index.html
│   │   ├── login.html
│   │   ├── register.html
│   │   ├── css/
│   │   │   └── style.css
│   │   └── js/
│   │       ├── config.js
│   │       └── auth.js
│   └── package.json
│
├── .gitignore
└── README.md
```

## 👥 Work Division

See `WORK_DIVISION_PLAN.md` in the parent directory for detailed task assignments.

### Quick Overview:

**Person 1 - Admin Features:**
- Create `AdminController.java`
- Implement admin backend logic
- Create admin frontend pages (`admin/dashboard.html`, etc.)

**Person 2 - User Features:**
- Create `Bid.java`, `Watchlist.java` models
- Create `BidController.java`, `WatchlistController.java`
- Create bidding frontend pages (`my-bids.html`, `watchlist.html`)

**Person 3 - Core Features:**
- Create `AuthController.java`, `UserController.java`, `AuctionController.java`
- Implement `UserService.java`, `AuctionService.java`
- Create `WebSocketController.java` for real-time updates
- Implement `AuctionSchedulerService.java`
- Create main frontend pages (`dashboard.html`, `auction-detail.html`)

## 🔧 What Needs to be Implemented

### Backend TODOs:
- [ ] JWT authentication in `SecurityConfig.java`
- [ ] Controllers (Auth, User, Auction, Admin, Bid, Watchlist)
- [ ] Services (UserService, AuctionService, BidService, etc.)
- [ ] WebSocket message handlers
- [ ] Scheduler for auto-ending auctions
- [ ] Data initialization in `DataInitializer.java`
- [ ] Input validation
- [ ] Error handling
- [ ] Tests

### Frontend TODOs:
- [ ] Login/Register logic
- [ ] Dashboard page
- [ ] Auction detail page
- [ ] Bidding functionality
- [ ] Admin pages
- [ ] WebSocket client connection
- [ ] Real-time updates
- [ ] Form validations
- [ ] Error handling

## 🛠️ Development Guidelines

### Backend:
1. Follow REST API conventions
2. Use `ApiResponse` wrapper for all API responses
3. Add validation annotations to DTOs
4. Use meaningful HTTP status codes
5. Add comments for complex logic
6. Follow Java naming conventions

### Frontend:
1. Use the utilities in `auth.js` for authentication checks
2. Use `CONFIG.ENDPOINTS` for API calls
3. Handle loading states and errors
4. Show user-friendly error messages
5. Make UI responsive

### Git:
1. Work on your assigned branch
2. Commit frequently with meaningful messages
3. Sync with main daily
4. Create Pull Request when feature is complete

## 📚 API Documentation (To Be Implemented)

### Authentication Endpoints (Person 3)
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login
- `POST /api/auth/logout` - Logout
- `GET /api/auth/profile` - Get user profile

### Auction Endpoints (Person 3)
- `GET /api/auctions` - Get all active auctions
- `GET /api/auctions/{id}` - Get auction details
- `GET /api/auctions/search?keyword=...` - Search auctions

### Bid Endpoints (Person 2)
- `POST /api/bids` - Place bid
- `GET /api/bids/auction/{auctionId}` - Get bid history
- `GET /api/bids/user` - Get user's bids

### Admin Endpoints (Person 1)
- `POST /api/admin/auctions` - Create auction
- `PUT /api/admin/auctions/{id}` - Update auction
- `DELETE /api/admin/auctions/{id}` - Delete auction
- `GET /api/admin/users` - Get all users
- `POST /api/admin/users/{id}/toggle-status` - Ban/Unban user

## 🧪 Testing

### Backend Testing:
```bash
cd server
mvn test
```

### Test with Postman/Thunder Client:
1. Import the endpoints from above
2. Test each endpoint
3. Verify response format

### Frontend Testing:
1. Open browser DevTools
2. Check console for errors
3. Test all user flows
4. Test on different browsers

## ❗ Important Notes

1. **This is NOT complete code** - It's a foundation. You need to implement the logic.
2. **Database**: Currently using H2 in-memory. Data will be lost on restart. For production, switch to MySQL/PostgreSQL.
3. **Security**: SecurityConfig currently permits all requests. You need to implement JWT authentication.
4. **TODOs**: Look for `TODO` comments in code for guidance on what to implement.
5. **Dependencies**: All major dependencies are included. Add more if needed.

## 🆘 Troubleshooting

### Backend won't start:
- Check if port 8080 is available
- Check Java version: `java -version` (should be 17+)
- Run `mvn clean install` again

### Frontend won't load:
- Check if backend is running
- Check CORS configuration in `WebConfig.java`
- Check browser console for errors

### Database errors:
- Check `application.properties` configuration
- Access H2 console to verify tables are created

## 📞 Communication

- Daily standups to sync progress
- Use team chat for quick questions
- Create GitHub issues for bugs
- Review each other's Pull Requests

## ✅ Definition of Done

A feature is complete when:
- Code is written and tested
- No compiler/linter errors
- Code reviewed by team member
- PR merged to main
- Documentation updated (if needed)

---

**Good luck team! Build something awesome! 🚀**

*Last updated: 2025-11-01*
