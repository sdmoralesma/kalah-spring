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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

    private static final int FIRST_BOARD_POSITION = 1;
    private static final int LAST_BOARD_POSITION = 14;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final int KEY_ORIENTATION = 0;
    public static final int REQUIRED_VALUES_FOR_BOARD = 15;

    private final Map<Integer, Integer> map;

    public Board() {
        this.map = new Board(0, 6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0).asMap();
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

    public Board move(int pitId, Orientation orientation) {
        Objects.requireNonNull(orientation);
        validateIfBoardContainsThePitId(pitId, map);
        validateIfPitIdIsNotAKalah(pitId);
        validateNorthPlayerSelectedPitId(pitId, orientation);
        validateSouthPlayerSelectedPitId(pitId, orientation);
        validatePitHasStones(pitId, map);
        validateTurnOfUser(map, orientation);

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
        }

        return new Board(newMap);
    }

    private void validateTurnOfUser(Map<Integer, Integer> map, Orientation orientation) {
        Orientation turn = Orientation.fromInt(map.get(KEY_ORIENTATION));
        if (!turn.equals(orientation)) {
            throw new GameException("It is turn of the " + turn + " player");
        }
    }

    private void validatePitHasStones(int pitId, Map<Integer, Integer> map) {
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

    private void validateIfBoardContainsThePitId(int pitId, Map<Integer, Integer> newMap) {
        if (!newMap.containsKey(pitId)) {
            throw new GameException("Pit id not valid: " + pitId);
        }
    }

    private void validateIfPitIdIsNotAKalah(int pitId) {
        if (pitId == 7 || pitId == 14) {
            throw new GameException("Can not select a Kalah, choose a pit: 1-6 or 8-13");
        }
    }


}
