package com.nagp.hotelbooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelBooking {
    @NotNull(message = "bookingId cannot be null")
    @NotEmpty(message = "Please provide the bookingId")
    private String bookingId;
}
