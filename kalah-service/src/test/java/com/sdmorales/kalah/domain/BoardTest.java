package com.sdmorales.kalah.domain;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void moveOneStoneOnPit1ForNorthPlayerOk() {
        Board result = board.move(1, Orientation.NORTH);

        assertEquals(
            Map.ofEntries(entry(1, 0), entry(2, 7), entry(3, 7), entry(4, 7), entry(5, 7), entry(6, 7), entry(7, 1),
                entry(8, 6), entry(9, 6), entry(10, 6), entry(11, 6), entry(12, 6), entry(13, 6), entry(14, 0)),
            result.asMap());
    }

    @Test
    void moveOneStoneOnPit3ForNorthPlayerOk() {
        Board result = board.move(3, Orientation.NORTH);

        assertEquals(
            Map.ofEntries(entry(1, 6), entry(2, 6), entry(3, 0), entry(4, 7), entry(5, 7), entry(6, 7), entry(7, 1),
                entry(8, 7), entry(9, 7), entry(10, 6), entry(11, 6), entry(12, 6), entry(13, 6), entry(14, 0)),
            result.asMap());
    }

    @Test
    void moveOneStoneOnPit8ForSouthPlayerOk() {
        Board result = board.move(8, Orientation.SOUTH);

        assertEquals(
            Map.ofEntries(entry(1, 6), entry(2, 6), entry(3, 6), entry(4, 6), entry(5, 6), entry(6, 6), entry(7, 0),
                entry(8, 0), entry(9, 7), entry(10, 7), entry(11, 7), entry(12, 7), entry(13, 7), entry(14, 1)),
            result.asMap());
    }

    @Test
    void moveOneStoneOnPit10ForSouthPlayerOk() {
        Board result = board.move(10, Orientation.SOUTH);

        assertEquals(
            Map.ofEntries(entry(1, 7), entry(2, 7), entry(3, 6), entry(4, 6), entry(5, 6), entry(6, 6), entry(7, 0),
                entry(8, 6), entry(9, 6), entry(10, 0), entry(11, 7), entry(12, 7), entry(13, 7), entry(14, 1)),
            result.asMap());
    }

    @Test
    void verifyPitDoesNotHaveStones() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> new Board(createEmptyBoard()).move(1, Orientation.NORTH));

        assertTrue(exception.getMessage().contains("Pit is empty"));
    }

    @Test
    void verifyNorthPlayerCanNotMoveSouthPits() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> board.move(10, Orientation.NORTH));

        assertTrue(exception.getMessage().contains("North player"));
    }

    @Test
    void verifySouthPlayerCanNotMoveSouthPits() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> board.move(1, Orientation.SOUTH));

        assertTrue(exception.getMessage().contains("South player"));
    }

    @Test
    void verifyPlayersCanNotSelectKalahPitId() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> board.move(7, Orientation.NORTH));

        assertTrue(exception.getMessage().contains("Can not select a kalah"));
    }

    @Test
    void verifyPlayersOnlyMoveInValidPitIds() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> board.move(0, Orientation.SOUTH));

        assertTrue(exception.getMessage().contains("Pit id not valid"));
    }

    private Map<Integer, Integer> createEmptyBoard() {
        return Map.ofEntries(entry(1, 0), entry(2, 0), entry(3, 0), entry(4, 0), entry(5, 0), entry(6, 0), entry(7, 0),
            entry(8, 0), entry(9, 0), entry(10, 0), entry(11, 0), entry(12, 0), entry(13, 0), entry(14, 0));
    }

}