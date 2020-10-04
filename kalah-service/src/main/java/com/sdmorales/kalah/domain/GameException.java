package com.sdmorales.kalah.domain;

/**
 * Un-ckecked exception that represents a validation error during a game.
 */
public class GameException extends RuntimeException {

    public GameException(String message) {
        super(message);
    }
}
