package com.sdmorales.kalah;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

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

}
