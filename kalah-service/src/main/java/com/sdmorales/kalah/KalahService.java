package com.sdmorales.kalah;

import com.sdmorales.kalah.domain.Board;
import com.sdmorales.kalah.domain.Orientation;
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
        String turn = Orientation.SOUTH.toString();// Could be randomized
        Game gameToSave = new Game(userAId.toString(), userBId.toString(), new Board().asJson(), turn);
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
        Orientation turn = Orientation.fromString(game.getTurn());
        Board newBoard = board.move(pitId, turn);
        game.setBoard(newBoard.asJson());
        game.setTurn(Orientation.flip(turn).toString());
        return Optional.of(game);
    }

    @Transactional(readOnly = true)
    public Optional<Game> findByGameId(Long gameId) {
        return kalahRepository.findById(gameId);
    }
}
