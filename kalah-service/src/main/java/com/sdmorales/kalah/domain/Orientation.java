package com.sdmorales.kalah.domain;

import java.util.Objects;

public enum Orientation {
    NORTH,
    SOUTH;

    public static Orientation flip(Orientation orientation) {
        return switch (orientation) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
        };
    }

    public static Orientation fromString(String orientation) {
        Objects.requireNonNull(orientation);
        if (NORTH.toString().equalsIgnoreCase(orientation)) {
            return Orientation.NORTH;
        } else if (SOUTH.toString().equalsIgnoreCase(orientation)) {
            return Orientation.SOUTH;
        } else {
            throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }
}