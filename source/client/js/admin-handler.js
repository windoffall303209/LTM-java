/**
 * Admin Handler - Admin panel functionality
 */

// Check if user is admin
if (window.location.pathname.includes('admin.html')) {
    requireAuth();
    
    const user = getCurrentUser();
    if (user.role !== 'admin') {
        alert('Access denied: Admin only');
        window.location.href = 'dashboard.html';
    }
    
    // Load admin data
    loadStats();
    loadAllAuctions();
    loadAllUsers();
}

// Load statistics
function loadStats() {
    // Request from server (cần implement server side)
    socketClient.send('GET_STATISTICS');
}

socketClient.on('STATISTICS', (params) => {
    const [users, activeAuctions, totalBids, revenue] = params;
    
    document.getElementById('totalUsers').textContent = users;
    document.getElementById('activeAuctions').textContent = activeAuctions;
    document.getElementById('totalBids').textContent = totalBids;
    document.getElementById('totalRevenue').textContent = formatCurrency(parseInt(revenue));
});

// Create auction form
document.getElementById('createAuctionForm')?.addEventListener('submit', (e) => {
    e.preventDefault();
    
    const name = document.getElementById('productName').value;
    const description = document.getElementById('productDescription').value;
    const category = document.getElementById('productCategory').value;
    const startPrice = document.getElementById('startPrice').value;
    const duration = document.getElementById('duration').value;
    const imageUrl = document.getElementById('imageUrl').value;
    
    // Send create command
    socketClient.send(`CREATE_AUCTION|${name}|${startPrice}|${duration}|${description}|${category}|${imageUrl}`);
});

socketClient.on('AUCTION_CREATED', (params) => {
    showToast('Tạo đấu giá thành công!', 'success');
    document.getElementById('createAuctionForm').reset();
    loadAllAuctions();
});

// Load all auctions (for management)
function loadAllAuctions() {
    socketClient.send('ADMIN_GET_ALL_AUCTIONS');
}

socketClient.on('ALL_AUCTIONS', (params) => {
    // Parse và display
    // (Implement chi tiết tương tự auction list)
});

// Load all users
function loadAllUsers() {
    socketClient.send('ADMIN_GET_ALL_USERS');
}

socketClient.on('ALL_USERS', (params) => {
    // Parse và display
    // (Implement chi tiết)
});

// Ban user
function banUser(userId) {
    if (confirm('Bạn có chắc muốn cấm user này?')) {
        socketClient.send(`BAN_USER|${userId}`);
    }
}

// Delete auction
function deleteAuction(auctionId) {
    if (confirm('Bạn có chắc muốn xóa đấu giá này?')) {
        socketClient.send(`DELETE_AUCTION|${auctionId}`);
    }
}

