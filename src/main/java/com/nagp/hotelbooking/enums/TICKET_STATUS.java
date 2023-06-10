package com.nagp.hotelbooking.enums;

public enum TICKET_STATUS {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    CANCELLED_PENDING("CANCELLED_PENDING"),
    CANCELLED_CONFIRMED("CANCELLED_CONFIRMED"),
    REJECTED("REJECTED");
    private final String text;

    TICKET_STATUS(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
