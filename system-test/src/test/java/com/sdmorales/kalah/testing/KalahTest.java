package com.sdmorales.kalah.testing;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.put;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class KalahTest {

    public static final String USER_A_NAME = "sergio";
    public static final String USER_B_NAME = "david";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void verifyGameIsCreatedOk() {
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
            .body("userA", is(USER_A_NAME))
            .body("userB", is(USER_B_NAME));
    }

    @Test
    void verifyGameIsRetrievedByIdOk() {
        given()
            .body(KalahFixtures.createGameAsJson())
            .contentType(ContentType.JSON)
            .when()
            .get("/games/{gameId}", 1)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .body("userA", is(USER_A_NAME))
            .body("userB", is(USER_B_NAME));
    }

    @Test
    void verifyMakeMoveIsOk() {
        put("/games/{gameId}/pits/{pitId}", 1, 1)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .body("userA", is(USER_A_NAME))
            .body("userB", is(USER_B_NAME));
    }
}
