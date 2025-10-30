/**
 * Auction Handler - Xử lý hiển thị và tương tác với auctions
 */

let currentAuctionId = null;
let auctions = [];

// Load auctions when dashboard page loads
if (window.location.pathname.includes('dashboard.html')) {
    // Require auth
    requireAuth();
    
    // Display current user
    const user = getCurrentUser();
    document.getElementById('currentUsername').textContent = user.username;
    
    // Show admin nav if admin
    if (user.role === 'admin') {
        document.getElementById('adminNavItem').style.display = 'block';
    }
    
    // Request auction list
    setTimeout(() => {
        socketClient.send('LIST_AUCTIONS');
    }, 500);
    
    // Auto refresh every 5s
    setInterval(() => {
        if (socketClient.isConnected()) {
            socketClient.send('LIST_AUCTIONS');
        }
    }, CONFIG.INTERVALS.auctionList);
}

// Handle auction list response
socketClient.on('AUCTION_LIST', (params) => {
    if (params.length === 0 || !params[0]) {
        displayEmptyAuctionList();
        return;
    }
    
    // Parse: id:name:price:time;id:name:price:time;...
    auctions = [];
    const auctionItems = params[0].split(';');
    
    auctionItems.forEach(item => {
        const [id, name, price, timeLeft, winner] = item.split(':');
        auctions.push({
            id: parseInt(id),
            name: name,
            price: parseInt(price),
            timeLeft: parseInt(timeLeft),
            winner: winner || ''
        });
    });
    
    displayAuctionList(auctions);
});

// Display auction list
function displayAuctionList(auctions) {
    const listContainer = document.getElementById('auctionList');
    if (!listContainer) return;
    
    if (auctions.length === 0) {
        displayEmptyAuctionList();
        return;
    }
    
    listContainer.innerHTML = '';
    
    auctions.forEach(auction => {
        const item = document.createElement('div');
        item.className = 'list-group-item auction-item';
        if (auction.id === currentAuctionId) {
            item.classList.add('active');
        }
        if (auction.timeLeft < 60) {
            item.classList.add('ending-soon');
        }
        
        item.innerHTML = `
            <div class="d-flex justify-content-between">
                <h6 class="mb-1">${escapeHtml(auction.name)}</h6>
                <small class="auction-timer">
                    <i class="bi bi-clock"></i> ${formatTime(auction.timeLeft)}
                </small>
            </div>
            <div class="auction-price">${formatCurrency(auction.price)}</div>
            ${auction.winner ? `<small class="auction-winner">
                <i class="bi bi-trophy-fill"></i> ${escapeHtml(auction.winner)}
            </small>` : ''}
        `;
        
        item.addEventListener('click', () => {
            selectAuction(auction.id);
        });
        
        listContainer.appendChild(item);
    });
}

function displayEmptyAuctionList() {
    const listContainer = document.getElementById('auctionList');
    if (!listContainer) return;
    
    listContainer.innerHTML = `
        <div class="text-center py-5 text-muted">
            <i class="bi bi-inbox" style="font-size: 3rem;"></i>
            <p class="mt-2">Chưa có phiên đấu giá nào</p>
        </div>
    `;
}

// Select an auction to view details
function selectAuction(auctionId) {
    currentAuctionId = auctionId;
    
    // Highlight in list
    document.querySelectorAll('.auction-item').forEach(item => {
        item.classList.remove('active');
    });
    event.currentTarget?.classList.add('active');
    
    // Request auction details
    socketClient.send(`GET_AUCTION|${auctionId}`);
    socketClient.send(`JOIN_AUCTION|${auctionId}`);
    socketClient.send(`GET_HISTORY|${auctionId}`);
}

// Handle auction info response
socketClient.on('AUCTION_INFO', (params) => {
    const [id, name, description, price, winner, timeLeft, status] = params;
    
    // Show detail card
    document.getElementById('emptyState').style.display = 'none';
    document.getElementById('auctionDetailCard').style.display = 'block';
    
    // Update UI
    document.getElementById('productName').textContent = name;
    document.getElementById('productDescription').textContent = description || 'Không có mô tả';
    document.getElementById('currentPrice').textContent = formatCurrency(parseInt(price));
    document.getElementById('currentWinner').textContent = winner || 'Chưa có';
    document.getElementById('timeLeft').textContent = formatTime(parseInt(timeLeft));
    
    // Update bid input placeholder
    const minBid = parseInt(price) + 100000;
    document.getElementById('bidAmount').placeholder = `Tối thiểu ${formatNumber(minBid)}`;
});

// Handle bid history
socketClient.on('BID_HISTORY', (params) => {
    if (!params[0]) return;
    
    const historyContainer = document.getElementById('bidHistory');
    if (!historyContainer) return;
    
    // Parse: username:amount:time;username:amount:time;...
    const bids = [];
    const bidItems = params[0].split(';');
    
    bidItems.forEach(item => {
        const [username, amount, time] = item.split(':');
        bids.push({ username, amount: parseInt(amount), time: parseInt(time) });
    });
    
    historyContainer.innerHTML = '';
    
    if (bids.length === 0) {
        historyContainer.innerHTML = '<p class="text-muted">Chưa có bid nào</p>';
        return;
    }
    
    bids.forEach((bid, index) => {
        const bidItem = document.createElement('div');
        bidItem.className = 'bid-item';
        if (index === 0) bidItem.classList.add('winning-bid');
        if (bid.username === getCurrentUser().username) bidItem.classList.add('my-bid');
        
        bidItem.innerHTML = `
            <div class="d-flex justify-content-between">
                <strong>${escapeHtml(bid.username)}</strong>
                <span>${formatCurrency(bid.amount)}</span>
            </div>
            <small class="text-muted">${timeAgo(bid.time)}</small>
        `;
        
        historyContainer.appendChild(bidItem);
    });
});

// Search and filter
document.getElementById('searchInput')?.addEventListener('input', debounce((e) => {
    const search = e.target.value.toLowerCase();
    const filtered = auctions.filter(a => a.name.toLowerCase().includes(search));
    displayAuctionList(filtered);
}, 300));

document.getElementById('statusFilter')?.addEventListener('change', (e) => {
    const filter = e.target.value;
    let filtered = auctions;
    
    if (filter === 'active') {
        filtered = auctions.filter(a => a.timeLeft > 0);
    } else if (filter === 'ending') {
        filtered = auctions.filter(a => a.timeLeft > 0 && a.timeLeft < 300);
    }
    
    displayAuctionList(filtered);
});

