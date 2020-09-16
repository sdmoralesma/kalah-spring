package com.sdmorales.kalah;

import java.net.URI;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class KalahController {

    private final KalahService kalahService;

    public KalahController(KalahService kalahService) {
        this.kalahService = kalahService;
    }

    @PostMapping("/games")
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        Game persistedGame = kalahService.createGame(game);
        return ResponseEntity.created(buildUriFor(persistedGame)).body(persistedGame);
    }

    private URI buildUriFor(Game persistedGame) {
        return UriComponentsBuilder.newInstance()
            .path("/" + persistedGame.getId())
            .build()
            .toUri();
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<Game> createGame(@PathVariable("gameId") Long gameId) {
        Optional<Game> byGameId = kalahService.findByGameId(gameId);
        return byGameId.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/games/{gameId}/pits/{pitId}")
    public ResponseEntity<Game> makeMove(@PathVariable("gameId") Long gameId, @PathVariable("pitId") Integer pitId) {
        return ResponseEntity.ok(kalahService.makeMove(gameId, pitId));
    }
}
