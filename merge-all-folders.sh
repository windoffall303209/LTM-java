#!/bin/bash

# Script để merge 3 folders thành 1 project hoàn chỉnh
# Author: Claude Code
# Date: 02/11/2025

echo "========================================="
echo "  MERGE 3 FOLDERS THÀNH 1 PROJECT"
echo "========================================="
echo ""

# Tạo folder merged
MERGED_DIR="merged-project"

if [ -d "$MERGED_DIR" ]; then
    echo "⚠️  Folder $MERGED_DIR đã tồn tại. Xóa và tạo mới? (y/n)"
    read -r response
    if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
        rm -rf "$MERGED_DIR"
    else
        echo "❌ Hủy merge."
        exit 1
    fi
fi

echo "📁 Tạo folder merged-project..."
cp -r "basic-project-user2" "$MERGED_DIR"

echo ""
echo "========================================="
echo "  STEP 1: Copy files từ User2 (Base)"
echo "========================================="
echo "✅ Đã copy base từ basic-project-user2"

echo ""
echo "========================================="
echo "  STEP 2: Merge files từ User1"
echo "========================================="

# Copy services từ User1
echo "📄 Copy AuctionService.java..."
cp "basic-project -user1/source/server/src/main/java/com/auction/service/AuctionService.java" "$MERGED_DIR/source/server/src/main/java/com/auction/service/"

echo "📄 Copy BidService.java..."
cp "basic-project -user1/source/server/src/main/java/com/auction/service/BidService.java" "$MERGED_DIR/source/server/src/main/java/com/auction/service/"

echo "📄 Copy AuctionSchedulerService.java..."
cp "basic-project -user1/source/server/src/main/java/com/auction/service/AuctionSchedulerService.java" "$MERGED_DIR/source/server/src/main/java/com/auction/service/"

# Copy controllers từ User1
echo "📄 Copy AuctionController.java..."
cp "basic-project -user1/source/server/src/main/java/com/auction/controller/AuctionController.java" "$MERGED_DIR/source/server/src/main/java/com/auction/controller/"

echo "📄 Copy BidController.java..."
cp "basic-project -user1/source/server/src/main/java/com/auction/controller/BidController.java" "$MERGED_DIR/source/server/src/main/java/com/auction/controller/"

# Copy WebSocket từ User1
echo "📄 Copy WebSocketController.java..."
mkdir -p "$MERGED_DIR/source/server/src/main/java/com/auction/websocket"
cp "basic-project -user1/source/server/src/main/java/com/auction/websocket/WebSocketController.java" "$MERGED_DIR/source/server/src/main/java/com/auction/websocket/"

# Copy DataInitializer từ User1 (có sample auctions)
echo "📄 Copy DataInitializer.java từ User1..."
cp "basic-project -user1/source/server/src/main/java/com/auction/config/DataInitializer.java" "$MERGED_DIR/source/server/src/main/java/com/auction/config/"

# Copy frontend từ User1
echo "📄 Copy dashboard.html..."
cp "basic-project -user1/source/client/public/dashboard.html" "$MERGED_DIR/source/client/public/"

echo "📄 Copy auction-detail.html..."
cp "basic-project -user1/source/client/public/auction-detail.html" "$MERGED_DIR/source/client/public/"

echo "📄 Copy my-bids.html..."
cp "basic-project -user1/source/client/public/my-bids.html" "$MERGED_DIR/source/client/public/"

echo "📄 Copy dashboard.js..."
cp "basic-project -user1/source/client/public/js/dashboard.js" "$MERGED_DIR/source/client/public/js/"

echo "📄 Copy auction.js..."
cp "basic-project -user1/source/client/public/js/auction.js" "$MERGED_DIR/source/client/public/js/"

echo "✅ Đã merge xong User1"

echo ""
echo "========================================="
echo "  STEP 3: Merge files từ Admin"
echo "========================================="

# Copy services từ Admin
echo "📄 Copy AdminAuctionService.java..."
cp "basic-project -admin/source/server/src/main/java/com/auction/service/AdminAuctionService.java" "$MERGED_DIR/source/server/src/main/java/com/auction/service/"

echo "📄 Copy AdminUserService.java..."
cp "basic-project -admin/source/server/src/main/java/com/auction/service/AdminUserService.java" "$MERGED_DIR/source/server/src/main/java/com/auction/service/"

echo "📄 Copy AdminStatisticsService.java..."
cp "basic-project -admin/source/server/src/main/java/com/auction/service/AdminStatisticsService.java" "$MERGED_DIR/source/server/src/main/java/com/auction/service/"

# Copy controller từ Admin
echo "📄 Copy AdminController.java..."
cp "basic-project -admin/source/server/src/main/java/com/auction/controller/AdminController.java" "$MERGED_DIR/source/server/src/main/java/com/auction/controller/"

# Copy SecurityConfig từ Admin (có admin role)
echo "📄 Copy SecurityConfig.java từ Admin..."
cp "basic-project -admin/source/server/src/main/java/com/auction/config/SecurityConfig.java" "$MERGED_DIR/source/server/src/main/java/com/auction/config/"

# Copy admin frontend
echo "📄 Copy admin HTML pages..."
mkdir -p "$MERGED_DIR/source/client/public/admin"
cp "basic-project -admin/source/client/public/admin/"*.html "$MERGED_DIR/source/client/public/admin/"

echo "📄 Copy admin JS files..."
cp "basic-project -admin/source/client/public/js/admin-"*.js "$MERGED_DIR/source/client/public/js/"

echo "📄 Copy admin-style.css..."
cp "basic-project -admin/source/client/public/css/admin-style.css" "$MERGED_DIR/source/client/public/css/"

echo "✅ Đã merge xong Admin"

echo ""
echo "========================================="
echo "  STEP 4: Verify merged project"
echo "========================================="

echo ""
echo "📊 Thống kê files:"
echo "- Backend Java files: $(find "$MERGED_DIR/source/server/src/main/java" -name "*.java" | wc -l)"
echo "- Frontend HTML files: $(find "$MERGED_DIR/source/client/public" -name "*.html" | wc -l)"
echo "- Frontend JS files: $(find "$MERGED_DIR/source/client/public" -name "*.js" | wc -l)"
echo "- Frontend CSS files: $(find "$MERGED_DIR/source/client/public" -name "*.css" | wc -l)"

echo ""
echo "========================================="
echo "  ✅ MERGE HOÀN TẤT!"
echo "========================================="
echo ""
echo "Bước tiếp theo:"
echo "1. cd merged-project/source/server"
echo "2. mvn clean install"
echo "3. mvn spring-boot:run"
echo ""
echo "📝 Xem chi tiết tại CODE_SUMMARY.md"
