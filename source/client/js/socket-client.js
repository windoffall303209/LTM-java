/**
 * WebSocket Client - Kết nối đến server
 */

class SocketClient {
    constructor() {
        this.socket = null;
        this.connected = false;
        this.reconnectAttempts = 0;
        this.messageHandlers = {};
        this.connectionCallbacks = [];
    }
    
    /**
     * Kết nối đến server
     */
    connect() {
        const serverURL = getServerURL();
        console.log('Connecting to:', serverURL);
        
        try {
            this.socket = new WebSocket(serverURL);
            
            this.socket.onopen = () => {
                console.log('✅ Connected to server');
                this.connected = true;
                this.reconnectAttempts = 0;
                this.updateConnectionStatus('connected');
                this.triggerConnectionCallbacks(true);
            };
            
            this.socket.onmessage = (event) => {
                this.handleMessage(event.data);
            };
            
            this.socket.onerror = (error) => {
                console.error('❌ WebSocket error:', error);
                this.updateConnectionStatus('error');
            };
            
            this.socket.onclose = () => {
                console.log('🔌 Connection closed');
                this.connected = false;
                this.updateConnectionStatus('disconnected');
                this.triggerConnectionCallbacks(false);
                this.attemptReconnect();
            };
            
        } catch (error) {
            console.error('Failed to create WebSocket:', error);
            this.updateConnectionStatus('error');
        }
    }
    
    /**
     * Gửi message đến server
     */
    send(message) {
        if (this.connected && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(message);
            console.log('📤 Sent:', message);
            return true;
        } else {
            console.error('Cannot send: Not connected');
            showToast('Không thể gửi: Chưa kết nối server', 'error');
            return false;
        }
    }
    
    /**
     * Xử lý message từ server
     */
    handleMessage(message) {
        console.log('📥 Received:', message);
        
        // Parse message: COMMAND|param1|param2|...
        const parts = message.split('|');
        const command = parts[0];
        const params = parts.slice(1);
        
        // Trigger handlers for this command
        if (this.messageHandlers[command]) {
            this.messageHandlers[command].forEach(handler => {
                try {
                    handler(params, message);
                } catch (error) {
                    console.error('Error in message handler:', error);
                }
            });
        }
        
        // Trigger global handlers
        if (this.messageHandlers['*']) {
            this.messageHandlers['*'].forEach(handler => {
                handler(command, params, message);
            });
        }
    }
    
    /**
     * Đăng ký handler cho message type
     */
    on(messageType, handler) {
        if (!this.messageHandlers[messageType]) {
            this.messageHandlers[messageType] = [];
        }
        this.messageHandlers[messageType].push(handler);
    }
    
    /**
     * Xóa handler
     */
    off(messageType, handler) {
        if (this.messageHandlers[messageType]) {
            this.messageHandlers[messageType] = this.messageHandlers[messageType]
                .filter(h => h !== handler);
        }
    }
    
    /**
     * Callback khi connection status thay đổi
     */
    onConnectionChange(callback) {
        this.connectionCallbacks.push(callback);
    }
    
    triggerConnectionCallbacks(connected) {
        this.connectionCallbacks.forEach(cb => cb(connected));
    }
    
    /**
     * Attempt to reconnect
     */
    attemptReconnect() {
        if (!CONFIG.RECONNECT.enabled) return;
        
        if (this.reconnectAttempts < CONFIG.RECONNECT.maxAttempts) {
            this.reconnectAttempts++;
            console.log(`Reconnecting... (${this.reconnectAttempts}/${CONFIG.RECONNECT.maxAttempts})`);
            
            setTimeout(() => {
                this.connect();
            }, CONFIG.RECONNECT.delay);
        } else {
            console.error('Max reconnect attempts reached');
            showToast('Không thể kết nối server. Vui lòng refresh trang.', 'error');
        }
    }
    
    /**
     * Update connection status indicator
     */
    updateConnectionStatus(status) {
        const indicator = document.getElementById('connectionIndicator');
        const text = document.getElementById('connectionText');
        
        if (indicator) {
            indicator.className = '';
            if (status === 'connected') {
                indicator.style.color = '#198754';
                if (text) text.textContent = 'Đã kết nối';
            } else if (status === 'disconnected') {
                indicator.style.color = '#dc3545';
                if (text) text.textContent = 'Mất kết nối';
            } else if (status === 'error') {
                indicator.style.color = '#ffc107';
                if (text) text.textContent = 'Lỗi kết nối';
            }
        }
    }
    
    /**
     * Đóng connection
     */
    disconnect() {
        if (this.socket) {
            this.socket.close();
            this.socket = null;
            this.connected = false;
        }
    }
    
    /**
     * Check if connected
     */
    isConnected() {
        return this.connected && this.socket && this.socket.readyState === WebSocket.OPEN;
    }
}

// Create global instance
const socketClient = new SocketClient();

// Auto connect when page loads (nếu đã login)
if (typeof window !== 'undefined') {
    window.addEventListener('DOMContentLoaded', () => {
        if (isLoggedIn() && window.location.pathname.includes('dashboard')) {
            socketClient.connect();
        }
    });
}

