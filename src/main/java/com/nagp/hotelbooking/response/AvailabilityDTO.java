package com.nagp.hotelbooking.response;

import org.springframework.beans.factory.annotation.Value;

public interface AvailabilityDTO {

    @Value("#{target.hotelName}")
    String getHotelName();

    @Value("#{target.city}")
    String getCity();

    @Value("#{target.availability}")
    String getAvailability();
}
