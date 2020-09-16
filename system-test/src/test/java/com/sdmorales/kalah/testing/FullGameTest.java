package com.sdmorales.kalah.testing;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.put;
import static java.util.Map.entry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FullGameTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void verifyFullGameOk() {
        given()
            .body(KalahFixtures.createGameAsJson())
            .contentType(ContentType.JSON)
            .when()
            .post("/games")
            .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .header("Location", is(not(emptyString())))
            .body("id", is(greaterThan(0)))
            .body("userA", is("sergio"))
            .body("userB", is("david"));

        Response response = put("/games/{gameId}/pits/{pitId}", 1, 1)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .body("userA", is("sergio"))
            .body("userB", is("david"))
            .extract().response();

        Map<String, Integer> board = JsonPath.from(response.jsonPath().getString("board")).getMap("$");
        assertThat(board, equalTo(createExpectedBoard()));
    }

    private Map<String, Integer> createExpectedBoard() {
        return Map
            .ofEntries(entry("1", 6), entry("2", 6), entry("3", 6), entry("4", 6), entry("5", 6), entry("6", 6),
                entry("7", 0),
                entry("8", 6), entry("9", 6), entry("10", 6), entry("11", 6), entry("12", 6), entry("13", 6),
                entry("14", 0));
    }
}
