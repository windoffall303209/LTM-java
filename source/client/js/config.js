/**
 * Configuration file cho client
 */

const CONFIG = {
    // WebSocket Server URL
    SERVER_URL: 'ws://localhost:8888',
    
    // Alternative servers (for testing)
    SERVERS: {
        local: 'ws://localhost:8888',
        dev: 'ws://192.168.1.100:8888',
        // prod: 'ws://your-production-server:8888'
    },
    
    // Auto-reconnect settings
    RECONNECT: {
        enabled: true,
        maxAttempts: 5,
        delay: 3000 // ms
    },
    
    // Update intervals
    INTERVALS: {
        auctionList: 5000,  // Refresh auction list every 5s
        countdown: 1000,     // Update countdown every 1s
        ping: 30000         // Send ping every 30s
    },
    
    // UI Settings
    UI: {
        toastDuration: 3000,
        animationSpeed: 300,
        maxBidHistory: 20
    },
    
    // Audio
    AUDIO: {
        enabled: true,
        notificationSound: 'assets/sounds/notification.mp3'
    },
    
    // Format
    CURRENCY: {
        locale: 'vi-VN',
        currency: 'VND'
    }
};

// Get current server URL (có thể dynamic từ environment)
function getServerURL() {
    // Check if custom server URL in localStorage
    const customURL = localStorage.getItem('serverURL');
    if (customURL) {
        return customURL;
    }
    
    // Default
    return CONFIG.SERVER_URL;
}

// Export for use in other files
if (typeof module !== 'undefined' && module.exports) {
    module.exports = CONFIG;
}

