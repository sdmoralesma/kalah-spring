package com.sdmorales.kalah.domain;

import static java.util.Map.entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A {@link Board} is modeled by a map with 16 entries. The first entry (i.e: 0) represents the orientation of the game,
 * the other position from 1 to 15 represent pits where 7 and 14 are special because those are the kalah for each side.
 */
public class Board {

    public static final int KEY_ORIENTATION = 0;

    private static final int FIRST_BOARD_POSITION = 1;
    private static final int LAST_BOARD_POSITION = 14;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int REQUIRED_VALUES_FOR_BOARD = 15;

    private final Map<Integer, Integer> map;

    public Board() {
        this.map = new Board(Side.SOUTH.asInt(), 6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0).asMap();
    }

    public Board(Map<Integer, Integer> map) {
        Objects.requireNonNull(map);
        boolean anyValueIsNegative = map.values().stream().mapToInt(i -> i).anyMatch(value -> value < 0);
        if (anyValueIsNegative) {
            throw new IllegalStateException("Only values >= 0 are allowed in a board");
        }
        if (map.size() != REQUIRED_VALUES_FOR_BOARD) {
            throw new IllegalArgumentException("Requires " + REQUIRED_VALUES_FOR_BOARD + " entries to create a board");
        }
        this.map = Collections.unmodifiableMap(map);
    }

    public Board(int zero, int one, int two, int three, int four, int five, int six, int seven, int eight, int nine,
        int ten, int eleven, int twelve, int thirteen, int fourteen) {
        List<Integer> values = List.of(zero, one, two, three, four, five, six, seven, eight, nine,
            ten, eleven, twelve, thirteen, fourteen);
        if (values.stream().anyMatch(i -> i < 0)) {
            throw new IllegalStateException("Only values >= 0 are allowed in a board");
        }

        this.map = fromListToMap(values);
    }

    public Side getOrientation() {
        return Side.fromInt(map.get(Board.KEY_ORIENTATION));
    }

    public Status getStatus() {
        if (northPlayerHaveStones(map) && southPlayerHaveStones(map)) {
            return Status.IN_PROGRESS;
        } else {
            return Status.FINISHED;
        }
    }

    public Side getWinner() {
        int northPlayerTotalStones = map.get(Side.NORTH.getKalah());
        int southPlayerTotalStones = map.get(Side.SOUTH.getKalah());

        if (northPlayerTotalStones > southPlayerTotalStones) {
            return Side.NORTH;
        } else if (northPlayerTotalStones < southPlayerTotalStones) {
            return Side.SOUTH;
        } else {
            return Side.NONE;
        }
    }

    private Map<Integer, Integer> fromListToMap(List<Integer> list) {
        return IntStream.rangeClosed(KEY_ORIENTATION, LAST_BOARD_POSITION)
            .boxed()
            .map(i -> entry(i, list.get(i)))
            .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
    }

    public Map<Integer, Integer> asMap() {
        return map;
    }

