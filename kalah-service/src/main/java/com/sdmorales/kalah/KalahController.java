package com.sdmorales.kalah;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KalahController {

    private final KalahService kalahService;

    public KalahController(KalahService kalahService) {
        this.kalahService = kalahService;
    }

    @PostMapping("/games")
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        Game gameToSave = new Game(game.getUserA(), game.getUserB());
        return ResponseEntity.ok(kalahService.createGame(gameToSave));
    }

    @PutMapping("/games/{gameId}/pits/{pitId}")
    public ResponseEntity<Game> makeMove(@PathVariable("gameId") Long gameId, @PathVariable("pitId") Long pitId) {
        return ResponseEntity.ok(kalahService.makeMove(gameId, pitId));
    }
}
