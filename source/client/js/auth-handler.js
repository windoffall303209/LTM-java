/**
 * Authentication Handler - Login & Register
 */

// ========== LOGIN PAGE ==========
if (window.location.pathname.includes('login.html')) {
    // Redirect if already logged in
    redirectIfLoggedIn();
    
    // Connect to server
    socketClient.connect();
    
    // Handle login form
    document.getElementById('loginForm')?.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        
        if (!username || !password) {
            showAlert('Vui lòng nhập đầy đủ thông tin', 'warning');
            return;
        }
        
        // Disable button
        const loginBtn = document.getElementById('loginBtn');
        loginBtn.disabled = true;
        loginBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang đăng nhập...';
        
        // Send login request
        socketClient.send(`LOGIN|${username}|${password}`);
    });
    
    // Handle login response
    socketClient.on('LOGIN_SUCCESS', (params) => {
        const [userId, username, role] = params;
        
        // Save user info
        saveCurrentUser({
            id: parseInt(userId),
            username: username,
            role: role
        });
        
        showToast('Đăng nhập thành công!', 'success');
        
        // Redirect to dashboard
        setTimeout(() => {
            if (role === 'admin') {
                window.location.href = 'dashboard.html';
            } else {
                window.location.href = 'dashboard.html';
            }
        }, 500);
    });
    
    socketClient.on('LOGIN_FAILED', (params) => {
        const errorMsg = params[0] || 'Đăng nhập thất bại';
        showAlert(errorMsg, 'danger');
        
        // Re-enable button
        const loginBtn = document.getElementById('loginBtn');
        loginBtn.disabled = false;
        loginBtn.innerHTML = '<i class="bi bi-box-arrow-in-right"></i> Đăng Nhập';
    });
    
    // Quick login buttons
    document.querySelectorAll('.quick-login').forEach(btn => {
        btn.addEventListener('click', () => {
            const user = btn.dataset.user;
            document.getElementById('username').value = user;
            document.getElementById('password').value = 'admin123';
        });
    });
}

// ========== REGISTER PAGE ==========
if (window.location.pathname.includes('register.html')) {
    // Redirect if already logged in
    redirectIfLoggedIn();
    
    // Connect to server
    socketClient.connect();
    
    // Password strength indicator
    const passwordInput = document.getElementById('password');
    const strengthBar = document.getElementById('passwordStrength');
    
    passwordInput?.addEventListener('input', () => {
        const strength = checkPasswordStrength(passwordInput.value);
        strengthBar.className = 'progress-bar';
        
        if (strength === 'weak') {
            strengthBar.style.width = '33%';
            strengthBar.classList.add('bg-danger');
        } else if (strength === 'medium') {
            strengthBar.style.width = '66%';
            strengthBar.classList.add('bg-warning');
        } else {
            strengthBar.style.width = '100%';
            strengthBar.classList.add('bg-success');
        }
    });
    
    // Handle register form
    document.getElementById('registerForm')?.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const agreeTerms = document.getElementById('agreeTerms').checked;
        
        // Validation
        if (!username || username.length < 3) {
            showAlert('Username phải có ít nhất 3 ký tự', 'warning');
            return;
        }
        
        if (!isValidEmail(email)) {
            showAlert('Email không hợp lệ', 'warning');
            return;
        }
        
        if (password.length < 6) {
            showAlert('Password phải có ít nhất 6 ký tự', 'warning');
            return;
        }
        
        if (password !== confirmPassword) {
            showAlert('Password không khớp', 'warning');
            return;
        }
        
        if (!agreeTerms) {
            showAlert('Vui lòng đồng ý với điều khoản sử dụng', 'warning');
            return;
        }
        
        // Disable button
        const registerBtn = document.getElementById('registerBtn');
        registerBtn.disabled = true;
        registerBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang đăng ký...';
        
        // Send register request
        socketClient.send(`REGISTER|${username}|${password}|${email}`);
    });
    
    // Handle register response
    socketClient.on('REGISTER_SUCCESS', (params) => {
        showAlert('Đăng ký thành công! Đang chuyển đến trang đăng nhập...', 'success');
        
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 1500);
    });
    
    socketClient.on('REGISTER_FAILED', (params) => {
        const errorMsg = params[0] || 'Đăng ký thất bại';
        showAlert(errorMsg, 'danger');
        
        // Re-enable button
        const registerBtn = document.getElementById('registerBtn');
        registerBtn.disabled = false;
        registerBtn.innerHTML = '<i class="bi bi-person-plus"></i> Đăng Ký';
    });
}

// ========== LOGOUT ==========
function handleLogout() {
    if (confirm('Bạn có chắc muốn đăng xuất?')) {
        // Send logout to server
        socketClient.send('LOGOUT');
        
        // Clear local data
        clearCurrentUser();
        
        // Disconnect socket
        socketClient.disconnect();
        
        // Redirect to login
        window.location.href = 'login.html';
    }
}

// Attach logout handler
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('logoutBtn')?.addEventListener('click', handleLogout);
});

