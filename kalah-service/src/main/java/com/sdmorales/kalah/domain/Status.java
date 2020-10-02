package com.sdmorales.kalah.domain;

public enum Status {

    IN_PROGRESS("IN_PROGRESS"),
    FINISHED("FINISHED");

    private final String string;

    Status(String string) {
        this.string = string;
    }

    public String asString() {
        return this.string;
    }
}
