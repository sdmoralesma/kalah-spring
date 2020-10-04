package com.sdmorales.kalah.boundary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.sdmorales.kalah.control.KalahRepository;
import com.sdmorales.kalah.domain.Board;
import com.sdmorales.kalah.entity.Game;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KalahServiceTest {

    private KalahService kalahService;

    @Mock
    private KalahRepository kalahRepository;

    @BeforeEach
    void setUp() {
        kalahService = new KalahService(kalahRepository);
    }

    @Test
    void createGame() {
        Game gameFixture = new Game(1L, "aaa", "bbb", new Board().asJson());
        when(kalahRepository.save(any(Game.class))).thenReturn(gameFixture);

        Game game = kalahService.createGame();

        assertEquals(gameFixture.getBoard(), game.getBoard());
        assertEquals(gameFixture.getUserA(), game.getUserA());
        assertEquals(gameFixture.getUserB(), game.getUserB());
    }

    @Test
    void verifyMakeMoveOk() {
        Game gameFixture = new Game(1L, "aaa", "bbb", new Board().asJson());
        when(kalahRepository.findById(eq(1L))).thenReturn(Optional.of(gameFixture));
        when(kalahRepository.save(any(Game.class))).thenReturn(gameFixture);

        Game game = kalahService.makeMove(1L, 1).orElseThrow(IllegalStateException::new);

        assertEquals(gameFixture.getBoard(), game.getBoard());
        assertEquals(gameFixture.getUserA(), game.getUserA());
        assertEquals(gameFixture.getUserB(), game.getUserB());
    }

    @Test
    void makeMoveReturnsEmpty() {
        when(kalahRepository.findById(eq(1L))).thenReturn(Optional.empty());

        Optional<Game> optionalGame = kalahService.makeMove(1L, 1);

        assertTrue(optionalGame.isEmpty());
    }

    @Test
    void updateGame() {
        Game gameFixture = new Game(1L, "aaa", "bbb", new Board().asJson());
        Board board = new Board(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        when(kalahRepository.findById(eq(1L))).thenReturn(Optional.of(gameFixture));
        when(kalahRepository.save(any(Game.class))).thenReturn(gameFixture);

        Game game = kalahService.updateGame(1L, board.asMap()).orElseThrow(IllegalStateException::new);

        assertEquals(gameFixture.getBoard(), game.getBoard());
        assertEquals(gameFixture.getUserA(), game.getUserA());
        assertEquals(gameFixture.getUserB(), game.getUserB());
    }

    @Test
    void updateGameReturnsEmpty() {
        when(kalahRepository.findById(eq(1L))).thenReturn(Optional.empty());

        Optional<Game> optionalGame = kalahService.updateGame(1L, Collections.emptyMap());

        assertTrue(optionalGame.isEmpty());
    }

    @Test
    void findByGameIdOk() {
        Game gameFixture = new Game(1L, "aaa", "bbb", new Board().asJson());
        when(kalahRepository.findById(eq(1L))).thenReturn(Optional.of(gameFixture));

        Optional<Game> optionalGame = kalahService.findByGameId(1L);

        assertTrue(optionalGame.isPresent());
    }

    @Test
    void findByGameIdReturnsEmpty() {
        Optional<Game> optionalGame = kalahService.findByGameId(1L);

        assertTrue(optionalGame.isEmpty());
    }
}