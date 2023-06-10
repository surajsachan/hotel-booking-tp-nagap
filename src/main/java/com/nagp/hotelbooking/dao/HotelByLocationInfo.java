package com.nagp.hotelbooking.dao;

import org.springframework.beans.factory.annotation.Value;

public interface HotelByLocationInfo {
    @Value("#{target.hotelName}")
    String getHotelName();


    @Value("#{target.city}")
    String getCity();

    @Value("#{target.capacity}")
    String getCapacity();

    @Value("#{target.price}")
    String getPrice();

    @Value("#{target.hotelID}")
    String getHotelId();

    @Value("#{target.locationID}")
    String getLocationId();

}
