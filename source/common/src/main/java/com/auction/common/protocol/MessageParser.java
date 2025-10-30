package com.auction.common.protocol;

import java.util.*;

/**
 * Parser nâng cao cho các message phức tạp
 */
public class MessageParser {
    
    /**
     * Parse danh sách auctions
     * Format: AUCTION_LIST|id:name:price:time;id:name:price:time;...
     */
    public static List<Map<String, String>> parseAuctionList(String message) {
        List<Map<String, String>> auctions = new ArrayList<>();
        String[] parts = Protocol.parseMessage(message);
        
        if (parts.length < 2) {
            return auctions;
        }
        
        String[] items = parts[1].split(MessageType.ITEM_DELIMITER);
        for (String item : items) {
            String[] fields = item.split(MessageType.FIELD_DELIMITER);
            if (fields.length >= 4) {
                Map<String, String> auction = new HashMap<>();
                auction.put("id", fields[0]);
                auction.put("name", fields[1]);
                auction.put("price", fields[2]);
                auction.put("timeLeft", fields[3]);
                if (fields.length > 4) {
                    auction.put("winner", fields[4]);
                }
                auctions.add(auction);
            }
        }
        
        return auctions;
    }
    
    /**
     * Build auction list message
     */
    public static String buildAuctionList(List<Map<String, String>> auctions) {
        StringBuilder sb = new StringBuilder();
        sb.append(MessageType.AUCTION_LIST);
        sb.append(MessageType.DELIMITER);
        
        for (int i = 0; i < auctions.size(); i++) {
            Map<String, String> auction = auctions.get(i);
            sb.append(auction.get("id")).append(MessageType.FIELD_DELIMITER);
            sb.append(auction.get("name")).append(MessageType.FIELD_DELIMITER);
            sb.append(auction.get("price")).append(MessageType.FIELD_DELIMITER);
            sb.append(auction.get("timeLeft"));
            
            if (auction.containsKey("winner")) {
                sb.append(MessageType.FIELD_DELIMITER);
                sb.append(auction.get("winner"));
            }
            
            if (i < auctions.size() - 1) {
                sb.append(MessageType.ITEM_DELIMITER);
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Parse bid history
     * Format: BID_HISTORY|username:amount:time;username:amount:time;...
     */
    public static List<Map<String, String>> parseBidHistory(String message) {
        List<Map<String, String>> history = new ArrayList<>();
        String[] parts = Protocol.parseMessage(message);
        
        if (parts.length < 2) {
            return history;
        }
        
        String[] items = parts[1].split(MessageType.ITEM_DELIMITER);
        for (String item : items) {
            String[] fields = item.split(MessageType.FIELD_DELIMITER);
            if (fields.length >= 3) {
                Map<String, String> bid = new HashMap<>();
                bid.put("username", fields[0]);
                bid.put("amount", fields[1]);
                bid.put("time", fields[2]);
                history.add(bid);
            }
        }
        
        return history;
    }
}

