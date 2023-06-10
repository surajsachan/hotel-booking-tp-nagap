package com.nagp.hotelbooking.repository;

import com.nagp.hotelbooking.dao.HotelByLocationInfo;
import com.nagp.hotelbooking.entity.Hotel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends CrudRepository<Hotel, String> {

    Optional<Hotel> findByName(String name);

    @Query(
            value = "SELECT h1.name as hotelName, h1.hotel_id as hotelID, l1.city as city, l1.capacity as capacity, l1.price as price, l1.location_id as locationID " +
                    "FROM HOTEL h1 INNER JOIN HOTEL_LOCATION hl1 ON h1.hotel_id = hl1.hotel_id INNER JOIN LOCATION l1 ON l1.location_id = hl1.location_id WHERE " +
                    "hl1.location_id = :locationId",
            nativeQuery = true)
    Optional<HotelByLocationInfo> findByLocationId(@Param("locationId") String id);
}