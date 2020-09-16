package com.sdmorales.kalah.domain;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

}