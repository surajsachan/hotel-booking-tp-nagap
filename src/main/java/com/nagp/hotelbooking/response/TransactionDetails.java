package com.nagp.hotelbooking.response;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class TransactionDetails {
    @NotNull
    @NotEmpty
    private String transactionId;

}