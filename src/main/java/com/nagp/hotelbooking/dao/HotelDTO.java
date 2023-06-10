package com.nagp.hotelbooking.dao;

import com.nagp.hotelbooking.entity.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelDTO {
    private String hotelName;
    private String hotelId;

    private final List<LocationDTO> locations;

    public HotelDTO() {
        locations = new ArrayList<>();
    }
    public String getHotelName() {
        return hotelName;
    }

    public HotelDTO setHotelName(String hotelName) {
        this.hotelName = hotelName;
        return this;
    }

    public String getHotelId() {
        return hotelId;
    }

    public HotelDTO setHotelId(String hotelId) {
        this.hotelId = hotelId;
        return this;
    }

    public List<LocationDTO> getLocationDTO() {
        return locations;
    }

    public HotelDTO addLocation(LocationDTO location) {
        this.locations.add(location);
        return this;
    }

    public static HotelDTO dtoBuilder(Hotel hotel) {
        if (hotel == null) return new HotelDTO();
        HotelDTO hotelDTO = new HotelDTO().setHotelId(hotel.getHotelId())
                .setHotelName(hotel.getName());
        return hotelDTO;
    }
}
