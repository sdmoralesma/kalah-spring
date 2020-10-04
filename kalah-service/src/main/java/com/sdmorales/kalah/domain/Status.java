package com.sdmorales.kalah.domain;

/**
 * The {@link Status} of a kalah game.
 */
public enum Status {

    /**
     * The game has started and players can make moves.
     */
    IN_PROGRESS("IN_PROGRESS"),

    /**
     * The game has finished. Players can not make more moves.
     */
    FINISHED("FINISHED");

    private final String string;

    Status(String string) {
        this.string = string;
    }

    public String asString() {
        return this.string;
    }
}
