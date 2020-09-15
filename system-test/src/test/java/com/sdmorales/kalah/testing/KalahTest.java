package com.sdmorales.kalah.testing;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.post;
import static io.restassured.RestAssured.put;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class KalahTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void verifyPingOk() {
        get("/ping")
            .then()
            .statusCode(200)
            .body(equalTo("pong"));
    }

    @Test
    void verifyGameIsCreatedOk() {
        post("/games")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(notNullValue()));
    }

    @Test
    void verifyMakeMoveIsOk() {
        put("/games/{gameId}/pits/{pitId}", UUID.randomUUID().toString(), UUID.randomUUID().toString())
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(notNullValue()));
    }
}
