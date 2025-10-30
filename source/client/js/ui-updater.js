/**
 * UI Updater - Cập nhật UI theo thời gian thực
 */

// Countdown timer
let countdownInterval = null;

function startCountdown() {
    if (countdownInterval) {
        clearInterval(countdownInterval);
    }
    
    countdownInterval = setInterval(() => {
        const timeLeftEl = document.getElementById('timeLeft');
        if (!timeLeftEl) return;
        
        const currentText = timeLeftEl.textContent;
        const parts = currentText.split(':');
        if (parts.length !== 2) return;
        
        let mins = parseInt(parts[0]);
        let secs = parseInt(parts[1]);
        
        // Countdown
        if (secs > 0) {
            secs--;
        } else if (mins > 0) {
            mins--;
            secs = 59;
        } else {
            // Time's up
            clearInterval(countdownInterval);
            return;
        }
        
        const newTime = `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
        timeLeftEl.textContent = newTime;
        
        // Add warning class if less than 1 minute
        if (mins === 0 && secs < 60) {
            timeLeftEl.classList.add('text-danger');
        }
    }, 1000);
}

// Start countdown when auction is selected
document.addEventListener('DOMContentLoaded', () => {
    const observer = new MutationObserver(() => {
        const timeLeftEl = document.getElementById('timeLeft');
        if (timeLeftEl && timeLeftEl.textContent !== '00:00') {
            startCountdown();
        }
    });
    
    const timeLeftEl = document.getElementById('timeLeft');
    if (timeLeftEl) {
        observer.observe(timeLeftEl, { childList: true, characterData: true, subtree: true });
    }
});

// Auto-scroll bid history to top when new bid
const bidHistoryObserver = new MutationObserver(() => {
    const historyContainer = document.getElementById('bidHistory');
    if (historyContainer) {
        historyContainer.scrollTop = 0;
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const historyContainer = document.getElementById('bidHistory');
    if (historyContainer) {
        bidHistoryObserver.observe(historyContainer, { childList: true });
    }
});

// Highlight new items
function highlightElement(element) {
    element.classList.add('highlight-new');
    setTimeout(() => {
        element.classList.remove('highlight-new');
    }, 2000);
}

// Update page title with notification count
let notificationCount = 0;

function updatePageTitle() {
    const baseTitle = 'Auction System';
    if (notificationCount > 0) {
        document.title = `(${notificationCount}) ${baseTitle}`;
    } else {
        document.title = baseTitle;
    }
}

// Listen for bid updates to increment notification
socketClient.on('BID_UPDATE', () => {
    if (document.hidden) {
        notificationCount++;
        updatePageTitle();
    }
});

// Reset notification count when page is visible
document.addEventListener('visibilitychange', () => {
    if (!document.hidden) {
        notificationCount = 0;
        updatePageTitle();
    }
});

// Animate price changes
function animatePriceChange(element, newValue) {
    element.style.transition = 'transform 0.3s, color 0.3s';
    element.style.transform = 'scale(1.2)';
    element.style.color = '#28a745';
    
    setTimeout(() => {
        element.textContent = newValue;
        element.style.transform = 'scale(1)';
        element.style.color = '';
    }, 300);
}

