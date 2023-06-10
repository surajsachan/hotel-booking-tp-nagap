package com.nagp.hotelbooking.repository;

import com.nagp.hotelbooking.entity.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface LocationRepository extends CrudRepository<Location, String> {
    @Query(
            value = "SELECT * FROM LOCATION l WHERE l.city =:city",
            nativeQuery = true)
    Collection<Location> findByCity(@Param("city") String city);

    @Query(
            value = "SELECT available_capacity FROM LOCATION l WHERE l.location_id = :id",
            nativeQuery = true)
    int findAvailableCapacity(@Param("id") String id);


    @Query(
            value = "SELECT h1.name FROM HOTEL h1 INNER JOIN HOTEL_LOCATION hl1 ON h1.hotel_id = hl1.hotel_id " +
                    "WHERE hl1.location_id = :id",
            nativeQuery = true)
    String findHotelNameFromLocationId(@Param("id") String id);


}
