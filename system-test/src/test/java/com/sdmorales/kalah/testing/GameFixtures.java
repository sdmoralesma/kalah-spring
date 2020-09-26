package com.sdmorales.kalah.testing;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

public class GameFixtures {

    private GameFixtures() {
        // No-Op
    }

    public static JsonPath createGame() {
        return given()
            .contentType(ContentType.JSON)
            .post("/games")
            .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .header("Location", is(not(emptyString())))
            .body("id", is(greaterThan(0)))
            .body("userA", is(not(emptyOrNullString())))
            .body("userB", is(not(emptyOrNullString())))
            .body("turn", is(not(emptyOrNullString())))
            .extract().response().jsonPath();
    }

}
