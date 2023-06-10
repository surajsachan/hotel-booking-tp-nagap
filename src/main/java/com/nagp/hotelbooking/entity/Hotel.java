package com.nagp.hotelbooking.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Hotel {
    @Id
    private final String hotelId;
    private String name;
    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "hotel_location", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "location_id")
    private final Set<String> locations;

    public Hotel() {
        this.hotelId = UUID.randomUUID().toString();
        this.locations = new HashSet<>();
    }

    public Hotel(String name) {
        if (name == null || name.trim().equals("")) throw new IllegalArgumentException("Please provide a valid name ");
        this.name = name.toLowerCase().trim();
        this.hotelId = UUID.randomUUID().toString();
        this.locations = new HashSet<>();
    }

    public void addLocation(String locationId) {
        this.locations.add(locationId);
    }

    @Override
    public String toString() {
        String builder = "Hotel{id=" + hotelId +
                ", name=" + name +
                ", location=" + locations +
                "}";
        return builder;
    }

    public String getName() {
        return name;
    }

    public String getHotelId() {
        return hotelId;
    }

    public Set<String> getLocations() {
        return locations;
    }
}
