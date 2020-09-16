package com.sdmorales.kalah;

import com.sdmorales.kalah.domain.Board;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KalahService {

    private final KalahRepository kalahRepository;

    public KalahService(KalahRepository kalahRepository) {
        this.kalahRepository = kalahRepository;
    }

    @Transactional
    public Game createGame(Game game) {
        Game gameToSave = new Game(game.getUserA(), game.getUserB(), new Board().asJson());
        return kalahRepository.save(gameToSave);
    }

    @Transactional
    public Game makeMove(Long gameId, Long pitId) {
        Game game = kalahRepository.findById(gameId).orElseThrow(IllegalStateException::new);
        //todo: connect me
        return game;
    }

    @Transactional(readOnly = true)
    public Optional<Game> findByGameId(Long gameId) {
        return kalahRepository.findById(gameId);
    }
}
