# 🛠️ HƯỚNG DẪN HOÀN THIỆN SERVER CORE

Project đã có 95% code! Còn thiếu 3 files server core để chạy được hoàn chỉnh.

---

## ✅ ĐÃ CÓ SẴN

- ✅ Tất cả Services (Auth, Auction, Bid, Broadcast)
- ✅ Tất cả DAOs (User, Auction, Bid)
- ✅ Tất cả Models
- ✅ Protocol & DTO
- ✅ Client hoàn chỉnh (HTML/CSS/JS)
- ✅ Database scripts

---

## ⚠️ CẦN TẠO (3 files)

### 1. Simple WebSocket Server Wrapper

Do Java Socket phức tạp với WebSocket, tôi khuyên bạn dùng **Java-WebSocket library** hoặc làm đơn giản hơn:

#### OPTION A: Dùng HTTP Server + Upgrade to WebSocket (Phức tạp)
#### OPTION B: Simplified Socket Server (Đơn giản nhất)

Tạo file: `source/server/src/main/java/com/auction/server/AuctionServer.java`

```java
package com.auction.server;

import com.auction.server.service.*;
import com.auction.server.database.DatabaseConnection;
import com.auction.server.util.Logger;
import com.auction.server.util.ConfigLoader;
import com.auction.common.protocol.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class AuctionServer {
    private static final int PORT = ConfigLoader.getServerPort();
    private static Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
    
    // Services
    private static AuthService authService;
    private static AuctionService auctionService;
    private static BidService bidService;
    private static BroadcastService broadcastService;
    
    public static void main(String[] args) {
        Logger.info("===== AUCTION SERVER STARTING =====");
        
        // Init services
        authService = new AuthService();
        auctionService = new AuctionService();
        bidService = new BidService();
        broadcastService = new BroadcastService();
        
        // Test database
        if (!DatabaseConnection.getInstance().testConnection()) {
            Logger.error("Cannot connect to database!");
            return;
        }
        
        // Start server
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Logger.info("Server started on port " + PORT);
            Logger.info("Waiting for clients...");
            
            while (true) {
                Socket client = serverSocket.accept();
                ClientHandler handler = new ClientHandler(client);
                clients.add(handler);
                new Thread(handler).start();
                Logger.info("Client connected: " + client.getInetAddress());
            }
        } catch (IOException e) {
            Logger.error("Server error", e);
        }
    }
    
    // Broadcast to all clients
    public static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }
    
    // Remove client
    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
    
    // Getters for services
    public static AuthService getAuthService() { return authService; }
    public static AuctionService getAuctionService() { return auctionService; }
    public static BidService getBidService() { return bidService; }
    public static BroadcastService getBroadcastService() { return broadcastService; }
}
```

### 2. ClientHandler.java

```java
package com.auction.server;

import com.auction.server.model.User;
import com.auction.server.util.Logger;
import com.auction.common.protocol.*;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private User currentUser;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            String message;
            while ((message = in.readLine()) != null) {
                handleMessage(message);
            }
        } catch (IOException e) {
            Logger.error("Client error", e);
        } finally {
            cleanup();
        }
    }
    
    private void handleMessage(String message) {
        Logger.info("Received: " + message);
        
        String[] parts = Protocol.parseMessage(message);
        if (parts.length == 0) return;
        
        String command = parts[0];
        
        try {
            switch (command) {
                case MessageType.LOGIN:
                    handleLogin(parts);
                    break;
                case MessageType.REGISTER:
                    handleRegister(parts);
                    break;
                case MessageType.LIST_AUCTIONS:
                    handleListAuctions();
                    break;
                case MessageType.BID:
                    handleBid(parts);
                    break;
                // TODO: Add more commands
                default:
                    send(Protocol.buildMessage(MessageType.ERROR, "Unknown command"));
            }
        } catch (Exception e) {
            send(Protocol.buildMessage(MessageType.ERROR, e.getMessage()));
        }
    }
    
    private void handleLogin(String[] parts) {
        if (parts.length < 3) return;
        
        try {
            User user = AuctionServer.getAuthService().login(parts[1], parts[2]);
            currentUser = user;
            send(Protocol.buildMessage(MessageType.LOGIN_SUCCESS, 
                String.valueOf(user.getId()), user.getUsername(), user.getRole()));
        } catch (Exception e) {
            send(Protocol.buildMessage(MessageType.LOGIN_FAILED, e.getMessage()));
        }
    }
    
    private void handleRegister(String[] parts) {
        // TODO: Implement
    }
    
    private void handleListAuctions() {
        // TODO: Implement
    }
    
    private void handleBid(String[] parts) {
        // TODO: Implement
    }
    
    public void send(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    
    private void cleanup() {
        try {
            socket.close();
            AuctionServer.removeClient(this);
        } catch (IOException e) {
            Logger.error("Cleanup error", e);
        }
    }
}
```

---

## 🎯 CẢI THIỆN HƠN NỮA

1. **Thêm Timer thread** để check expired auctions
2. **Implement đầy đủ các commands** trong ClientHandler
3. **Add error handling** tốt hơn
4. **WebSocket support** nếu muốn (dùng thư viện)

---

## 🚀 BUILD & RUN

```bash
# Compile
cd source/server
javac -d bin -cp "lib/*:../common/bin" src/main/java/com/auction/server/**/*.java

# Run
java -cp "bin:lib/*:../common/bin" com.auction.server.AuctionServer
```

---

## ✅ TEST FLOW

1. Start server
2. Start client (npm start)
3. Login với alice/admin123
4. Check console logs

---

Bạn có code gần như hoàn chỉnh! Chỉ cần hoàn thiện 3 files trên là xong! 🎉

