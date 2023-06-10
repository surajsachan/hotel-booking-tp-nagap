package com.nagp.hotelbooking.entity;

import com.nagp.hotelbooking.enums.TICKET_STATUS;
import com.nagp.hotelbooking.response.AddBookingResponse;
import com.nagp.hotelbooking.response.ConfirmBookingResponse;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Booking {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");
    @Id
    private final String bookingId;
    private String locationId;
    private String userID;
    @ElementCollection(targetClass = Integer.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "booking_room", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "room_id")
    private List<Integer> roomIds;
    private String date;
    private String ticketStatus;
    private String bookedOn;
    private String confirmedOn;

    public Booking() {
        this.bookingId = UUID.randomUUID().toString();
    }

    public Booking(String locationId, String userID, String date) {
        this.bookingId = UUID.randomUUID().toString();
        this.locationId = locationId;
        this.userID = userID;
        this.date = date;
        this.ticketStatus = TICKET_STATUS.PENDING.toString();
        this.bookedOn = getFormattedDate(new Date());
        this.roomIds = new ArrayList<>();
    }

    public static String getFormattedDate(Date date) {
        return dateFormat.format(date);
    }

    public static AddBookingResponse getPendingBookingMessage(Booking booking, int price, String hotelName, String city) {
        return AddBookingResponse.builder()
                .bookedFor(booking.getDate())
                .bookingID(booking.getBookingId())
                .bookedOn(booking.getBookedOn())
                .roomIDs(booking.getRoomIds())
                .amount(price * booking.getRoomIds().size())
                .status(booking.getTicketStatus())
                .message("Your booking has been created").build();
    }

    public static ConfirmBookingResponse getConfirmationBookingMessage(Booking booking, int price, String hotelName, String city) {
        return ConfirmBookingResponse.builder()
                .bookedFor(booking.getDate())
                .bookingID(booking.getBookingId())
                .bookedOn(booking.getBookedOn())
                .roomIDs(booking.getRoomIds())
                .amount(price * booking.getRoomIds().size())
                .status(booking.getTicketStatus())
                .confirmedOn(booking.getConfirmedOn())
                .hotelName(hotelName).city(city)
                .message("Your booking has been confirmed").build();
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getUserID() {
        return userID;
    }

    public List<Integer> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(List<Integer> roomIds) {
        this.roomIds = roomIds;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(String bookedOn) {
        this.bookedOn = bookedOn;
    }

    public String getConfirmedOn() {
        return confirmedOn;
    }

    public void setConfirmedOn(String confirmedOn) {
        this.confirmedOn = confirmedOn;
    }

    @Override
    public String toString() {
        String builder = "Booking{ID=" + bookingId +
                ", locationID=" + locationId +
                ", userID=" + userID +
                ", roomID=" + roomIds +
                ", booked date=" + bookedOn +
                ", booking date=" + date +
                ", confirmation date=" + confirmedOn +
                ", status=" + ticketStatus +
                "}";
        return builder;
    }
}
