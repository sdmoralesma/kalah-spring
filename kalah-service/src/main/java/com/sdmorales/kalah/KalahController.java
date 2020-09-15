package com.sdmorales.kalah;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KalahController {

    private final KalahService kalahService;

    public KalahController(KalahService kalahService) {
        this.kalahService = kalahService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @PostMapping("/games")
    public ResponseEntity<Game> createGame() {
        Game game = kalahService.createGame();
        return ResponseEntity.ok(game);
    }

    @PutMapping("/games/{gameId}/pits/{pitId}")
    public ResponseEntity<Game> makeMove(@PathVariable("gameId") String gameId, @PathVariable("pitId") String pitId) {
        return ResponseEntity.ok(kalahService.makeMove());
    }
}
