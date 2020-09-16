package com.sdmorales.kalah;

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
        return kalahRepository.save(game);
    }

    @Transactional
    public Game makeMove(Long gameId, Long pitId) {
        Game gameById = kalahRepository.findById(gameId)
            .orElseThrow(IllegalStateException::new);
        //todo: make move
        return gameById;
    }

    @Transactional(readOnly = true)
    public Optional<Game> findByGameId(Long gameId) {
        return kalahRepository.findById(gameId);
    }
}
