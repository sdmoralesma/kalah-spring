package com.sdmorales.kalah.domain;

import static java.util.Map.entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

    public static final int KEY_ORIENTATION = 0;

    private static final int FIRST_BOARD_POSITION = 1;
    private static final int LAST_BOARD_POSITION = 14;
    private static final int KALAH_NORTH_PLAYER = LAST_BOARD_POSITION;
    private static final int KALAH_SOUTH_PLAYER = 7;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int REQUIRED_VALUES_FOR_BOARD = 15;

    private final Map<Integer, Integer> map;

    public Board() {
        this.map = new Board(Orientation.SOUTH.asInt(), 6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0).asMap();
    }

    public Board(Map<Integer, Integer> map) {
        this.map = Collections.unmodifiableMap(map);
    }

    public Board(Integer... values) {
        if (values.length != REQUIRED_VALUES_FOR_BOARD) {
            throw new IllegalArgumentException("Requires " + REQUIRED_VALUES_FOR_BOARD + " values to create a board");
        }
        this.map = fromListToMap(Arrays.asList(values));
    }

    public Orientation getOrientation() {
        return Orientation.fromInt(map.get(Board.KEY_ORIENTATION));
    }

    public Status getStatus() {
        if (northPlayerHaveStones(map) && southPlayerHaveStones(map)) {
            return Status.IN_PROGRESS;
        } else {
            return Status.FINISHED;
        }
    }

    public Orientation getWinner() {
        if (getStatus() != Status.FINISHED) {
            throw new IllegalStateException("Game has not finished");
        }

        int northPlayerTotalStones = map.get(KALAH_NORTH_PLAYER);
        int southPlayerTotalStones = map.get(KALAH_SOUTH_PLAYER);

        if (northPlayerTotalStones > southPlayerTotalStones) {
            return Orientation.NORTH;
        } else if (northPlayerTotalStones < southPlayerTotalStones) {
            return Orientation.SOUTH;
        } else {
            return Orientation.NONE;
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
        Orientation orientation = getOrientation();
        validateGameStatusInProgress();
        validateIfBoardContainsThePitId(pitId);
        validateIfPitIdIsNotAKalah(pitId);
        validateNorthPlayerSelectedPitId(pitId, orientation);
        validateSouthPlayerSelectedPitId(pitId, orientation);
        validatePitHasStones(pitId);

        Map<Integer, Integer> newMap = new HashMap<>(map);
        Integer currentStones = newMap.get(pitId);
        newMap.put(pitId, 0);
        while (currentStones > 0) {
            pitId++;
            if (pitId > LAST_BOARD_POSITION) {
                pitId = FIRST_BOARD_POSITION;
            }

            Integer value = newMap.get(pitId) + 1;
            newMap.put(pitId, value);
            currentStones--;

            if (currentStones == 0) {
                if (pitId == KALAH_NORTH_PLAYER) {
                    newMap.put(KEY_ORIENTATION, Orientation.NORTH.asInt());
                } else if (pitId == KALAH_SOUTH_PLAYER) {
                    newMap.put(KEY_ORIENTATION, Orientation.SOUTH.asInt());
                } else {
                    Integer currentOrientation = map.get(KEY_ORIENTATION);
                    newMap.put(KEY_ORIENTATION, Orientation.flip(currentOrientation).asInt());
                }
            }
        }

        return calculateEndGame(newMap);
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
            .filter(e -> e.getKey() >= 8 && e.getKey() <= 13)
            .anyMatch(e -> e.getValue() >= 1);
    }

    private boolean southPlayerHaveStones(Map<Integer, Integer> map) {
        return map.entrySet().stream()
            .filter(e -> e.getKey() >= 1 && e.getKey() <= 6)
            .anyMatch(e -> e.getValue() >= 1);
    }

    private void validatePitHasStones(int pitId) {
        Integer currentStones = map.get(pitId);
        if (currentStones <= 0) {
            throw new GameException("Pit is empty: " + pitId);
        }
    }

    private void validateSouthPlayerSelectedPitId(int pitId, Orientation orientation) {
        if (pitId >= 7 && orientation == Orientation.SOUTH) {
            throw new GameException("South player can only select pitId between 1 and 6");
        }
    }

    private void validateNorthPlayerSelectedPitId(int pitId, Orientation orientation) {
        if (pitId <= 6 && orientation == Orientation.NORTH) {
            throw new GameException("North player can only select pitId between 8 and 13");
        }
    }

    private void validateIfBoardContainsThePitId(int pitId) {
        if (!map.containsKey(pitId)) {
            throw new GameException("Pit id not valid: " + pitId);
        }
    }

    private void validateIfPitIdIsNotAKalah(int pitId) {
        if (pitId == 7 || pitId == 14) {
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
