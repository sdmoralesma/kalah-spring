package com.sdmorales.kalah.domain;

public enum Orientation {
    NORTH(1),
    SOUTH(0);

    private final int value;

    Orientation(int value) {
        this.value = value;
    }

    public int asInt() {
        return value;
    }

    public static Orientation flip(int orientation) {
        return switch (Orientation.fromInt(orientation)) {
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

}