/**
 * Bid Handler - Xử lý đặt giá
 */

// Place bid button
document.getElementById('placeBidBtn')?.addEventListener('click', () => {
    const amount = document.getElementById('bidAmount').value;
    
    if (!amount || amount <= 0) {
        showToast('Vui lòng nhập giá hợp lệ', 'warning');
        return;
    }
    
    if (!currentAuctionId) {
        showToast('Vui lòng chọn phiên đấu giá', 'warning');
        return;
    }
    
    // Send bid
    socketClient.send(`BID|${currentAuctionId}|${amount}`);
    
    // Clear input
    document.getElementById('bidAmount').value = '';
});

// Quick increment buttons
document.querySelectorAll('[data-increment]').forEach(btn => {
    btn.addEventListener('click', () => {
        const increment = parseInt(btn.dataset.increment);
        const input = document.getElementById('bidAmount');
        const current = parseInt(input.value) || 0;
        const currentPrice = parseInt(document.getElementById('currentPrice')
            .textContent.replace(/[^\d]/g, '')) || 0;
        
        input.value = Math.max(current, currentPrice) + increment;
    });
});

// Auto bid
document.getElementById('setAutoBidBtn')?.addEventListener('click', () => {
    const maxAmount = document.getElementById('maxAutoBid').value;
    
    if (!maxAmount || maxAmount <= 0) {
        showToast('Vui lòng nhập giá tối đa', 'warning');
        return;
    }
    
    if (!currentAuctionId) {
        showToast('Vui lòng chọn phiên đấu giá', 'warning');
        return;
    }
    
    if (confirm(`Bật auto bid với giá tối đa ${formatCurrency(maxAmount)}?`)) {
        socketClient.send(`AUTO_BID|${currentAuctionId}|${maxAmount}`);
        showToast('Đã bật auto bid', 'success');
        document.getElementById('maxAutoBid').value = '';
    }
});

// Handle bid responses
socketClient.on('BID_SUCCESS', (params) => {
    const [auctionId, amount] = params;
    showToast(`Đặt giá thành công: ${formatCurrency(parseInt(amount))}`, 'success');
});

socketClient.on('BID_FAILED', (params) => {
    const [auctionId, error] = params;
    showToast(error || 'Đặt giá thất bại', 'error');
});

// Handle bid updates (broadcast from server)
socketClient.on('BID_UPDATE', (params) => {
    const [auctionId, username, amount, timeLeft] = params;
    
    // Update UI if this is current auction
    if (parseInt(auctionId) === currentAuctionId) {
        document.getElementById('currentPrice').textContent = formatCurrency(parseInt(amount));
        document.getElementById('currentWinner').textContent = username;
        document.getElementById('timeLeft').textContent = formatTime(parseInt(timeLeft));
        
        // Play sound
        playNotificationSound();
        
        // Show toast
        const currentUser = getCurrentUser().username;
        if (username !== currentUser) {
            showToast(`${username} đã đặt giá ${formatCurrency(parseInt(amount))}`, 'info');
        }
        
        // Refresh history
        socketClient.send(`GET_HISTORY|${auctionId}`);
    }
    
    // Update in auction list
    const auction = auctions.find(a => a.id === parseInt(auctionId));
    if (auction) {
        auction.price = parseInt(amount);
        auction.winner = username;
        auction.timeLeft = parseInt(timeLeft);
    }
});

// Handle outbid notification
socketClient.on('OUTBID', (params) => {
    const [auctionId, newWinner, newAmount] = params;
    
    const currentUser = getCurrentUser().username;
    if (newWinner !== currentUser) {
        showToast(`Bạn đã bị đè giá! Giá mới: ${formatCurrency(parseInt(newAmount))}`, 'warning');
        playNotificationSound();
    }
});

// Handle auction ending soon
socketClient.on('AUCTION_ENDING_SOON', (params) => {
    const [auctionId, seconds] = params;
    showToast(`Phiên đấu giá sắp kết thúc trong ${seconds}s!`, 'warning');
});

// Handle auction extended
socketClient.on('AUCTION_EXTENDED', (params) => {
    const [auctionId, newTime] = params;
    showToast('Thời gian đấu giá đã được gia hạn', 'info');
});

// Handle auction ended
socketClient.on('AUCTION_ENDED', (params) => {
    const [auctionId, winner, finalPrice] = params;
    
    const currentUser = getCurrentUser().username;
    if (winner === currentUser) {
        showToast(`🎉 Chúc mừng! Bạn đã thắng với giá ${formatCurrency(parseInt(finalPrice))}`, 'success');
    } else {
        showToast(`Đấu giá kết thúc. Người thắng: ${winner}`, 'info');
    }
    
    // Refresh auction list
    socketClient.send('LIST_AUCTIONS');
});

