/**
 * ADMIN Authentication Utility
 * Xử lý authentication riêng cho Admin Panel
 */

const AdminAuth = {
  /**
   * Check if user is logged in
   */
  isLoggedIn() {
    const userId = localStorage.getItem('userId');
    const username = localStorage.getItem('username');
    return !!(userId && username);
  },

  /**
   * Check if user is admin
   */
  isAdmin() {
    const userRole = localStorage.getItem('userRole');
    return userRole === 'ADMIN';
  },

  /**
   * Get current user ID
   */
  getUserId() {
    return parseInt(localStorage.getItem('userId')) || null;
  },

  /**
   * Get current username
   */
  getUsername() {
    return localStorage.getItem('username') || null;
  },

  /**
   * Get current user role
   */
  getUserRole() {
    return localStorage.getItem('userRole') || null;
  },

  /**
   * Require admin authentication - redirect if not admin
   * This is the main auth check for all admin pages
   */
  requireAdmin() {
    if (!this.isLoggedIn()) {
      console.warn('[Admin Auth] Not authenticated, redirecting to login...');
      this.redirectToLogin();
      return false;
    }

    if (!this.isAdmin()) {
      console.warn('[Admin Auth] Not admin, redirecting to user dashboard...');
      this.redirectToUserDashboard();
      return false;
    }

    console.log('[Admin Auth] Admin authenticated:', this.getUsername());
    return true;
  },

  /**
   * Redirect to login page
   */
  redirectToLogin() {
    window.location.href = '/login.html';
  },

  /**
   * Redirect to user dashboard
   */
  redirectToUserDashboard() {
    window.location.href = '/dashboard.html';
  },

  /**
   * Logout admin
   */
  async logout() {
    try {
      console.log('[Admin Auth] Logging out...');

      await fetch(`${window.ADMIN_API_CONFIG.BASE_URL}/api/auth/logout`, {
        method: 'POST',
        credentials: 'include'
      });

      console.log('[Admin Auth] Logout successful');
    } catch (error) {
      console.error('[Admin Auth] Logout error:', error);
    } finally {
      // Clear all localStorage
      localStorage.clear();

      // Redirect to login
      this.redirectToLogin();
    }
  },

  /**
   * Get auth headers for API requests
   */
  getAuthHeaders() {
    return {
      'Content-Type': 'application/json',
    };
  },

  /**
   * Make authenticated API request
   */
  async apiRequest(url, options = {}) {
    const defaultOptions = {
      credentials: 'include',
      headers: this.getAuthHeaders(),
    };

    const mergedOptions = {
      ...defaultOptions,
      ...options,
      headers: {
        ...defaultOptions.headers,
        ...(options.headers || {})
      }
    };

    try {
      const response = await fetch(url, mergedOptions);

      // Check if unauthorized (401) - redirect to login
      if (response.status === 401 || response.status === 403) {
        console.warn('[Admin Auth] Unauthorized access, redirecting...');
        this.redirectToLogin();
        throw new Error('Unauthorized');
      }

      return response;
    } catch (error) {
      console.error('[Admin Auth] API request error:', error);
      throw error;
    }
  },

  /**
   * Display user info in admin panel
   */
  displayUserInfo() {
    const username = this.getUsername();
    const userRole = this.getUserRole();

    // Update all elements with class 'admin-username'
    document.querySelectorAll('.admin-username').forEach(el => {
      el.textContent = username || 'Admin';
    });

    // Update all elements with class 'admin-role'
    document.querySelectorAll('.admin-role').forEach(el => {
      el.textContent = userRole || 'ADMIN';
    });
  },

  /**
   * Initialize admin page
   * Call this on every admin page load
   */
  init() {
    console.log('[Admin Auth] Initializing admin authentication...');

    // Check admin authentication
    if (!this.requireAdmin()) {
      return false;
    }

    // Display user info
    this.displayUserInfo();

    // Setup logout buttons
    this.setupLogoutButtons();

    console.log('[Admin Auth] Initialization complete');
    return true;
  },

  /**
   * Setup logout button event listeners
   */
  setupLogoutButtons() {
    document.querySelectorAll('.admin-logout-btn, .logout-btn').forEach(btn => {
      btn.addEventListener('click', (e) => {
        e.preventDefault();
        this.logout();
      });
    });
  },

  /**
   * Debug: Show current auth state
   */
  debugAuthState() {
    console.log('=== Admin Auth State ===');
    console.log('userId:', localStorage.getItem('userId'));
    console.log('username:', localStorage.getItem('username'));
    console.log('userRole:', localStorage.getItem('userRole'));
    console.log('isLoggedIn:', this.isLoggedIn());
    console.log('isAdmin:', this.isAdmin());
    console.log('========================');
  }
};

// Make AdminAuth available globally
window.AdminAuth = AdminAuth;

console.log('[Admin Auth] Module loaded');
