package com.sdmorales.kalah.testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class KalahFixtures {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private KalahFixtures() {
        // No-Op
    }

    public static String createGameAsJson() {
        Map<String, String> map = Map.of("userA", "sergio",
            "userB", "david");
        return mapAsJson(map);


    }

    private static String mapAsJson(Map<String, String> map) {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Can not parse map to json");
        }
    }

}
