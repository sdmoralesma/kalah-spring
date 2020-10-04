package com.sdmorales.kalah.domain;

import java.time.temporal.ValueRange;

/**
 * Represents the two players of a kalah game, where each player is at one side opposite to the other. There are two
 * sides in a Kalah game: North and South.
 */
public enum Side {
    /**
     * The North side player
     */
    NORTH(1, 14, ValueRange.of(8, 13), "NORTH"),
    /**
     * The South side player
     */
    SOUTH(0, 7, ValueRange.of(1, 6), "SOUTH"),
    /**
     * In case there is a Draw
     */
    NONE(-1, -1, null, "NONE");

    private final int value;
    private final int kalah;
    private final ValueRange range;
    private final String asString;

    Side(int value, int kalah, ValueRange range, String asString) {
        this.value = value;
        this.kalah = kalah;
        this.range = range;
        this.asString = asString;
    }

    public int asInt() {
        return value;
    }

    public String asString() {
        return this.asString;
    }

    public int getKalah() {
        return kalah;
    }

    /**
     * Validates if the pit belong to the side.
     *
     * @param pitId the id of the pit to evaluate
     * @return true if the id is in the range of the {@link Side}, false otherwise
     */
    public boolean isPitIdInRange(int pitId) {
        return range.isValidValue(pitId);
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