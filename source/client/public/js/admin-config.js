/**
 * ADMIN API Configuration
 * Cấu hình riêng cho Admin Panel
 */

const ADMIN_API_BASE_URL = 'http://localhost:8000';
const ADMIN_WS_URL = 'http://localhost:8000/ws';

// Export for use in admin scripts
window.ADMIN_API_CONFIG = {
  BASE_URL: ADMIN_API_BASE_URL,
  WS_URL: ADMIN_WS_URL,

  // Admin-specific endpoints
  ENDPOINTS: {
    // Auction Management
    AUCTIONS: '/api/admin/auctions',
    AUCTIONS_ALL: '/api/admin/auctions/all',
    AUCTION_CREATE: '/api/admin/auctions',
    AUCTION_UPDATE: (id) => `/api/admin/auctions/${id}`,
    AUCTION_DELETE: (id) => `/api/admin/auctions/${id}`,
    AUCTION_START: (id) => `/api/admin/auctions/${id}/start`,
    AUCTION_END: (id) => `/api/admin/auctions/${id}/end`,

    // User Management
    USERS: '/api/admin/users',
    USER_TOGGLE_STATUS: (id) => `/api/admin/users/${id}/toggle-status`,
    USER_UPDATE_BALANCE: (id) => `/api/admin/users/${id}/update-balance`,

    // Statistics
    STATISTICS: '/api/admin/statistics',

    // Auth (shared with user)
    LOGIN: '/api/auth/login',
    LOGOUT: '/api/auth/logout',
  },

  // WebSocket topics for admin
  WS_TOPICS: {
    AUCTIONS: '/topic/auctions',
    AUCTION: (id) => `/topic/auction/${id}`,
  },

  // Admin-specific settings
  SETTINGS: {
    // Refresh intervals (in milliseconds)
    DASHBOARD_REFRESH_INTERVAL: 30000, // 30 seconds
    AUCTION_LIST_REFRESH_INTERVAL: 10000, // 10 seconds
    USER_LIST_REFRESH_INTERVAL: 60000, // 1 minute

    // Pagination
    DEFAULT_PAGE_SIZE: 20,
    MAX_PAGE_SIZE: 100,

    // Timeouts
    REQUEST_TIMEOUT: 30000, // 30 seconds
  }
};

console.log('[Admin Config] Configuration loaded:', window.ADMIN_API_CONFIG);
