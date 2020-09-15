package com.sdmorales.kalah;

import java.util.Objects;
import java.util.StringJoiner;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userA;
    private String userB;

    public Game() {
    }

    public Game(String userA, String userB) {
        this.userA = userA;
        this.userB = userB;
    }

    public Long getId() {
        return id;
    }

    public String getUserA() {
        return userA;
    }

    public String getUserB() {
        return userB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return id.equals(game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Game.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("userA='" + userA + "'")
            .add("userB='" + userB + "'")
            .toString();
    }
}
