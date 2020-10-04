package com.sdmorales.kalah.boundary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

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
        Game gameFixture = GameFixtures.createGameFixture();
        when(kalahService.createGame()).thenReturn(gameFixture);
        ResponseEntity<Game> game = this.kalahController.createGame();

        assertEquals(HttpStatus.CREATED, game.getStatusCode());
        assertEquals(gameFixture, game.getBody());
    }

    @Test
    void verifyModifyGameOk() {
        Game gameFixture = GameFixtures.createGameFixture();
        when(kalahService.updateGame(anyLong(), anyMap())).thenReturn(Optional.of(gameFixture));
        ResponseEntity<Game> game = this.kalahController.modifyGameBoard(1L, Collections.emptyMap());

        assertEquals(HttpStatus.OK, game.getStatusCode());
        assertEquals(gameFixture, game.getBody());
    }

    @Test
    void verifyRetrievesGameOk() {
        Game gameFixture = GameFixtures.createGameFixture();
        when(kalahService.findByGameId(anyLong())).thenReturn(Optional.of(gameFixture));
        ResponseEntity<Game> game = this.kalahController.retrieveGame(1L);

        assertEquals(HttpStatus.OK, game.getStatusCode());
        assertEquals(gameFixture, game.getBody());
    }

    @Test
    void verifyMakeMoveOk() {
        Game gameFixture = GameFixtures.createGameFixture();
        when(kalahService.makeMove(1L, 1)).thenReturn(Optional.of(gameFixture));
        ResponseEntity<Game> game = this.kalahController.makeMove(1L, 1);

        assertEquals(HttpStatus.OK, game.getStatusCode());
        assertEquals(gameFixture, game.getBody());
    }

}