    public String asJson() {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Can not convert board as json: " + map);
        }
    }

    public static Board fromJson(String json) {
        try {
            Map<Integer, Integer> map = OBJECT_MAPPER.readValue(json, new TypeReference<>() {
            });
            return new Board(map);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Can not convert json as map: " + json);
        }
    }

    public Board move(int pitId) {
        Side currentSide = getOrientation();
        validateGameStatusInProgress();
        validateIfBoardContainsThePitId(pitId);
        validateThatPitIdIsNotKalah(pitId);
        validateOrientationAndSelectedPitId(pitId, currentSide);
        validatePitHasStones(pitId);

        Map<Integer, Integer> newMap = new HashMap<>(map);
        Integer currentStones = newMap.get(pitId);
        newMap.put(pitId, 0);
        while (currentStones > 0) {
            pitId++;
            if (pitId > LAST_BOARD_POSITION) {
                pitId = FIRST_BOARD_POSITION;
            }

            if (currentPitIsOpponentsKalah(pitId, currentSide)) {
                continue;
            }

            int newValue = newMap.get(pitId) + 1;
            newMap.put(pitId, newValue);
            currentStones--;

            if (lastStoneEndsInKalah(pitId, currentStones)) {
                newMap.put(KEY_ORIENTATION, currentSide.asInt());// the player has one more move
                break;
            }

            if (lastStoneEndsInOwnEmptyPit(pitId, newMap, currentStones, newValue, currentSide)) {
                int stonesToCollect = newMap.get(determineOppositePitId(pitId));//then steal stones from opponents pit
                newMap.put(pitId, 0);
                newMap.put(determineOppositePitId(pitId), 0);
                newMap.computeIfPresent(currentSide.getKalah(), (k, v) -> v + stonesToCollect + 1);
            }

            if (currentStones == 0) {
                newMap.put(KEY_ORIENTATION, Side.flip(currentSide).asInt());
            }


        }

        return calculateEndGame(newMap);
    }

    private boolean lastStoneEndsInOwnEmptyPit(int pitId, Map<Integer, Integer> newMap, Integer currentStones,
        int newValue, Side currentSide) {
        return currentStones == 0
            && newValue == 1 // the pit was previously empty and now has 1 stone
            && currentSide.isPitIdInRange(pitId) // pit id belongs to current player
            && (Side.NORTH.getKalah() != pitId && Side.SOUTH.getKalah() != pitId)// pit is not a kalah
            && numberOfStonesInOppositePit(pitId, newMap) > 0; // check if the opposite pit has stones
    }

    private boolean lastStoneEndsInKalah(int pitId, Integer currentStones) {
        return currentStones == 0 && (pitId == Side.NORTH.getKalah() || pitId == Side.SOUTH.getKalah());
    }

    private boolean currentPitIsOpponentsKalah(int pitId, Side currentSide) {
        return (currentSide == Side.NORTH && pitId == Side.SOUTH.getKalah())
            || (currentSide == Side.SOUTH && pitId == Side.NORTH.getKalah());
    }

    private Integer numberOfStonesInOppositePit(int pitId, Map<Integer, Integer> newMap) {
        return newMap.get(determineOppositePitId(pitId));
    }

    private int determineOppositePitId(int pitId) {
        return 14 - pitId;
    }

    private Board calculateEndGame(Map<Integer, Integer> aMap) {
        if (new Board(aMap).getStatus() == Status.FINISHED) {
            int northPlayerTotalStones = aMap.entrySet().stream()
                .filter(e -> e.getKey() >= 8 && e.getKey() <= 14)
                .mapToInt(Entry::getValue)
                .sum();

            int southPlayerTotalStones = aMap.entrySet().stream()
                .filter(e -> e.getKey() >= 1 && e.getKey() <= 7)
                .mapToInt(Entry::getValue)
                .sum();

            return new Board(aMap.get(KEY_ORIENTATION), 0, 0, 0, 0, 0, 0, southPlayerTotalStones,
                0, 0, 0, 0, 0, 0, northPlayerTotalStones);
        }

        return new Board(aMap);
    }

    private void validateGameStatusInProgress() {
        if (getStatus() == Status.FINISHED) {
            throw new GameException("Game has finished");
        }
    }

    private boolean northPlayerHaveStones(Map<Integer, Integer> map) {
        return map.entrySet().stream()
            .filter(e -> Side.NORTH.isPitIdInRange(e.getKey()))
            .anyMatch(e -> e.getValue() >= 1);
    }

    private boolean southPlayerHaveStones(Map<Integer, Integer> map) {
        return map.entrySet().stream()
            .filter(e -> Side.SOUTH.isPitIdInRange(e.getKey()))
            .anyMatch(e -> e.getValue() >= 1);
    }

    private void validatePitHasStones(int pitId) {
        Integer currentStones = map.get(pitId);
        if (currentStones <= 0) {
            throw new GameException("Pit id is empty: " + pitId);
        }
    }

    private void validateOrientationAndSelectedPitId(int pitId, Side side) {
        if (!side.isPitIdInRange(pitId)) {
            throw new GameException(side.toString() + " player can not select pit id: " + pitId);
        }
    }

    private void validateIfBoardContainsThePitId(int pitId) {
        if (!map.containsKey(pitId)) {
            throw new GameException("Pit id is not valid: " + pitId);
        }
    }

    private void validateThatPitIdIsNotKalah(int pitId) {
        if (pitId == Side.SOUTH.getKalah() || pitId == Side.NORTH.getKalah()) {
            throw new GameException("Can not select a Kalah, choose a pit: 1-6 or 8-13");
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Board.class.getSimpleName() + "[", "]")
            .add("map=" + new TreeMap<>(map))
            .toString();
    }
}
