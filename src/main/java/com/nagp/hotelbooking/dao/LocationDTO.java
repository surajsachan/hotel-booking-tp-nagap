package com.nagp.hotelbooking.dao;

import com.nagp.hotelbooking.entity.Location;

public class LocationDTO {
    private String locationId;
    private String city;
    private int capacity;
    private int price;

    public static LocationDTO dtoBuilder(Location location) {
        if (location == null) return new LocationDTO();
        return new LocationDTO().setLocationId(location.getLocationId())
                .setPrice(location.getPrice())
                .setCity(location.getCity())
                .setCapacity(location.getCapacity());
    }

    public String getLocationId() {
        return locationId;
    }

    public LocationDTO setLocationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    public String getCity() {
        return city;
    }

    public LocationDTO setCity(String city) {
        this.city = city;
        return this;
    }

    public int getCapacity() {
        return capacity;
    }

    public LocationDTO setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public LocationDTO setPrice(int price) {
        this.price = price;
        return this;
    }
}
