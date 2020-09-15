package com.sdmorales.kalah.testing;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.put;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class KalahTest {

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
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .body("userA", is("sergio"))
            .body("userB", is("david"));
    }

    @Test
    void verifyMakeMoveIsOk() {
        put("/games/{gameId}/pits/{pitId}", 1, 1)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(greaterThan(0)))
            .body("userA", is("sergio"))
            .body("userB", is("david"));
    }
}
