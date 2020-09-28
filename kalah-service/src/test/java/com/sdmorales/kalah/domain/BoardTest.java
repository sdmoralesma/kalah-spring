package com.sdmorales.kalah.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void moveOneStoneOnPit1ForSouthPlayerOk() {
        Board result = board.move(1);

        assertBoardEquals(new Board(0, 0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0), result);
    }

    @Test
    void moveOneStoneOnPit3ForSouthPlayerOk() {
        Board result = board.move(3);

        assertBoardEquals(new Board(1, 6, 6, 0, 7, 7, 7, 1, 7, 7, 6, 6, 6, 6, 0), result);
    }

    @Test
    void moveOneStoneOnPit8ForNorthPlayerOk() {
        Board result = new Board(1, 6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0).move(8);

        assertBoardEquals(new Board(1, 6, 6, 6, 6, 6, 6, 0, 0, 7, 7, 7, 7, 7, 1), result);
    }

    @Test
    void moveOneStoneOnPit10ForNorthPlayerOk() {
        Board result = new Board(1, 6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0).move(10);

        assertBoardEquals(new Board(0, 7, 7, 6, 6, 6, 6, 0, 6, 6, 0, 7, 7, 7, 1), result);
    }

    @Test
    void verifyThrowsExceptionIfPitDoesNotHaveStones() {
        GameException exception = assertThrows(GameException.class,
            () -> new Board(0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0).move(1));

        assertEquals("Pit is empty: 1", exception.getMessage());
    }

    @Test
    void verifyNorthPlayerCanNotMoveSouthPits() {
        GameException exception = assertThrows(GameException.class,
            () -> new Board(1, 0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0).move(1));

        assertEquals("North player can only select pitId between 8 and 13", exception.getMessage());
    }

    @Test
    void verifySouthPlayerCanNotMoveNorthPits() {
        GameException exception = assertThrows(GameException.class, () -> board.move(10));

        assertEquals("South player can only select pitId between 1 and 6", exception.getMessage());
    }

    @Test
    void verifyPlayersCanNotSelectKalahPitId() {
        GameException exception = assertThrows(GameException.class, () -> board.move(7));

        assertEquals("Can not select a Kalah, choose a pit: 1-6 or 8-13", exception.getMessage());
    }

    @Test
    void verifySouthPlayerCanNotMoveOnInvalidPitId() {
        GameException exception = assertThrows(GameException.class, () -> board.move(100));

        assertEquals("Pit id not valid: 100", exception.getMessage());
    }

    @Test
    void verifyLastStoneInKalahAllowsOneMoreTurnForSouthPlayer() {
        Board firstResult = board.move(1);
        assertBoardEquals(new Board(0, 0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0), firstResult);

        Board secondResult = firstResult.move(2);
        assertBoardEquals(new Board(1, 0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0), secondResult);
    }

    @Test
    void verifyIfPlayerDoesNotHaveMoreStonesTheOtherPlayerCollectsAllRemaining(){
        Board result = new Board(0, 0, 0, 0, 0, 0, 1, 10, 0, 0, 0, 0, 2, 3, 10).move(6);

        assertBoardEquals(new Board(0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 15), result);
        assertEquals(Orientation.NORTH, result.getWinner());
    }

    @Test
    void verifyNorthPlayerWins() {
        Board result = new Board(1, 0, 0, 0, 0, 0, 1, 35, 0, 0, 0, 0, 0, 0, 36);

        assertEquals(Orientation.NORTH, result.getWinner());// todo: fix me
    }

    @Test
    void verifySouthPlayerWins() {
        Board result = new Board(0, 0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 1, 35);

        assertEquals(Orientation.SOUTH, result.getWinner());// todo: fix me
    }

    @Test
    void verifyDraw() {
        Board result = new Board(0, 0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 0, 36);

        assertEquals(Orientation.NONE, result.getWinner());
    }

    @Test
    void verifyGameIsInProgress() {
        Board result = new Board(0, 1, 1, 0, 0, 0, 0, 34, 1, 1, 0, 0, 0, 0, 34).move(1);

        assertBoardEquals(new Board(1, 0, 2, 0, 0, 0, 0, 34, 1, 1, 0, 0, 0, 0, 34), result);
        assertEquals(Status.IN_PROGRESS, result.getStatus());
    }

    @Test
    void verifyGameFinished() {
        Board result = new Board(0, 0, 0, 0, 0, 0, 0, 36, 1, 1, 0, 0, 0, 0, 34);

        assertEquals(Status.FINISHED, result.getStatus());
    }

    @Test
    void verifyCanNotMoveBecauseGameIsFinished() {
        GameException exception = assertThrows(GameException.class,
            () -> new Board(0, 0, 0, 0, 0, 0, 0, 36, 1, 1, 0, 0, 0, 0, 34).move(1));

        assertEquals("Game has finished", exception.getMessage());
    }

    @Test
    void verifyLastStoneInEmptyPitOppositeStonesAreCollectedInKalah() {
        Board result = new Board(0, 1, 0, 0, 0, 0, 1, 10, 0, 1, 0, 0, 4, 0, 10).move(1);

        assertBoardEquals(new Board(1, 0, 0, 0, 0, 0, 1, 15, 0, 1, 0, 0, 0, 0, 10), result);
    }

    private static void assertBoardEquals(Board expected, Board actual) {
        assertEquals(new TreeMap<>(expected.asMap()), new TreeMap<>(actual.asMap()));
    }
}