package com.auction.exception;

/**
 * ResourceNotFoundException
 * Throw khi không tìm thấy resource (user, auction, bid, etc.)
 *
 * Ví dụ:
 * - Không tìm thấy user với ID
 * - Không tìm thấy auction với ID
 * - Không tìm thấy bid với ID
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Không tìm thấy %s với %s = %s", resourceName, fieldName, fieldValue));
    }
}
