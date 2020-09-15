package com.sdmorales.kalah;

import org.springframework.stereotype.Service;

@Service
public class KalahService {

    public Game createGame() {
        return new Game();
    }

    public Game makeMove() {
        return new Game();
    }

}
