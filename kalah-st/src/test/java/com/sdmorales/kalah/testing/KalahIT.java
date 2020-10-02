package com.sdmorales.kalah.testing;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.put;
import static java.util.Map.entry;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class KalahIT {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void verifyGameIsCreatedOk() {
        JsonPath game = GameFixtures.createGame();

        assertTrue(game.getInt("id") > 0);
    }

    @Test
    void verifyGameIsRetrievedByIdOk() {
        JsonPath game = GameFixtures.createGame();

        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/games/{gameId}", game.getLong("id"))
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .body("userA", is(not(emptyOrNullString())))
            .body("userB", is(not(emptyOrNullString())));
    }

    @Test
    void verifyMakeMoveIsOk() {
        JsonPath game = GameFixtures.createGame();
        long gameId = game.getInt("id");

        put("/games/{gameId}/pits/{pitId}", gameId, 1)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)));
    }

    @Test
    void verifyPlayerMoveOk() {
        JsonPath game = GameFixtures.createGame();
        long gameId = game.getInt("id");

        Response response = put("/games/{gameId}/pits/{pitId}", gameId, 1)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .extract().response();

        Map<String, Integer> board = JsonPath.from(response.jsonPath().getString("board")).getMap("$");
        assertMapEquals(fromListToMap(List.of(0, 0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0)), board);
    }

    @Test
    void verifyPlayerCanRepeatTurnOk() {
        JsonPath game = GameFixtures.createGame();
        long gameId = game.getInt("id");

        put("/games/{gameId}/pits/{pitId}", gameId, 1)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)));

        Response response = put("/games/{gameId}/pits/{pitId}", gameId, 2)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .extract().response();

        Map<String, Integer> board = JsonPath.from(response.jsonPath().getString("board")).getMap("$");
        assertMapEquals(fromListToMap(List.of(1, 0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0)), board);
    }

    @Test
    void verifyBoardCanBeModifiedOk() {
        JsonPath game = GameFixtures.createGame();
        long gameId = game.getInt("id");

        Map<String, Integer> newBoard = fromListToMap(List.of(1, 0, 0, 0, 0, 0, 1, 35, 0, 0, 0, 0, 0, 1, 35));

        Response response = given()
            .body(newBoard)
            .contentType(ContentType.JSON)
            .put("/games/{gameId}", gameId)
            .then()
            .statusCode(200)
            .extract().response();

        Map<String, Integer> board = JsonPath.from(response.jsonPath().getString("board")).getMap("$");
        assertMapEquals(fromListToMap(List.of(1, 0, 0, 0, 0, 0, 1, 35, 0, 0, 0, 0, 0, 1, 35)), board);
    }


    @Test
    void verifyNorthPlayerWins() {
        JsonPath game = GameFixtures.createGame();
        long gameId = game.getInt("id");

        Map<String, Integer> newBoard = fromListToMap(List.of(1, 0, 0, 0, 0, 0, 1, 25, 0, 0, 0, 0, 0, 1, 35));

        given()
            .body(newBoard)
            .contentType(ContentType.JSON)
            .put("/games/{gameId}", gameId)
            .then()
            .statusCode(200);

        Response response = put("/games/{gameId}/pits/{pitId}", gameId, 13)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("status", is("FINISHED"))
            .body("winner", is("NORTH"))
            .extract().response();

        Map<String, Integer> board = JsonPath.from(response.jsonPath().getString("board")).getMap("$");
        assertMapEquals(fromListToMap(List.of(1, 0, 0, 0, 0, 0, 0, 26, 0, 0, 0, 0, 0, 0, 36)), board);
    }

    @Test
    void verifySouthPlayerWins() {
        JsonPath game = GameFixtures.createGame();
        long gameId = game.getInt("id");

        Map<String, Integer> newBoard = fromListToMap(List.of(0, 0, 0, 0, 0, 0, 1, 35, 0, 0, 0, 0, 0, 1, 25));

        given()
            .body(newBoard)
            .contentType(ContentType.JSON)
            .put("/games/{gameId}", gameId)
            .then()
            .statusCode(200);

        Response response = put("/games/{gameId}/pits/{pitId}", gameId, 6)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("status", is("FINISHED"))
            .body("winner", is("SOUTH"))
            .extract().response();

        Map<String, Integer> board = JsonPath.from(response.jsonPath().getString("board")).getMap("$");
        assertMapEquals(fromListToMap(List.of(0, 0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 0, 26)), board);
    }

    private static void assertMapEquals(Map<String, Integer> expected, Map<String, Integer> actual) {
        assertEquals(new TreeMap<>(expected), new TreeMap<>(actual));
    }

    private Map<String, Integer> fromListToMap(List<Integer> list) {
        return IntStream.rangeClosed(0, 14)
            .boxed()
            .map(i -> entry(i, list.get(i)))
            .collect(Collectors.toUnmodifiableMap(entry -> entry.getKey().toString(), Entry::getValue));
    }

    @Nested
    class ErrorHandling {

        @Test
        void moveOnNonExistingGameReturnsNotFound() {
            put("/games/{gameId}/pits/{pitId}", 100, 100)
                .then()
                .statusCode(404);
        }

        @Test
        void samePlayerMovingTwiceReturnsInvalidRequest() {
            JsonPath game = GameFixtures.createGame();
            long gameId = game.getInt("id");
            int pitId = 1;

            put("/games/{gameId}/pits/{pitId}", gameId, pitId)
                .then()
                .statusCode(200);

            put("/games/{gameId}/pits/{pitId}", gameId, pitId)
                .then()
                .statusCode(400);
        }
    }

}
