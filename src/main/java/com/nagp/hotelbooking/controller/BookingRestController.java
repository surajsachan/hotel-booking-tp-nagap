package com.nagp.hotelbooking.controller;

import com.nagp.hotelbooking.entity.Booking;
import com.nagp.hotelbooking.model.AddBooking;
import com.nagp.hotelbooking.model.AvailabilityByCity;
import com.nagp.hotelbooking.model.CancelBooking;
import com.nagp.hotelbooking.model.ConfirmBooking;
import com.nagp.hotelbooking.response.AddBookingResponse;
import com.nagp.hotelbooking.response.AvailabilityDTO;
import com.nagp.hotelbooking.response.ConfirmBookingResponse;
import com.nagp.hotelbooking.service.BookingService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("booking")
public class BookingRestController {

    Logger logger = LoggerFactory.getLogger(BookingRestController.class);

    @Autowired
    BookingService bookingService;

    @Value("${server.port}")
    private int port;

    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public AddBookingResponse add(@RequestBody @Valid AddBooking addBooking, @RequestHeader("X-Authenticated-User") String username) throws IllegalArgumentException {
        logger.info("Working from port " + port + " of hotel booking service");
        return bookingService.add(addBooking, username);
    }

    @PostMapping(path = "/confirm", consumes = "application/json", produces = "application/json")
    @HystrixCommand(fallbackMethod = "getConfirmBookingFallbackResponse")
    public ConfirmBookingResponse confirm(@RequestBody @Valid ConfirmBooking confirmBooking, @RequestHeader("X-Authenticated-User") String username) throws IllegalArgumentException {
        logger.info("Working from port " + port + " of hotel booking service");
        return bookingService.confirm(confirmBooking.getBookingId(), username);
    }

    @PostMapping(path = "/cancel", consumes = "application/json", produces = "application/json")
    @HystrixCommand(fallbackMethod = "getCancelBookingFallbackResponse")
    public String cancel(@RequestBody @Valid CancelBooking cancelBooking) throws IllegalArgumentException {
        logger.info("Working from port " + port + " of hotel booking service");
        return bookingService.cancel(cancelBooking.getBookingId());
    }

    @PostMapping(path = "/getByCity", consumes = "application/json", produces = "application/json")
    public List<AvailabilityDTO> getAvailabilityByCity(@RequestBody @Valid AvailabilityByCity availabilityByCity) throws IllegalArgumentException {
        logger.info("Working from port " + port + " of hotel booking service");
        return bookingService.getAvailabilityOnGivenDateInGivenCity(availabilityByCity.getCity(), availabilityByCity.getDate());
    }

    @GetMapping(path = "/id/{id}", produces = "application/json")
    public Booking getHotelDetails(@PathVariable("id") String id) throws IllegalArgumentException {
        logger.info("Working from port " + port + " of hotel booking service");
        return bookingService.findHotelById(id);
    }

    public ConfirmBookingResponse getConfirmBookingFallbackResponse(@RequestBody @Valid ConfirmBooking confirmBooking, @RequestHeader("X-Authenticated-User") String username) {
        logger.info("Inside fallback method of confirm booking ");
        bookingService.cancel(confirmBooking.getBookingId());
        return ConfirmBookingResponse.builder().message("Your request has not been processed.In case the amount has debited,refund will be initiated to your payment source within 2-3 working days.").build();
    }

    public String getCancelBookingFallbackResponse(@RequestBody @Valid CancelBooking cancelBooking) {
        return "Your request cannot be processed. Please try after sometime.";
    }

}
