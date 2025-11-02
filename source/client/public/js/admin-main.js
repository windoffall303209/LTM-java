/**
 * ADMIN Main Utilities
 * Các utility functions dùng chung cho Admin Panel
 */

const AdminUtils = {
  /**
   * Format currency to Vietnamese format
   */
  formatCurrency(amount) {
    if (amount === null || amount === undefined) return '0 VND';
    return new Intl.NumberFormat('vi-VN').format(amount) + ' VND';
  },

  /**
   * Format date to Vietnamese format
   */
  formatDate(dateString) {
    if (!dateString) return 'N/A';

    const date = new Date(dateString);
    if (isNaN(date.getTime())) return 'Invalid Date';

    return new Intl.DateTimeFormat('vi-VN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    }).format(date);
  },

  /**
   * Format date to short format
   */
  formatDateShort(dateString) {
    if (!dateString) return 'N/A';

    const date = new Date(dateString);
    if (isNaN(date.getTime())) return 'Invalid Date';

    return new Intl.DateTimeFormat('vi-VN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    }).format(date);
  },

  /**
   * Format time elapsed (relative time)
   */
  formatTimeElapsed(dateString) {
    if (!dateString) return 'N/A';

    const date = new Date(dateString);
    if (isNaN(date.getTime())) return 'Invalid Date';

    const now = new Date();
    const diffMs = now - date;
    const diffSecs = Math.floor(diffMs / 1000);
    const diffMins = Math.floor(diffSecs / 60);
    const diffHours = Math.floor(diffMins / 60);
    const diffDays = Math.floor(diffHours / 24);

    if (diffDays > 0) return `${diffDays} ngày trước`;
    if (diffHours > 0) return `${diffHours} giờ trước`;
    if (diffMins > 0) return `${diffMins} phút trước`;
    return `${diffSecs} giây trước`;
  },

  /**
   * Show alert message
   */
  showAlert(type, message, duration = 5000) {
    const alertContainer = this.getOrCreateAlertContainer();

    const alertId = 'alert-' + Date.now();
    const alert = document.createElement('div');
    alert.id = alertId;
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
      ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    alertContainer.appendChild(alert);

    // Auto remove after duration
    setTimeout(() => {
      const el = document.getElementById(alertId);
      if (el) {
        el.remove();
      }
    }, duration);
  },

  /**
   * Show success message
   */
  showSuccess(message, duration = 5000) {
    this.showAlert('success', message, duration);
  },

  /**
   * Show error message
   */
  showError(message, duration = 5000) {
    this.showAlert('danger', message, duration);
  },

  /**
   * Show warning message
   */
  showWarning(message, duration = 5000) {
    this.showAlert('warning', message, duration);
  },

  /**
   * Show info message
   */
  showInfo(message, duration = 5000) {
    this.showAlert('info', message, duration);
  },

  /**
   * Get or create alert container
   */
  getOrCreateAlertContainer() {
    let container = document.getElementById('adminAlertContainer');

    if (!container) {
      container = document.createElement('div');
      container.id = 'adminAlertContainer';
      container.style.position = 'fixed';
      container.style.top = '20px';
      container.style.right = '20px';
      container.style.zIndex = '9999';
      container.style.maxWidth = '400px';
      document.body.appendChild(container);
    }

    return container;
  },

  /**
   * Confirm action with modal
   */
  async confirm(message, title = 'Xác nhận') {
    return new Promise((resolve) => {
      const confirmed = window.confirm(`${title}\n\n${message}`);
      resolve(confirmed);
    });
  },

  /**
   * Show loading spinner
   */
  showLoading(targetElement) {
    if (typeof targetElement === 'string') {
      targetElement = document.querySelector(targetElement);
    }

    if (!targetElement) return;

    targetElement.innerHTML = `
      <div class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <p class="mt-3">Đang tải...</p>
      </div>
    `;
  },

  /**
   * Show empty state
   */
  showEmptyState(targetElement, message = 'Không có dữ liệu') {
    if (typeof targetElement === 'string') {
      targetElement = document.querySelector(targetElement);
    }

    if (!targetElement) return;

    targetElement.innerHTML = `
      <div class="text-center py-5 text-muted">
        <i class="bi bi-inbox" style="font-size: 3rem;"></i>
        <p class="mt-3">${message}</p>
      </div>
    `;
  },

  /**
   * Get auction status badge HTML
   */
  getAuctionStatusBadge(status) {
    const badges = {
      'ACTIVE': '<span class="badge bg-success">Đang diễn ra</span>',
      'PENDING': '<span class="badge bg-warning">Chờ bắt đầu</span>',
      'ENDED': '<span class="badge bg-secondary">Đã kết thúc</span>',
      'CANCELLED': '<span class="badge bg-danger">Đã hủy</span>'
    };

    return badges[status] || `<span class="badge bg-secondary">${status}</span>`;
  },

  /**
   * Get user status badge HTML
   */
  getUserStatusBadge(isActive) {
    return isActive
      ? '<span class="badge bg-success">Hoạt động</span>'
      : '<span class="badge bg-danger">Bị khóa</span>';
  },

  /**
   * Get user role badge HTML
   */
  getUserRoleBadge(role) {
    return role === 'ADMIN'
      ? '<span class="badge bg-primary">Admin</span>'
      : '<span class="badge bg-secondary">User</span>';
  },

  /**
   * Truncate text
   */
  truncateText(text, maxLength = 50) {
    if (!text) return '';
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
  },

  /**
   * Escape HTML to prevent XSS
   */
  escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
  },

  /**
   * Debounce function
   */
  debounce(func, delay = 300) {
    let timeoutId;
    return function (...args) {
      clearTimeout(timeoutId);
      timeoutId = setTimeout(() => func.apply(this, args), delay);
    };
  },

  /**
   * Format number with commas
   */
  formatNumber(num) {
    if (num === null || num === undefined) return '0';
    return new Intl.NumberFormat('vi-VN').format(num);
  },

  /**
   * Copy text to clipboard
   */
  async copyToClipboard(text) {
    try {
      await navigator.clipboard.writeText(text);
      this.showSuccess('Đã copy vào clipboard');
      return true;
    } catch (error) {
      console.error('Copy failed:', error);
      this.showError('Copy thất bại');
      return false;
    }
  },

  /**
   * Download data as JSON file
   */
  downloadJSON(data, filename = 'data.json') {
    const json = JSON.stringify(data, null, 2);
    const blob = new Blob([json], { type: 'application/json' });
    const url = URL.createObjectURL(blob);

    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();

    URL.revokeObjectURL(url);
    this.showSuccess(`Đã tải xuống ${filename}`);
  },

  /**
   * Download data as CSV file
   */
  downloadCSV(data, filename = 'data.csv') {
    if (!data || data.length === 0) {
      this.showError('Không có dữ liệu để tải xuống');
      return;
    }

    // Get headers from first object
    const headers = Object.keys(data[0]);

    // Create CSV content
    let csv = headers.join(',') + '\n';

    data.forEach(row => {
      const values = headers.map(header => {
        const value = row[header] ?? '';
        // Escape commas and quotes
        return `"${String(value).replace(/"/g, '""')}"`;
      });
      csv += values.join(',') + '\n';
    });

    // Download
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);

    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();

    URL.revokeObjectURL(url);
    this.showSuccess(`Đã tải xuống ${filename}`);
  }
};

// Make AdminUtils available globally
window.AdminUtils = AdminUtils;

console.log('[Admin Utils] Module loaded');
