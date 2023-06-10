package com.nagp.hotelbooking.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddBooking {
    @NotNull(message = "locationID cannot be null")
    @NotEmpty(message = "Please provide the locationID")
    private String locationId;
    @NotNull(message = "date cannot be null")
    @NotEmpty(message = "Please provide the date")
    private String date;
    private Integer rooms;
}
