package com.nagp.hotelbooking.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AvailabilityByCity {
    @NotNull(message = "city cannot be null")
    @NotEmpty(message = "Please provide the city")
    private String city;
    @NotNull(message = "date cannot be null")
    @NotEmpty(message = "Please provide the date")
    private String date;
}
