package com.sdmorales.kalah.testing;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class KalahTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void verifyOK() {
        get("/lotto")
            .then()
            .statusCode(200)
            .body("lotto.lottoId", equalTo(5));
    }

}
