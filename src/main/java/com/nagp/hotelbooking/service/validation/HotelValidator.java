package com.nagp.hotelbooking.service.validation;


import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class HotelValidator {
    private static final int MAX_ADVANCE_BOOKING_DAYS = 30;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public void validateHotelName(String hotelName) {
        if (hotelName == null || hotelName.trim().equals(""))
            throw new IllegalArgumentException("Please provide a valid name ");
    }

    /*
    dateString: MM/dd/yyyy eg 03/30/2023
     */
    public void validDateForBooking(String dateString) {
        long dif = getDaysBetweenTwoDates(LocalDate.now().format(formatter), dateString);
        if (dif < 0 || dif > MAX_ADVANCE_BOOKING_DAYS)
            throw new UnsupportedOperationException(String.format("Booking Date should be within next %d days.", MAX_ADVANCE_BOOKING_DAYS));
    }

    public void validateDateForCancellation(String dateString) {
        long dif = getDaysBetweenTwoDates(LocalDate.now().format(formatter), dateString);
        if (dif <= 0)
            throw new UnsupportedOperationException(String.format("Can only cancel a booking for a future date.", MAX_ADVANCE_BOOKING_DAYS));
    }

    private long getDaysBetweenTwoDates(String startDateStr, String endDateStr) {
        final LocalDate startDate = LocalDate.parse(startDateStr, formatter), endDate = LocalDate.parse(endDateStr, formatter);
        return Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
    }
}
