package com.nagp.hotelbooking.service;

import com.nagp.hotelbooking.controller.BookingRestController;
import com.nagp.hotelbooking.entity.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public final class NotificationService {

    @Value("${app.room.allowed_delay:5}")
    private int min;
    static Logger logger = LoggerFactory.getLogger(BookingRestController.class);
    public void notifyReserveBooking(String username, int amount) {
        logger.warn(new String(new char[50]).replace("\0", "="));
        logger.info(String.format("Booking reserved for user %s. Kindly pay amount Rs.%d within next %d minutes else the booking will be cancelled.", username, amount, min));
        logger.warn(new String(new char[50]).replace("\0", "="));
    }

    public void paymentSuccessNotify(String username, int amount) {
        logger.warn(new String(new char[50]).replace("\0", "="));
        logger.info(String.format("Payment Success for user %s. Amount paid = Rs.%d",username, amount));
        logger.info(String.format("Payment Status: OK. Payment Date: %s",Booking.getFormattedDate(new Date())));
        logger.warn(new String(new char[50]).replace("\0", "="));
    }

    public void notifyConfirmBooking(String username, String hotelName, List<Integer> rooms, String bookingId, int amount) {
        logger.warn(new String(new char[50]).replace("\0", "="));
        logger.info(String.format("Booking ID: %s Confirmed for user %s. @%s",bookingId, username, hotelName));
        logger.info(String.format("Amount paid is Rs.%d", amount));
        logger.info(String.format("Rooms alloted:\n", amount));
        for(int roomId: rooms) logger.info(String.format("==> %d\n", roomId));
        logger.warn(new String(new char[50]).replace("\0", "="));
    }
}
