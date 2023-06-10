package com.nagp.hotelbooking.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ConfirmBooking {
    @NotNull(message = "bookingID cannot be null")
    @NotEmpty(message = "Please provide the bookingID")
    private String bookingId;

    private PaymentDetails paymentDetails;
}
