package com.nagp.hotelbooking.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;


@Entity
public class Location {
    @Id
    private final String locationId;
    private String city;
    private int capacity;
    private int price;

    public Location() {
        this.locationId = UUID.randomUUID().toString();
    }

    public Location(String city, int capacity, int price) {
        if (city == null || city.trim().equals("")) throw new IllegalArgumentException("City name is invalid");
        if (capacity <= 0) throw new IllegalArgumentException("Invalid capacity " + capacity);
        if (price <= 0) throw new IllegalArgumentException("Invalid price " + capacity);
        this.locationId = UUID.randomUUID().toString();
        this.city = city;
        this.price = price;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        String builder = "Hotel{id=" + locationId +
                ", city=" + city +
                ", price=" + price +
                ", capacity=" + capacity +
                "}";
        return builder;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getCity() {
        return city;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPrice() {
        return price;
    }
}
