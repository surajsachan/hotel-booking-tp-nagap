package com.nagp.hotelbooking.repository;

import com.nagp.hotelbooking.entity.Booking;
import com.nagp.hotelbooking.response.AvailabilityDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, String> {

    @Query(
            value = "SELECT br1.room_id FROM BOOKING_ROOM br1 where br1.booking_id in "+
                    "(SELECT b1.booking_id FROM BOOKING b1 where b1.location_id = :locationId and b1.date = :date " +
                    "and b1.ticket_status IN ('PENDING', 'CONFIRMED')) order by br1.room_id",
            nativeQuery = true)
    List<Integer> nextRoomID(@Param("locationId") String id, @Param("date") String date);

    @Query(
            value = "SELECT HOTEL_DATA.hotelName, HOTEL_DATA.city, HOTEL_DATA.capacity - COALESCE(LOCATION_OCCUPIED.occupied, 0) as availability FROM " +
                    "(SELECT h1.name as hotelName,l1.location_id as location_id, l1.city as city, l1.capacity as capacity " +
                    "FROM HOTEL h1 INNER JOIN HOTEL_LOCATION hl1 ON h1.hotel_id = hl1.hotel_id INNER JOIN LOCATION l1 ON l1.location_id = hl1.location_id " +
                    "WHERE l1.city = :city) as HOTEL_DATA LEFT JOIN " +
                    "(SELECT l1.location_id as location_id, sum(bc.total) as occupied FROM LOCATION l1 INNER JOIN " +
                    "(SELECT b1.location_id as location_id, count(*) as total from BOOKING b1 INNER JOIN BOOKING_ROOM br1 ON " +
                    "b1.booking_id = br1.booking_id WHERE b1.date = :date AND b1.ticket_status IN ('PENDING', 'CONFIRMED') " +
                    "group by br1.booking_id) as bc ON l1.location_id = bc.location_id group by l1.location_id) as LOCATION_OCCUPIED " +
                    "ON HOTEL_DATA.location_id = LOCATION_OCCUPIED.location_id",
            nativeQuery = true)
    List<AvailabilityDTO> availabilityList(@Param("city") String city, @Param("date") String date);

    @Modifying
    @Transactional
    @Query(value = "update Booking b set b.confirmed_on = :date, b.ticket_status = :status where b.booking_id = :id", nativeQuery = true)
    void confirmBooking(@Param("id") String id, @Param("date") String confirmationDate, @Param("status") String status);


    @Modifying
    @Transactional
    @Query(value = "update Booking b set b.ticket_status = :status where b.booking_id = :id", nativeQuery = true)
    void cancelBooking(@Param("id") String id, @Param("status") String status);


    @Query(
            value = "SELECT cast(b1.booking_id as char(36)) FROM BOOKING b1 " +
                    "WHERE b1.ticket_status = 'PENDING' and " +
                    "DATEADD(MINUTE, :min, parsedatetime(b1.booked_on, 'dd-MMMM-yyyy HH:mm:ss')) < parsedatetime(:date, 'dd-MMMM-yyyy HH:mm:ss')", nativeQuery = true)
    List<String> getPendingBookingsEligibleForRejection(@Param("date") String date, @Param("min") int min);


    @Modifying
    @Transactional
    @Query(
            value = "update Booking b set b.ticketStatus = :status where b.bookingId in :ids")
    void updatePendingBookingsEligibleForRejection(@Param("ids") List<String> ids, @Param("status") String status);
}
