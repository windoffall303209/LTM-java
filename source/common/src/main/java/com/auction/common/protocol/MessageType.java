package com.auction.common.protocol;

/**
 * Định nghĩa các loại message trong hệ thống
 */
public class MessageType {
    
    // ========== CLIENT → SERVER ==========
    public static final String REGISTER = "REGISTER";
    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";
    public static final String LIST_AUCTIONS = "LIST_AUCTIONS";
    public static final String GET_AUCTION = "GET_AUCTION";
    public static final String JOIN_AUCTION = "JOIN_AUCTION";
    public static final String LEAVE_AUCTION = "LEAVE_AUCTION";
    public static final String BID = "BID";
    public static final String AUTO_BID = "AUTO_BID";
    public static final String GET_HISTORY = "GET_HISTORY";
    public static final String CREATE_AUCTION = "CREATE_AUCTION";
    public static final String DELETE_AUCTION = "DELETE_AUCTION";
    public static final String BAN_USER = "BAN_USER";
    public static final String GET_STATISTICS = "GET_STATISTICS";
    
    // ========== SERVER → CLIENT ==========
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";
    public static final String REGISTER_SUCCESS = "REGISTER_SUCCESS";
    public static final String REGISTER_FAILED = "REGISTER_FAILED";
    public static final String AUCTION_LIST = "AUCTION_LIST";
    public static final String AUCTION_INFO = "AUCTION_INFO";
    public static final String BID_SUCCESS = "BID_SUCCESS";
    public static final String BID_FAILED = "BID_FAILED";
    public static final String BID_UPDATE = "BID_UPDATE";
    public static final String OUTBID = "OUTBID";
    public static final String AUCTION_ENDING_SOON = "AUCTION_ENDING_SOON";
    public static final String AUCTION_EXTENDED = "AUCTION_EXTENDED";
    public static final String AUCTION_ENDED = "AUCTION_ENDED";
    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";
    
    // Protocol delimiter
    public static final String DELIMITER = "|";
    public static final String ITEM_DELIMITER = ";";
    public static final String FIELD_DELIMITER = ":";
}

