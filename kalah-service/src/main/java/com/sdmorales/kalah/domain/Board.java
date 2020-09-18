package com.sdmorales.kalah.domain;

import static java.util.Map.entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Board {

    private static final int FIRST_BOARD_POSITION = 1;
    private static final int LAST_BOARD_POSITION = 14;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Map<Integer, Integer> map;

    public Board() {
        this.map = Collections.unmodifiableMap(Map
            .ofEntries(entry(1, 6), entry(2, 6), entry(3, 6), entry(4, 6), entry(5, 6), entry(6, 6), entry(7, 0),
                entry(8, 6), entry(9, 6), entry(10, 6), entry(11, 6), entry(12, 6), entry(13, 6), entry(14, 0)));
    }

    public Board(Map<Integer, Integer> map) {
        this.map = Collections.unmodifiableMap(map);
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
        validateIfBoardContainsThePitId(pitId, map);
        validateIfPitIdIsNotAKalah(pitId);
        validateNorthPlayerSelectedPitId(pitId, orientation);
        validateSouthPlayerSelectedPitId(pitId, orientation);
        validatePitHasStones(pitId, map);

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

    private void validatePitHasStones(int pitId, Map<Integer, Integer> map) {
        Integer currentStones = map.get(pitId);
        if (currentStones <= 0) {
            throw new IllegalStateException("Pit is empty:" + pitId);
        }
    }

    private void validateSouthPlayerSelectedPitId(int pitId, Orientation orientation) {
        if (pitId <= 6 && orientation == Orientation.SOUTH) {
            throw new IllegalStateException("South player can only select pitId between 8 and 13");
        }
    }

    private void validateNorthPlayerSelectedPitId(int pitId, Orientation orientation) {
        if (pitId >= 7 && orientation == Orientation.NORTH) {
            throw new IllegalStateException("North player can only select pitId between 1 and 6");
        }
    }

    private void validateIfBoardContainsThePitId(int pitId, Map<Integer, Integer> newMap) {
        if (!newMap.containsKey(pitId)) {
            throw new IllegalStateException("Pit id not valid: " + pitId);
        }
    }

    private void validateIfPitIdIsNotAKalah(int pitId) {
        if (pitId == 7 || pitId == 14) {
            throw new IllegalStateException("Can not select a kalah, choose a pit: 1-6 or 8-13");
        }
    }


}
