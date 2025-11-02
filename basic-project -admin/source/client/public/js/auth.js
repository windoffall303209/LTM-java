/**
 * File: auth.js
 * Utilities cho authentication và authorization
 *
 * Cung cấp các functions:
 * - Kiểm tra login status
 * - Lưu/xóa auth data
 * - Redirect dựa trên role
 * - Helpers để protect pages
 */

/**
 * Kiểm tra user đã đăng nhập chưa
 * @returns {boolean} true nếu đã login
 */
function isAuthenticated() {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    return token !== null && user !== null;
}

/**
 * Lấy thông tin user hiện tại
 * @returns {Object|null} User object hoặc null
 */
function getCurrentUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
}

/**
 * Lấy JWT token
 * @returns {string|null}
 */
function getToken() {
    return localStorage.getItem('token');
}

/**
 * Kiểm tra user hiện tại có phải admin không
 * @returns {boolean}
 */
function isAdmin() {
    const user = getCurrentUser();
    return user && user.role === 'ADMIN';
}

/**
 * Kiểm tra user hiện tại có phải user thường không
 * @returns {boolean}
 */
function isUser() {
    const user = getCurrentUser();
    return user && user.role === 'USER';
}

/**
 * Lưu auth data vào localStorage
 * @param {string} token - JWT token
 * @param {Object} user - User object
 */
function saveAuth(token, user) {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
}

/**
 * Xóa auth data khỏi localStorage
 */
function clearAuth() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
}

/**
 * Require authentication
 * Redirect to login nếu chưa đăng nhập
 * @returns {boolean} true nếu đã login
 */
function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

/**
 * Require admin role
 * Redirect nếu không phải admin
 * @returns {boolean} true nếu là admin
 */
function requireAdmin() {
    if (!isAuthenticated()) {
        window.location.href = 'login.html';
        return false;
    }
    if (!isAdmin()) {
        alert('Truy cập bị từ chối. Chỉ Admin mới có quyền.');
        window.location.href = 'dashboard.html';
        return false;
    }
    return true;
}

/**
 * Logout
 * Xóa auth data và redirect về login
 */
function logout() {
    // Chỗ này sẽ gọi logout API nếu cần
    // fetch(getApiUrl('/auth/logout'), {
    //     method: 'POST',
    //     headers: getAuthHeaders()
    // });

    clearAuth();
    window.location.href = 'login.html';
}

/**
 * Auto-redirect nếu đã login
 * Dùng cho login/register pages
 */
function redirectIfAuthenticated() {
    if (isAuthenticated()) {
        window.location.href = 'dashboard.html';
    }
}
