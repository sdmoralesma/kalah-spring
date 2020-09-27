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

    public static Orientation fromInt(int orientation) {
        return switch (orientation) {
            case 0 -> SOUTH;
            case 1 -> NORTH;
            default -> throw new IllegalArgumentException("Invalid orientation: " + orientation);
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