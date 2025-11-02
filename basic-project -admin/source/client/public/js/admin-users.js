/**
 * ADMIN Users Page Logic
 * Xử lý logic cho trang quản lý users
 */

const AdminUsers = {
  users: [],
  currentFilter: 'ALL',

  /**
   * Initialize users page
   */
  async init() {
    console.log('[Admin Users] Initializing...');

    // Load users
    await this.loadUsers();

    // Setup event listeners
    this.setupEventListeners();

    console.log('[Admin Users] Initialization complete');
  },

  /**
   * Load all users from API
   */
  async loadUsers() {
    try {
      AdminUtils.showLoading('#usersTableBody');

      const response = await AdminAuth.apiRequest(
        `${window.ADMIN_API_CONFIG.BASE_URL}${window.ADMIN_API_CONFIG.ENDPOINTS.USERS}`
      );

      const result = await response.json();

      if (result.success) {
        this.users = result.data || [];
        this.displayUsers();
      } else {
        AdminUtils.showError(result.message || 'Không thể tải danh sách users');
        AdminUtils.showEmptyState('#usersTableBody', 'Không thể tải dữ liệu');
      }
    } catch (error) {
      console.error('[Admin Users] Error loading users:', error);
      AdminUtils.showError('Lỗi khi tải danh sách users');
      AdminUtils.showEmptyState('#usersTableBody', 'Lỗi khi tải dữ liệu');
    }
  },

  /**
   * Display users in table
   */
  displayUsers() {
    const tbody = document.getElementById('usersTableBody');
    if (!tbody) return;

    // Filter users
    const filteredUsers = this.filterUsers();

    if (filteredUsers.length === 0) {
      AdminUtils.showEmptyState(tbody, 'Không có user nào');
      return;
    }

    tbody.innerHTML = filteredUsers.map((user, index) => `
      <tr>
        <td>${index + 1}</td>
        <td>
          <strong>${AdminUtils.escapeHtml(user.username)}</strong><br>
          <small class="text-muted">${AdminUtils.escapeHtml(user.email || 'N/A')}</small>
        </td>
        <td>${AdminUtils.escapeHtml(user.fullName || 'N/A')}</td>
        <td>${AdminUtils.getUserRoleBadge(user.role)}</td>
        <td>${AdminUtils.formatCurrency(user.balance)}</td>
        <td>${AdminUtils.getUserStatusBadge(user.isActive)}</td>
        <td>${AdminUtils.formatDate(user.createdAt)}</td>
        <td>
          <div class="btn-group btn-group-sm">
            <button class="btn ${user.isActive ? 'btn-warning' : 'btn-success'}"
                    onclick="AdminUsers.toggleUserStatus(${user.userId}, ${user.isActive})"
                    title="${user.isActive ? 'Khóa' : 'Mở khóa'}">
              <i class="bi bi-${user.isActive ? 'lock' : 'unlock'}-fill"></i>
            </button>
            <button class="btn btn-primary"
                    onclick="AdminUsers.updateBalance(${user.userId})"
                    title="Cập nhật số dư">
              <i class="bi bi-cash"></i>
            </button>
          </div>
        </td>
      </tr>
    `).join('');
  },

  /**
   * Filter users
   */
  filterUsers() {
    switch (this.currentFilter) {
      case 'ACTIVE':
        return this.users.filter(u => u.isActive);
      case 'BANNED':
        return this.users.filter(u => !u.isActive);
      case 'ADMIN':
        return this.users.filter(u => u.role === 'ADMIN');
      case 'USER':
        return this.users.filter(u => u.role === 'USER');
      default:
        return this.users;
    }
  },

  /**
   * Toggle user status (ban/unban)
   */
  async toggleUserStatus(userId, currentStatus) {
    const action = currentStatus ? 'khóa' : 'mở khóa';

    if (!await AdminUtils.confirm(`Bạn có chắc muốn ${action} user này?`)) {
      return;
    }

    try {
      const response = await AdminAuth.apiRequest(
        `${window.ADMIN_API_CONFIG.BASE_URL}${window.ADMIN_API_CONFIG.ENDPOINTS.USER_TOGGLE_STATUS(userId)}`,
        { method: 'POST' }
      );

      const result = await response.json();

      if (result.success) {
        AdminUtils.showSuccess(`Đã ${action} user thành công`);
        await this.loadUsers();
      } else {
        AdminUtils.showError(result.message || `Không thể ${action} user`);
      }
    } catch (error) {
      console.error('[Admin Users] Error toggling user status:', error);
      AdminUtils.showError(`Lỗi khi ${action} user`);
    }
  },

  /**
   * Update user balance
   */
  async updateBalance(userId) {
    const amount = prompt('Nhập số tiền muốn cộng/trừ (số âm để trừ):');

    if (amount === null) return;

    const numAmount = parseFloat(amount);

    if (isNaN(numAmount)) {
      AdminUtils.showError('Số tiền không hợp lệ');
      return;
    }

    try {
      const url = `${window.ADMIN_API_CONFIG.BASE_URL}${window.ADMIN_API_CONFIG.ENDPOINTS.USER_UPDATE_BALANCE(userId)}`;

      const response = await AdminAuth.apiRequest(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `amount=${numAmount}`
      });

      const result = await response.json();

      if (result.success) {
        AdminUtils.showSuccess('Đã cập nhật số dư thành công');
        await this.loadUsers();
      } else {
        AdminUtils.showError(result.message || 'Không thể cập nhật số dư');
      }
    } catch (error) {
      console.error('[Admin Users] Error updating balance:', error);
      AdminUtils.showError('Lỗi khi cập nhật số dư');
    }
  },

  /**
   * Setup event listeners
   */
  setupEventListeners() {
    // Filter buttons
    document.querySelectorAll('[data-user-filter]').forEach(btn => {
      btn.addEventListener('click', (e) => {
        this.currentFilter = e.target.dataset.userFilter;
        this.displayUsers();

        // Update active button
        document.querySelectorAll('[data-user-filter]').forEach(b => b.classList.remove('active'));
        e.target.classList.add('active');
      });
    });

    // Refresh button
    const refreshBtn = document.getElementById('refreshUsers');
    if (refreshBtn) {
      refreshBtn.addEventListener('click', () => this.loadUsers());
    }
  }
};

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
  if (!AdminAuth.init()) return;
  AdminUsers.init();
});

console.log('[Admin Users] Module loaded');
