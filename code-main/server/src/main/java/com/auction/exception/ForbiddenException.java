package com.auction.exception;

/**
 * ForbiddenException
 * Throw khi user không có quyền truy cập resource
 *
 * Ví dụ:
 * - User thường truy cập admin endpoint
 * - User cố gắng sửa auction của người khác
 * - User cố gắng xem thông tin private của người khác
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
