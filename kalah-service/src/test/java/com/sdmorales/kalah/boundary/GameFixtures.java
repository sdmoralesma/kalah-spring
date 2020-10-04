package com.sdmorales.kalah.boundary;

import com.sdmorales.kalah.domain.Board;
import com.sdmorales.kalah.entity.Game;

public class GameFixtures {

    public static Game createGameFixture() {
        return new Game("userA", "userB", new Board(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15).asJson());
    }

}
