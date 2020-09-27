package com.sdmorales.kalah.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void moveOneStoneOnPit1ForSouthPlayerOk() {
        Board result = board.move(1, Orientation.SOUTH);

        assertBoardEquals(new Board(0, 0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0), result);
    }

    @Test
    void moveOneStoneOnPit3ForSouthPlayerOk() {
        Board result = board.move(3, Orientation.SOUTH);

        assertBoardEquals(new Board(0, 6, 6, 0, 7, 7, 7, 1, 7, 7, 6, 6, 6, 6, 0), result);
    }

    @Test
    void moveOneStoneOnPit8ForNorthPlayerOk() {
        Board result = new Board(1, 6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0).move(8, Orientation.NORTH);

        assertBoardEquals(new Board(1, 6, 6, 6, 6, 6, 6, 0, 0, 7, 7, 7, 7, 7, 1), result);
    }

    @Test
    void moveOneStoneOnPit10ForNorthPlayerOk() {
        Board result = new Board(1, 6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0).move(10, Orientation.NORTH);

        assertBoardEquals(new Board(1, 7, 7, 6, 6, 6, 6, 0, 6, 6, 0, 7, 7, 7, 1), result);
    }

    @Test
    void verifyThrowsExceptionIfPitDoesNotHaveStones() {
        GameException exception = assertThrows(GameException.class,
            () -> new Board(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0).move(1, Orientation.SOUTH));

        assertTrue(exception.getMessage().contains("Pit is empty"));
    }

    @Test
    void verifyNorthPlayerCanNotMoveSouthPits() {
        GameException exception = assertThrows(GameException.class, () -> board.move(1, Orientation.NORTH));

        assertEquals("North player can only select pitId between 8 and 13", exception.getMessage());
    }

    @Test
    void verifySouthPlayerCanNotMoveNorthPits() {
        GameException exception = assertThrows(GameException.class, () -> board.move(10, Orientation.SOUTH));

        assertEquals("South player can only select pitId between 1 and 6", exception.getMessage());
    }

    @Test
    void verifyPlayersCanNotSelectKalahPitId() {
        GameException exception = assertThrows(GameException.class,
            () -> board.move(7, Orientation.SOUTH));

        assertEquals("Can not select a Kalah, choose a pit: 1-6 or 8-13", exception.getMessage());
    }

    @Test
    void verifySouthPlayerCanNotMoveOnInvalidPitId() {
        GameException exception = assertThrows(GameException.class, () -> board.move(100, Orientation.SOUTH));

        assertEquals("Pit id not valid: 100", exception.getMessage());
    }

    @Test
    void verifyNorthPlayerCanNotMoveWhenIsNotHisTurn() {
        GameException exception = assertThrows(GameException.class, () -> board.move(10, Orientation.NORTH));

        assertEquals("It is turn of the SOUTH player", exception.getMessage());
    }

    @Test
    @Disabled
    void verifyLastStoneInKalahAllowsNewTurnForSouthPlayer() {
        Board firstResult = board.move(1, Orientation.SOUTH);
        assertBoardEquals(new Board(0, 0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0), firstResult);

        Board secondResult = board.move(1, Orientation.SOUTH);
        assertBoardEquals(new Board(0, 0, 0, 8, 8, 8, 8, 8, 7, 7, 6, 6, 6, 6, 0), secondResult);
    }

    private static void assertBoardEquals(Board expected, Board actual) {
        assertEquals(new TreeMap<>(expected.asMap()), new TreeMap<>(actual.asMap()));
    }
}