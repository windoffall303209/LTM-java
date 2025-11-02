package com.auction.exception;

/**
 * BadRequestException
 * Throw khi request không hợp lệ
 *
 * Ví dụ:
 * - Bid amount < current price
 * - Username đã tồn tại khi đăng ký
 * - Auction đã kết thúc không thể bid
 * - Password không khớp
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
