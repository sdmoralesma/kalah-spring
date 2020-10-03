package com.sdmorales.kalah.boundary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

import com.sdmorales.kalah.domain.Board;
import com.sdmorales.kalah.entity.Game;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class KalahControllerTest {

    private KalahController kalahController;

    @Mock
    private KalahService kalahService;

    @BeforeEach
    void setUp() {
        this.kalahController = new KalahController(kalahService);
    }

    @Test
    void verifyCreateGameOk() {
        Game gameFixture = createGameFixture();
        when(kalahService.createGame()).thenReturn(gameFixture);
        ResponseEntity<Game> game = this.kalahController.createGame();

        assertEquals(HttpStatus.CREATED, game.getStatusCode());
        assertEquals(gameFixture, game.getBody());
    }

    @Test
    void verifyModifyGameOk() {
        Game gameFixture = createGameFixture();
        when(kalahService.updateGame(anyLong(), anyMap())).thenReturn(Optional.of(gameFixture));
        ResponseEntity<Game> game = this.kalahController.modifyGameBoard(1L, Collections.emptyMap());

        assertEquals(HttpStatus.OK, game.getStatusCode());
        assertEquals(gameFixture, game.getBody());
    }

    @Test
    void verifyRetrievesGameOk() {
        Game gameFixture = createGameFixture();
        when(kalahService.findByGameId(anyLong())).thenReturn(Optional.of(gameFixture));
        ResponseEntity<Game> game = this.kalahController.retrieveGame(1L);

        assertEquals(HttpStatus.OK, game.getStatusCode());
        assertEquals(gameFixture, game.getBody());
    }

    @Test
    void verifyMakeMoveOk() {
        Game gameFixture = createGameFixture();
        when(kalahService.makeMove(1L, 1)).thenReturn(Optional.of(gameFixture));
        ResponseEntity<Game> game = this.kalahController.makeMove(1L, 1);

        assertEquals(HttpStatus.OK, game.getStatusCode());
        assertEquals(gameFixture, game.getBody());
    }

    private Game createGameFixture() {
        Integer[] arrayInts = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        return new Game("userA", "userB", new Board(arrayInts).asJson());
    }

}