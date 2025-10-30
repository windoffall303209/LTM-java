package com.auction.common.dto;

import java.io.Serializable;

/**
 * Data Transfer Object cho Response chung
 */
public class ResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean success;
    private String message;
    private Object data;
    
    public ResponseDTO() {}
    
    public ResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public ResponseDTO(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    // Static factory methods
    public static ResponseDTO success(String message) {
        return new ResponseDTO(true, message);
    }
    
    public static ResponseDTO success(String message, Object data) {
        return new ResponseDTO(true, message, data);
    }
    
    public static ResponseDTO error(String message) {
        return new ResponseDTO(false, message);
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    
    @Override
    public String toString() {
        return "ResponseDTO{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

