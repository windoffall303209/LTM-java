// admin-header.js - Shared header component for admin pages

/**
 * Render admin navbar with user information
 * @param {string} activePage - The current active page (dashboard, auctions, users)
 */
async function renderAdminHeader(activePage = 'dashboard') {
  const navbarContainer = document.querySelector('nav.navbar');
  if (!navbarContainer) return;

  // Get user info from localStorage
  const username = localStorage.getItem('username');
  const userId = localStorage.getItem('userId');

  let currentUser = {
    username: username,
    fullName: username,
    email: '',
    balance: 0,
    role: 'ADMIN'
  };

  // Try to load full user info from API
  try {
    const response = await fetch(`${window.API_CONFIG.BASE_URL}/api/users/${userId}`, {
      credentials: 'include'
    });
    const result = await response.json();
    if (result.success) {
      currentUser = result.data;
    }
  } catch (error) {
    console.error('Error loading user info for admin header:', error);
  }

  const displayName = currentUser.fullName || currentUser.username;

  // Helper function to get active class
  const activeClass = (page) => page === activePage ? 'active' : '';

  // Render the navbar HTML
  navbarContainer.innerHTML = `
    <div class="container-fluid">
      <a class="navbar-brand" href="dashboard.html">👑 Admin Panel</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <a class="nav-link ${activeClass('dashboard')}" href="dashboard.html">Dashboard</a>
          </li>
          <li class="nav-item">
            <a class="nav-link ${activeClass('auctions')}" href="auctions.html">Quản Lý Đấu Giá</a>
          </li>
          <li class="nav-item">
            <a class="nav-link ${activeClass('users')}" href="users.html">Quản Lý Users</a>
          </li>
        </ul>
        <ul class="navbar-nav">
          <li class="nav-item">
            <a class="nav-link" href="../dashboard.html">
              <i class="bi bi-house"></i> Trang Chủ
            </a>
          </li>
          <!-- User Dropdown -->
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle text-warning" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <i class="bi bi-person-badge"></i> <strong id="adminName">${displayName}</strong>
            </a>
            <ul class="dropdown-menu dropdown-menu-end">
              <li class="dropdown-header">
                <strong>Thông tin Admin</strong>
              </li>
              <li><hr class="dropdown-divider"></li>
              <li>
                <span class="dropdown-item-text">
                  <small class="text-muted">Tên đầy đủ:</small><br/>
                  <strong>${currentUser.fullName || 'N/A'}</strong>
                </span>
              </li>
              <li>
                <span class="dropdown-item-text">
                  <small class="text-muted">Email:</small><br/>
                  <strong>${currentUser.email || 'N/A'}</strong>
                </span>
              </li>
              <li>
                <span class="dropdown-item-text">
                  <small class="text-muted">Role:</small><br/>
                  <strong class="text-warning">${currentUser.role}</strong>
                </span>
              </li>
              <li><hr class="dropdown-divider"></li>
              <li>
                <div class="px-2">
                  <button onclick="handleAdminLogout(event)" class="btn btn-danger btn-sm w-100">
                    <i class="bi bi-box-arrow-right"></i> Đăng xuất
                  </button>
                </div>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  `;
}

/**
 * Handle logout from admin header
 */
async function handleAdminLogout(e) {
  e.preventDefault();

  try {
    await fetch(`${window.API_CONFIG.BASE_URL}/api/auth/logout`, {
      method: 'POST',
      credentials: 'include'
    });
  } catch (error) {
    console.error('Logout error:', error);
  } finally {
    localStorage.clear();
    window.location.href = '../login.html?logout=true';
  }
}
