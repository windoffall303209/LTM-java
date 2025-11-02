package com.auction.exception;

/**
 * UnauthorizedException
 * Throw khi user chưa đăng nhập hoặc token không hợp lệ
 *
 * Ví dụ:
 * - Truy cập protected endpoint mà chưa đăng nhập
 * - Token hết hạn
 * - Token không hợp lệ
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
