/**
 * ADMIN Auctions Page Logic
 * Xử lý logic cho trang quản lý đấu giá
 */

const AdminAuctions = {
  auctions: [],
  currentFilter: 'ALL',

  /**
   * Initialize auctions page
   */
  async init() {
    console.log('[Admin Auctions] Initializing...');

    // Load auctions
    await this.loadAuctions();

    // Setup event listeners
    this.setupEventListeners();

    // Setup WebSocket
    this.setupWebSocketListener();

    console.log('[Admin Auctions] Initialization complete');
  },

  /**
   * Load all auctions from API
   */
  async loadAuctions() {
    try {
      AdminUtils.showLoading('#auctionsTableBody');

      const response = await AdminAuth.apiRequest(
        `${window.ADMIN_API_CONFIG.BASE_URL}${window.ADMIN_API_CONFIG.ENDPOINTS.AUCTIONS_ALL}`
      );

      const result = await response.json();

      if (result.success) {
        this.auctions = result.data || [];
        this.displayAuctions();
      } else {
        AdminUtils.showError(result.message || 'Không thể tải danh sách đấu giá');
        AdminUtils.showEmptyState('#auctionsTableBody', 'Không thể tải dữ liệu');
      }
    } catch (error) {
      console.error('[Admin Auctions] Error loading auctions:', error);
      AdminUtils.showError('Lỗi khi tải danh sách đấu giá');
      AdminUtils.showEmptyState('#auctionsTableBody', 'Lỗi khi tải dữ liệu');
    }
  },

  /**
   * Display auctions in table
   */
  displayAuctions() {
    const tbody = document.getElementById('auctionsTableBody');
    if (!tbody) return;

    // Filter auctions
    const filteredAuctions = this.filterAuctions();

    if (filteredAuctions.length === 0) {
      AdminUtils.showEmptyState(tbody, 'Không có đấu giá nào');
      return;
    }

    tbody.innerHTML = filteredAuctions.map((auction, index) => `
      <tr>
        <td>${index + 1}</td>
        <td>
          <strong>${AdminUtils.escapeHtml(auction.title)}</strong><br>
          <small class="text-muted">${AdminUtils.truncateText(auction.description, 50)}</small>
        </td>
        <td>${AdminUtils.formatCurrency(auction.currentPrice)}</td>
        <td>${auction.totalBids || 0}</td>
        <td>${AdminUtils.getAuctionStatusBadge(auction.status)}</td>
        <td>${AdminUtils.formatDate(auction.startTime)}</td>
        <td>
          <div class="btn-group btn-group-sm">
            ${auction.status === 'PENDING' ?
              `<button class="btn btn-success" onclick="AdminAuctions.startAuction(${auction.auctionId})" title="Bắt đầu">
                <i class="bi bi-play-fill"></i>
              </button>` : ''}
            ${auction.status === 'ACTIVE' ?
              `<button class="btn btn-danger" onclick="AdminAuctions.endAuction(${auction.auctionId})" title="Kết thúc">
                <i class="bi bi-stop-fill"></i>
              </button>` : ''}
            <button class="btn btn-primary" onclick="AdminAuctions.editAuction(${auction.auctionId})" title="Sửa">
              <i class="bi bi-pencil"></i>
            </button>
            ${auction.status !== 'ACTIVE' ?
              `<button class="btn btn-danger" onclick="AdminAuctions.deleteAuction(${auction.auctionId})" title="Xóa">
                <i class="bi bi-trash"></i>
              </button>` : ''}
          </div>
        </td>
      </tr>
    `).join('');
  },

  /**
   * Filter auctions by status
   */
  filterAuctions() {
    if (this.currentFilter === 'ALL') {
      return this.auctions;
    }
    return this.auctions.filter(a => a.status === this.currentFilter);
  },

  /**
   * Start auction
   */
  async startAuction(auctionId) {
    if (!await AdminUtils.confirm(`Bạn có chắc muốn bắt đầu đấu giá này?`)) {
      return;
    }

    try {
      const response = await AdminAuth.apiRequest(
        `${window.ADMIN_API_CONFIG.BASE_URL}${window.ADMIN_API_CONFIG.ENDPOINTS.AUCTION_START(auctionId)}`,
        { method: 'POST' }
      );

      const result = await response.json();

      if (result.success) {
        AdminUtils.showSuccess('Đã bắt đầu đấu giá');
        await this.loadAuctions();
      } else {
        AdminUtils.showError(result.message || 'Không thể bắt đầu đấu giá');
      }
    } catch (error) {
      console.error('[Admin Auctions] Error starting auction:', error);
      AdminUtils.showError('Lỗi khi bắt đầu đấu giá');
    }
  },

  /**
   * End auction
   */
  async endAuction(auctionId) {
    if (!await AdminUtils.confirm(`Bạn có chắc muốn kết thúc đấu giá này?`)) {
      return;
    }

    try {
      const response = await AdminAuth.apiRequest(
        `${window.ADMIN_API_CONFIG.BASE_URL}${window.ADMIN_API_CONFIG.ENDPOINTS.AUCTION_END(auctionId)}`,
        { method: 'POST' }
      );

      const result = await response.json();

      if (result.success) {
        AdminUtils.showSuccess('Đã kết thúc đấu giá');
        await this.loadAuctions();
      } else {
        AdminUtils.showError(result.message || 'Không thể kết thúc đấu giá');
      }
    } catch (error) {
      console.error('[Admin Auctions] Error ending auction:', error);
      AdminUtils.showError('Lỗi khi kết thúc đấu giá');
    }
  },

  /**
   * Delete auction
   */
  async deleteAuction(auctionId) {
    if (!await AdminUtils.confirm(`Bạn có chắc muốn xóa đấu giá này? Hành động này không thể hoàn tác!`)) {
      return;
    }

    try {
      const response = await AdminAuth.apiRequest(
        `${window.ADMIN_API_CONFIG.BASE_URL}${window.ADMIN_API_CONFIG.ENDPOINTS.AUCTION_DELETE(auctionId)}`,
        { method: 'DELETE' }
      );

      const result = await response.json();

      if (result.success) {
        AdminUtils.showSuccess('Đã xóa đấu giá');
        await this.loadAuctions();
      } else {
        AdminUtils.showError(result.message || 'Không thể xóa đấu giá');
      }
    } catch (error) {
      console.error('[Admin Auctions] Error deleting auction:', error);
      AdminUtils.showError('Lỗi khi xóa đấu giá');
    }
  },

  /**
   * Edit auction
   */
  editAuction(auctionId) {
    // TODO: Implement edit modal
    AdminUtils.showInfo('Chức năng chỉnh sửa sẽ được cập nhật sau');
  },

  /**
   * Setup event listeners
   */
  setupEventListeners() {
    // Filter buttons
    document.querySelectorAll('[data-filter]').forEach(btn => {
      btn.addEventListener('click', (e) => {
        this.currentFilter = e.target.dataset.filter;
        this.displayAuctions();

        // Update active button
        document.querySelectorAll('[data-filter]').forEach(b => b.classList.remove('active'));
        e.target.classList.add('active');
      });
    });

    // Refresh button
    const refreshBtn = document.getElementById('refreshAuctions');
    if (refreshBtn) {
      refreshBtn.addEventListener('click', () => this.loadAuctions());
    }
  },

  /**
   * Setup WebSocket listener
   */
  setupWebSocketListener() {
    AdminWebSocket.addAuctionListener((event) => {
      // Reload auctions on any auction event
      this.loadAuctions();
    });
  }
};

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
  if (!AdminAuth.init()) return;
  AdminWebSocket.init();
  AdminAuctions.init();
});

console.log('[Admin Auctions] Module loaded');
