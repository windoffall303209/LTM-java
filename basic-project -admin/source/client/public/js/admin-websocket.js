/**
 * ADMIN WebSocket Handler
 * Xử lý WebSocket connections và real-time updates cho Admin Panel
 */

const AdminWebSocket = {
  stompClient: null,
  isConnected: false,
  reconnectAttempts: 0,
  maxReconnectAttempts: 5,
  reconnectDelay: 3000,
  subscriptions: [],

  /**
   * Connect to WebSocket server
   */
  connect() {
    if (this.isConnected) {
      console.log('[Admin WS] Already connected');
      return;
    }

    console.log('[Admin WS] Connecting to WebSocket server...');

    const socket = new SockJS(window.ADMIN_API_CONFIG.WS_URL);
    this.stompClient = Stomp.over(socket);

    // Disable debug logging
    this.stompClient.debug = () => {};

    this.stompClient.connect(
      {},
      (frame) => this.onConnected(frame),
      (error) => this.onError(error)
    );
  },

  /**
   * On connected callback
   */
  onConnected(frame) {
    console.log('[Admin WS] Connected:', frame);
    this.isConnected = true;
    this.reconnectAttempts = 0;

    // Subscribe to admin topics
    this.subscribeToAuctions();

    // Notify listeners
    this.notifyConnectionListeners(true);
  },

  /**
   * On error callback
   */
  onError(error) {
    console.error('[Admin WS] Connection error:', error);
    this.isConnected = false;

    // Notify listeners
    this.notifyConnectionListeners(false);

    // Attempt to reconnect
    this.attemptReconnect();
  },

  /**
   * Disconnect from WebSocket
   */
  disconnect() {
    if (this.stompClient && this.isConnected) {
      console.log('[Admin WS] Disconnecting...');

      // Unsubscribe all
      this.subscriptions.forEach(sub => {
        if (sub && typeof sub.unsubscribe === 'function') {
          sub.unsubscribe();
        }
      });
      this.subscriptions = [];

      this.stompClient.disconnect(() => {
        console.log('[Admin WS] Disconnected');
        this.isConnected = false;
      });
    }
  },

  /**
   * Attempt to reconnect
   */
  attemptReconnect() {
    if (this.reconnectAttempts >= this.maxReconnectAttempts) {
      console.error('[Admin WS] Max reconnect attempts reached');
      AdminUtils.showError('Mất kết nối WebSocket. Vui lòng tải lại trang.');
      return;
    }

    this.reconnectAttempts++;
    console.log(`[Admin WS] Reconnecting... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`);

    setTimeout(() => {
      this.connect();
    }, this.reconnectDelay);
  },

  /**
   * Subscribe to all auctions updates
   */
  subscribeToAuctions() {
    if (!this.isConnected) {
      console.warn('[Admin WS] Not connected, cannot subscribe');
      return;
    }

    const subscription = this.stompClient.subscribe('/topic/auctions', (message) => {
      try {
        const data = JSON.parse(message.body);
        console.log('[Admin WS] Auction event received:', data);

        // Notify listeners
        this.notifyAuctionListeners(data);

      } catch (error) {
        console.error('[Admin WS] Error parsing auction message:', error);
      }
    });

    this.subscriptions.push(subscription);
    console.log('[Admin WS] Subscribed to /topic/auctions');
  },

  /**
   * Subscribe to specific auction updates
   */
  subscribeToAuction(auctionId, callback) {
    if (!this.isConnected) {
      console.warn('[Admin WS] Not connected, cannot subscribe to auction');
      return null;
    }

    const subscription = this.stompClient.subscribe(`/topic/auction/${auctionId}`, (message) => {
      try {
        const data = JSON.parse(message.body);
        console.log(`[Admin WS] Auction ${auctionId} event:`, data);

        if (typeof callback === 'function') {
          callback(data);
        }
      } catch (error) {
        console.error('[Admin WS] Error parsing auction message:', error);
      }
    });

    this.subscriptions.push(subscription);
    console.log(`[Admin WS] Subscribed to /topic/auction/${auctionId}`);

    return subscription;
  },

  /**
   * Unsubscribe from auction
   */
  unsubscribeFromAuction(subscription) {
    if (subscription && typeof subscription.unsubscribe === 'function') {
      subscription.unsubscribe();

      // Remove from subscriptions array
      const index = this.subscriptions.indexOf(subscription);
      if (index > -1) {
        this.subscriptions.splice(index, 1);
      }

      console.log('[Admin WS] Unsubscribed from auction');
    }
  },

  /**
   * Connection listeners
   */
  connectionListeners: [],

  addConnectionListener(callback) {
    if (typeof callback === 'function') {
      this.connectionListeners.push(callback);
    }
  },

  removeConnectionListener(callback) {
    const index = this.connectionListeners.indexOf(callback);
    if (index > -1) {
      this.connectionListeners.splice(index, 1);
    }
  },

  notifyConnectionListeners(isConnected) {
    this.connectionListeners.forEach(callback => {
      try {
        callback(isConnected);
      } catch (error) {
        console.error('[Admin WS] Error in connection listener:', error);
      }
    });
  },

  /**
   * Auction event listeners
   */
  auctionListeners: [],

  addAuctionListener(callback) {
    if (typeof callback === 'function') {
      this.auctionListeners.push(callback);
    }
  },

  removeAuctionListener(callback) {
    const index = this.auctionListeners.indexOf(callback);
    if (index > -1) {
      this.auctionListeners.splice(index, 1);
    }
  },

  notifyAuctionListeners(data) {
    this.auctionListeners.forEach(callback => {
      try {
        callback(data);
      } catch (error) {
        console.error('[Admin WS] Error in auction listener:', error);
      }
    });
  },

  /**
   * Handle auction events
   */
  handleAuctionEvent(event) {
    const { type, auctionId, title } = event;

    switch (type) {
      case 'AUCTION_CREATED':
        console.log('[Admin WS] Auction created:', auctionId, title);
        AdminUtils.showInfo(`Đấu giá mới: ${title}`);
        break;

      case 'AUCTION_STARTED':
        console.log('[Admin WS] Auction started:', auctionId, title);
        AdminUtils.showSuccess(`Đấu giá đã bắt đầu: ${title}`);
        break;

      case 'AUCTION_ENDED':
        console.log('[Admin WS] Auction ended:', auctionId, title);
        AdminUtils.showWarning(`Đấu giá đã kết thúc: ${title}`);
        break;

      case 'AUCTION_DELETED':
        console.log('[Admin WS] Auction deleted:', auctionId);
        AdminUtils.showError(`Đấu giá đã bị xóa`);
        break;

      case 'AUCTION_CANCELLED':
        console.log('[Admin WS] Auction cancelled:', auctionId, title);
        AdminUtils.showWarning(`Đấu giá đã bị hủy: ${title}`);
        break;

      case 'BID_UPDATE':
        console.log('[Admin WS] Bid update:', auctionId);
        // Don't show notification for every bid in admin panel
        break;

      case 'AUCTION_EXTENDED':
        console.log('[Admin WS] Auction extended:', auctionId);
        AdminUtils.showInfo(`Đấu giá đã được gia hạn`);
        break;

      default:
        console.log('[Admin WS] Unknown event type:', type);
    }
  },

  /**
   * Initialize WebSocket for admin
   */
  init() {
    console.log('[Admin WS] Initializing WebSocket...');

    // Connect to WebSocket
    this.connect();

    // Add default auction listener to handle events
    this.addAuctionListener((event) => {
      this.handleAuctionEvent(event);
    });

    // Cleanup on page unload
    window.addEventListener('beforeunload', () => {
      this.disconnect();
    });

    console.log('[Admin WS] Initialization complete');
  }
};

// Make AdminWebSocket available globally
window.AdminWebSocket = AdminWebSocket;

console.log('[Admin WebSocket] Module loaded');
