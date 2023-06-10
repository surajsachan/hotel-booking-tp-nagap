package com.nagp.hotelbooking.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBookingResponse {
    private List<Integer> roomIDs;
    private String bookingID;
    private Integer amount;
    private String bookedOn;
    private String bookedFor;
    private String status;
    private String message;
}
