package com.nagp.hotelbooking.service;

import com.nagp.hotelbooking.dao.HotelBookingDao;
import com.nagp.hotelbooking.entity.Booking;
import com.nagp.hotelbooking.entity.Location;
import com.nagp.hotelbooking.enums.TICKET_STATUS;
import com.nagp.hotelbooking.model.AddBooking;
import com.nagp.hotelbooking.repository.BookingRepository;
import com.nagp.hotelbooking.repository.LocationRepository;
import com.nagp.hotelbooking.response.AddBookingResponse;
import com.nagp.hotelbooking.response.AvailabilityDTO;
import com.nagp.hotelbooking.response.ConfirmBookingResponse;
import com.nagp.hotelbooking.service.validation.HotelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private HotelValidator validator;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private HotelBookingDao hotelBookingDao;

    @Autowired
    private PaymentService paymentService;

    @Value("${app.room.MAX_ROOMS_BOOKING_ALLOWED: 4}")
    private int MAX_ROOMS_BOOKING_ALLOWED;

    private List<Integer> getNextAvailableRoomNumber(String locationId, String date, int capacity, int roomsRequested) {
        Set<Integer> roomsTaken = bookingRepository.nextRoomID(locationId, date).stream().collect(Collectors.toSet());
        Set<Integer> availableRooms = IntStream.range(1, capacity + 1).boxed().collect(Collectors.toSet());
        availableRooms.removeAll(roomsTaken);
        List<Integer> roomsAllocated = availableRooms.stream().limit(roomsRequested).collect(Collectors.toList());
        if (roomsAllocated.size() < roomsRequested)
            throw new RuntimeException("Enough Rooms not available on the date provided.");
        return roomsAllocated;
    }


    public AddBookingResponse add(AddBooking addBooking, String username) {
        if (addBooking.getRooms() <= 0 || addBooking.getRooms() > MAX_ROOMS_BOOKING_ALLOWED)
            throw new IllegalArgumentException("Requested room count cannot be serviced");
        validator.validDateForBooking(addBooking.getDate());
        Location location = locationRepository.findById(addBooking.getLocationId()).orElseThrow();
        Booking booking = new Booking(addBooking.getLocationId(), username, addBooking.getDate());
        List<Integer> roomIds = getNextAvailableRoomNumber(location.getLocationId(), addBooking.getDate(), location.getCapacity(), addBooking.getRooms());
        booking.setRoomIds(roomIds);
        booking = bookingRepository.save(booking);
        notificationService.notifyReserveBooking(username, location.getPrice() * addBooking.getRooms());
        String hotelName = locationRepository.findHotelNameFromLocationId(location.getLocationId());
        return Booking.getPendingBookingMessage(booking, location.getPrice(), hotelName, location.getCity());
    }

    public List<AvailabilityDTO> getAvailabilityOnGivenDateInGivenCity(String city, String date) {
        validator.validDateForBooking(date);
        return bookingRepository.availabilityList(city.toUpperCase(), date);
    }

    public ConfirmBookingResponse confirm(String bookingId, String username) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        if (!booking.getUserID().equals(username))
            throw new IllegalArgumentException("This booking is reserved by " + booking.getUserID());
        if (booking.getTicketStatus().equals(TICKET_STATUS.PENDING.toString())) {
            Location location = locationRepository.findById(booking.getLocationId()).orElseThrow();
            String hotelName = locationRepository.findHotelNameFromLocationId(location.getLocationId());
            int totalAmount = location.getPrice() * booking.getRoomIds().size();
            Map paymentResponse = paymentService.makePayment(username, hotelName, booking.getRoomIds(), bookingId, totalAmount);
            if (paymentResponse.get("message").toString().equals("Payment processed successfully")) {
                hotelBookingDao.insertToBookingIdTransactioIdMap(bookingId, paymentResponse.get("transactionId").toString());
                notificationService.notifyConfirmBooking(username, hotelName, booking.getRoomIds(), bookingId, totalAmount);
                String confirmedOn = Booking.getFormattedDate(new Date());
                bookingRepository.confirmBooking(bookingId, confirmedOn, TICKET_STATUS.CONFIRMED.toString());
                booking.setConfirmedOn(confirmedOn);
                booking.setTicketStatus(TICKET_STATUS.CONFIRMED.toString());
                return Booking.getConfirmationBookingMessage(booking, location.getPrice(), hotelName, location.getCity());
            } else {
                return ConfirmBookingResponse.builder().bookedFor(booking.getDate()).bookingID(booking.getBookingId()).bookedOn(booking.getBookedOn()).roomIDs(booking.getRoomIds()).status(booking.getTicketStatus()).confirmedOn(booking.getConfirmedOn()).message("BOOKING WITH BOOKING ID CANNOT BE CONFIRMED DUE TO PAYMENT FAILURE").build();
            }
        } else if (booking.getTicketStatus().equals(TICKET_STATUS.CONFIRMED.toString())) {
            return ConfirmBookingResponse.builder().bookedFor(booking.getDate()).bookingID(booking.getBookingId()).bookedOn(booking.getBookedOn()).roomIDs(booking.getRoomIds()).status(booking.getTicketStatus()).confirmedOn(booking.getConfirmedOn()).message("Your booking was already confirmed").build();
        } else {
            return ConfirmBookingResponse.builder().bookedFor(booking.getDate()).bookingID(booking.getBookingId()).bookedOn(booking.getBookedOn()).roomIDs(booking.getRoomIds()).status(booking.getTicketStatus()).confirmedOn(booking.getConfirmedOn()).message("BOOKING WITH BOOKING ID CANNOT BE CONFIRMED. STATUS IS '" + booking.getTicketStatus() + "'").build();
        }
    }

    public Booking findHotelById(String bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow();
    }

    public String cancel(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        if (!(booking.getTicketStatus().equals(TICKET_STATUS.PENDING.toString()) || booking.getTicketStatus().equals(TICKET_STATUS.CONFIRMED.toString()))) {
            return "INVALID STATUS, CANNOT CANCEL BOOKING WITH BOOKING ID " + bookingId;
        } else {
            String date = booking.getDate();
            validator.validateDateForCancellation(date);
            String msg = "BOOKING WITH BOOKING ID " + bookingId +
                    " CANCELLED.";
            if (booking.getTicketStatus().equals(TICKET_STATUS.PENDING.toString()))
                bookingRepository.cancelBooking(bookingId, TICKET_STATUS.CANCELLED_PENDING.toString());
            else {
                Map refundResponse = paymentService.processRefund(bookingId);
                bookingRepository.cancelBooking(bookingId, TICKET_STATUS.CANCELLED_CONFIRMED.toString());
                Location location = locationRepository.findById(booking.getLocationId()).orElseThrow();
                msg += "Rs." + location.getPrice() + " will be refunded.";
            }
            return msg;
        }
    }

    public List<String> getPendingBookingsEligibleForRejection(int delayAllowed) {
        return bookingRepository.getPendingBookingsEligibleForRejection(Booking.getFormattedDate(new Date()), delayAllowed);
    }

    public void updatePendingBookingsEligibleForRejection(List<String> idList) {
        bookingRepository.updatePendingBookingsEligibleForRejection(idList, TICKET_STATUS.REJECTED.toString());
    }
}
