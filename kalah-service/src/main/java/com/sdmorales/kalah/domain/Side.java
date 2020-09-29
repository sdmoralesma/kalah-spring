package com.sdmorales.kalah.domain;

import java.time.temporal.ValueRange;

public enum Side {
    NORTH(1, 14, ValueRange.of(8, 13)),
    SOUTH(0, 7, ValueRange.of(1, 6)),
    NONE(-1, -1, null);//todo: consider to remove

    private final int value;
    private final int kalah;
    private final ValueRange range;

    Side(int value, int kalah, ValueRange range) {
        this.value = value;
        this.kalah = kalah;
        this.range = range;
    }

    public int asInt() {
        return value;
    }

    public int getKalah() {
        return kalah;
    }

    public boolean isValidValue(int value) {
        return range.isValidValue(value);
    }

    public static Side flip(Side side) {
        return switch (side) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case NONE -> throw new IllegalStateException("Can not flip side: " + NONE);
        };
    }

    public static Side fromInt(int sideAsInt) {
        return switch (sideAsInt) {
            case 0 -> SOUTH;
            case 1 -> NORTH;
            default -> throw new IllegalArgumentException("Invalid orientation: " + sideAsInt);
        };
    }

}