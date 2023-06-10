package com.nagp.hotelbooking.dao;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class HotelBookingDao {
    public static Map<String, String> bookingIdTransactionIdMap = new HashMap<>();
    public void insertToBookingIdTransactioIdMap(String bookingId, String transactionId) {
        bookingIdTransactionIdMap.put(bookingId, transactionId);
    }
    public String fetchTransactionIdByBookingId(String bookingId) {
        return bookingIdTransactionIdMap.get(bookingId);
    }
}
