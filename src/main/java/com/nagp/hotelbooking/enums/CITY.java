package com.nagp.hotelbooking.enums;

public enum CITY {
    GOA("GOA"),
    DL("NEW DELHI"),
    MUM("MUMBAI"),
    BGL("BANGALORE"),
    KL("KOLKATA");
    private final String text;

    CITY(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}