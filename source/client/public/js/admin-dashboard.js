/**
 * ADMIN Dashboard Page Logic
 * Xử lý logic cho trang Admin Dashboard
 */

const AdminDashboard = {
  statistics: null,
  refreshInterval: null,

  /**
   * Initialize dashboard
   */
  async init() {
    console.log('[Admin Dashboard] Initializing...');

    // Load statistics
    await this.loadStatistics();

    // Setup WebSocket listener
    this.setupWebSocketListener();

    // Setup auto refresh
    this.setupAutoRefresh();

    console.log('[Admin Dashboard] Initialization complete');
  },

  /**
   * Load statistics from API
   */
  async loadStatistics() {
    try {
      const response = await AdminAuth.apiRequest(
        `${window.ADMIN_API_CONFIG.BASE_URL}${window.ADMIN_API_CONFIG.ENDPOINTS.STATISTICS}`
      );

      const result = await response.json();

      if (result.success) {
        this.statistics = result.data;
        this.displayStatistics();
      } else {
        AdminUtils.showError(result.message || 'Không thể tải thống kê');
      }
    } catch (error) {
      console.error('[Admin Dashboard] Error loading statistics:', error);
      AdminUtils.showError('Lỗi khi tải thống kê');
    }
  },

  /**
   * Display statistics on dashboard
   */
  displayStatistics() {
    if (!this.statistics) return;

    const {
      totalUsers = 0,
      activeUsers = 0,
      totalAuctions = 0,
      activeAuctions = 0,
      totalBids = 0,
      totalBidValue = 0
    } = this.statistics;

    // Update stat cards
    this.updateStatCard('totalUsers', totalUsers);
    this.updateStatCard('activeUsers', activeUsers);
    this.updateStatCard('totalAuctions', totalAuctions);
    this.updateStatCard('activeAuctions', activeAuctions);
    this.updateStatCard('totalBids', totalBids);
    this.updateStatCard('totalBidValue', AdminUtils.formatCurrency(totalBidValue));
  },

  /**
   * Update individual stat card
   */
  updateStatCard(cardId, value) {
    const element = document.getElementById(cardId);
    if (element) {
      element.textContent = value;
    }
  },

  /**
   * Setup WebSocket listener for real-time updates
   */
  setupWebSocketListener() {
    AdminWebSocket.addAuctionListener((event) => {
      // Reload statistics when auction events occur
      if (['AUCTION_CREATED', 'AUCTION_STARTED', 'AUCTION_ENDED'].includes(event.type)) {
        this.loadStatistics();
      }
    });
  },

  /**
   * Setup auto refresh
   */
  setupAutoRefresh() {
    const refreshInterval = window.ADMIN_API_CONFIG.SETTINGS.DASHBOARD_REFRESH_INTERVAL;

    this.refreshInterval = setInterval(() => {
      console.log('[Admin Dashboard] Auto refreshing statistics...');
      this.loadStatistics();
    }, refreshInterval);
  },

  /**
   * Cleanup
   */
  cleanup() {
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  }
};

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
  // Initialize admin auth
  if (!AdminAuth.init()) {
    return;
  }

  // Initialize WebSocket
  AdminWebSocket.init();

  // Initialize dashboard
  AdminDashboard.init();
});

// Cleanup on page unload
window.addEventListener('beforeunload', () => {
  AdminDashboard.cleanup();
});

console.log('[Admin Dashboard] Module loaded');
