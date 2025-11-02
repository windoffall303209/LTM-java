/**
 * WebSocket Client Utility
 * Kết nối và quản lý WebSocket connection với server
 *
 * Sử dụng STOMP over WebSocket với SockJS fallback
 *
 * Cách dùng:
 * 1. const ws = new WebSocketClient();
 * 2. ws.connect(() => {
 *      ws.subscribe(auctionId, (message) => { ... });
 *      ws.send(auctionId, { ... });
 *    });
 * 3. ws.disconnect();
 */

class WebSocketClient {
    constructor() {
        this.stompClient = null;
        this.connected = false;
        this.subscriptions = new Map();
    }

    /**
     * Kết nối tới WebSocket server
     * @param {Function} onConnected - Callback khi kết nối thành công
     * @param {Function} onError - Callback khi có lỗi
     */
    connect(onConnected, onError) {
        if (this.connected) {
            console.log('WebSocket đã kết nối rồi');
            if (onConnected) onConnected();
            return;
        }

        // Tạo WebSocket connection
        const socket = new SockJS(CONFIG.WS_URL);
        this.stompClient = Stomp.over(socket);

        // Tắt debug logs (có thể bật lại khi cần debug)
        this.stompClient.debug = null;

        // Kết nối
        this.stompClient.connect(
            {},
            (frame) => {
                console.log('WebSocket connected:', frame);
                this.connected = true;
                if (onConnected) onConnected();
            },
            (error) => {
                console.error('WebSocket connection error:', error);
                this.connected = false;
                if (onError) onError(error);
            }
        );
    }

    /**
     * Subscribe vào auction room để nhận updates
     * @param {Number} auctionId - ID của auction
     * @param {Function} onMessage - Callback khi nhận message
     */
    subscribe(auctionId, onMessage) {
        if (!this.connected) {
            console.error('WebSocket chưa kết nối! Gọi connect() trước.');
            return;
        }

        const topic = `/topic/auction/${auctionId}`;

        // Unsubscribe nếu đã subscribe rồi
        if (this.subscriptions.has(topic)) {
            this.unsubscribe(auctionId);
        }

        const subscription = this.stompClient.subscribe(topic, (message) => {
            try {
                const data = JSON.parse(message.body);
                if (onMessage) onMessage(data);
            } catch (error) {
                console.error('Error parsing WebSocket message:', error);
            }
        });

        this.subscriptions.set(topic, subscription);
        console.log(`Subscribed to ${topic}`);
    }

    /**
     * Unsubscribe khỏi auction room
     * @param {Number} auctionId - ID của auction
     */
    unsubscribe(auctionId) {
        const topic = `/topic/auction/${auctionId}`;
        const subscription = this.subscriptions.get(topic);

        if (subscription) {
            subscription.unsubscribe();
            this.subscriptions.delete(topic);
            console.log(`Unsubscribed from ${topic}`);
        }
    }

    /**
     * Gửi message join auction
     * @param {Number} auctionId - ID của auction
     * @param {String} username - Username
     */
    joinAuction(auctionId, username) {
        this.send(`/app/join/${auctionId}`, {
            username: username,
            auctionId: auctionId
        });
    }

    /**
     * Gửi message leave auction
     * @param {Number} auctionId - ID của auction
     * @param {String} username - Username
     */
    leaveAuction(auctionId, username) {
        this.send(`/app/leave/${auctionId}`, {
            username: username,
            auctionId: auctionId
        });
    }

    /**
     * Gửi message tới server
     * @param {String} destination - Destination path
     * @param {Object} message - Message object
     */
    send(destination, message) {
        if (!this.connected) {
            console.error('WebSocket chưa kết nối! Không thể gửi message.');
            return;
        }

        this.stompClient.send(destination, {}, JSON.stringify(message));
    }

    /**
     * Ngắt kết nối WebSocket
     */
    disconnect() {
        if (this.stompClient && this.connected) {
            // Unsubscribe tất cả
            this.subscriptions.forEach((subscription) => {
                subscription.unsubscribe();
            });
            this.subscriptions.clear();

            // Disconnect
            this.stompClient.disconnect(() => {
                console.log('WebSocket disconnected');
            });

            this.connected = false;
        }
    }

    /**
     * Kiểm tra trạng thái kết nối
     */
    isConnected() {
        return this.connected;
    }
}

// Export để dùng ở các file khác
// const wsClient = new WebSocketClient();
