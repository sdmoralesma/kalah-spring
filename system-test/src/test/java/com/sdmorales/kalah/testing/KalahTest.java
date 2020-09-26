package com.sdmorales.kalah.testing;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.put;
import static java.util.Map.entry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class KalahTest {

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
        int pitId = 1;

        put("/games/{gameId}/pits/{pitId}", gameId, pitId)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .body("userA", is(not(emptyOrNullString())))
            .body("userB", is(not(emptyOrNullString())));
    }

    @Test
    void verifyPlayerMoveOk() {
        JsonPath game = GameFixtures.createGame();
        long gameId = game.getInt("id");
        int pitId = 1;

        Response response = put("/games/{gameId}/pits/{pitId}", gameId, pitId)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .body("userA", is(not(emptyOrNullString())))
            .body("userB", is(not(emptyOrNullString())))
            .extract().response();

        Map<String, Integer> board = JsonPath.from(response.jsonPath().getString("board")).getMap("$");
        assertThat(board, equalTo(createExpectedBoardAfterValidMoveOnPitId1()));
    }

    @Test
    void verifyPlayerRepeatedMoveOk() {
        JsonPath game = GameFixtures.createGame();
        long gameId = game.getInt("id");

        put("/games/{gameId}/pits/{pitId}", gameId, 1)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .body("userA", is(not(emptyOrNullString())))
            .body("userB", is(not(emptyOrNullString())));

        Response response = put("/games/{gameId}/pits/{pitId}", gameId, 2)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .body("userA", is(not(emptyOrNullString())))
            .body("userB", is(not(emptyOrNullString())))
            .extract().response();

        Map<String, Integer> board = JsonPath.from(response.jsonPath().getString("board")).getMap("$");
        assertThat(board, equalTo(createExpectedBoardAfterValidMoveOnPitId1AndPitId2()));
    }

    private Map<String, Integer> createExpectedBoardAfterValidMoveOnPitId1() {
        return Map
            .ofEntries(entry("1", 0), entry("2", 7), entry("3", 7), entry("4", 7), entry("5", 7), entry("6", 7),
                entry("7", 1),
                entry("8", 6), entry("9", 6), entry("10", 6), entry("11", 6), entry("12", 6), entry("13", 6),
                entry("14", 0));
    }

    private Map<String, Integer> createExpectedBoardAfterValidMoveOnPitId1AndPitId2() {
        return Map
            .ofEntries(entry("1", 0), entry("2", 0), entry("3", 8), entry("4", 8), entry("5", 8), entry("6", 8),
                entry("7", 8),
                entry("8", 7), entry("9", 7), entry("10", 6), entry("11", 6), entry("12", 6), entry("13", 6),
                entry("14", 0));
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
