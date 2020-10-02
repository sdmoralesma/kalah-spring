package com.sdmorales.kalah;

import com.sdmorales.kalah.domain.Board;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KalahService {

    private final KalahRepository kalahRepository;

    public KalahService(KalahRepository kalahRepository) {
        this.kalahRepository = kalahRepository;
    }

    @Transactional
    public Game createGame() {
        UUID userAId = UUID.randomUUID();
        UUID userBId = UUID.randomUUID();
        Board newBoard = new Board();
        Game gameToSave = new Game(userAId.toString(), userBId.toString(), newBoard.asJson());
        gameToSave.setStatus(newBoard.getStatus().asString());
        gameToSave.setWinner(newBoard.getWinner().asString());
        return kalahRepository.save(gameToSave);
    }

    @Transactional
    public Optional<Game> makeMove(Long gameId, Integer pitId) {
        Optional<Game> optionalGame = kalahRepository.findById(gameId);
        if (optionalGame.isEmpty()) {
            return Optional.empty();
        }

        Game game = optionalGame.get();
        Board board = Board.fromJson(game.getBoard());
        Board newBoard = board.move(pitId);
        game.setBoard(newBoard.asJson());
        game.setStatus(newBoard.getStatus().asString());
        game.setWinner(newBoard.getWinner().asString());
        Game savedGame = kalahRepository.save(game);
        return Optional.of(savedGame);
    }

    @Transactional
    public Optional<Game> updateGame(Long gameId, Map<Integer, Integer> boardAsMap) {
        Optional<Game> optionalGame = kalahRepository.findById(gameId);
        if (optionalGame.isEmpty()) {
            return Optional.empty();
        }

        Game game = optionalGame.get();
        Board newBoard = new Board(boardAsMap);
        game.setBoard(newBoard.asJson());
        game.setStatus(newBoard.getStatus().asString());
        game.setWinner(newBoard.getWinner().asString());
        Game savedGame = kalahRepository.save(game);
        return Optional.of(savedGame);
    }

    @Transactional(readOnly = true)
    public Optional<Game> findByGameId(Long gameId) {
        return kalahRepository.findById(gameId);
    }
}
