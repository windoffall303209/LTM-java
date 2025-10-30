package com.auction.common.protocol;

/**
 * Class tiện ích để build và parse messages theo protocol
 */
public class Protocol {
    
    /**
     * Build message từ command và params
     */
    public static String buildMessage(String command, String... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(command);
        
        for (String param : params) {
            sb.append(MessageType.DELIMITER);
            sb.append(param != null ? param : "");
        }
        
        return sb.toString();
    }
    
    /**
     * Parse message thành command và params
     */
    public static String[] parseMessage(String message) {
        if (message == null || message.isEmpty()) {
            return new String[0];
        }
        return message.split("\\" + MessageType.DELIMITER);
    }
    
    /**
     * Lấy command từ message
     */
    public static String getCommand(String message) {
        String[] parts = parseMessage(message);
        return parts.length > 0 ? parts[0] : "";
    }
    
    /**
     * Lấy params từ message (không bao gồm command)
     */
    public static String[] getParams(String message) {
        String[] parts = parseMessage(message);
        if (parts.length <= 1) {
            return new String[0];
        }
        
        String[] params = new String[parts.length - 1];
        System.arraycopy(parts, 1, params, 0, params.length);
        return params;
    }
    
    /**
     * Validate message format
     */
    public static boolean isValidMessage(String message) {
        return message != null && !message.trim().isEmpty();
    }
}

