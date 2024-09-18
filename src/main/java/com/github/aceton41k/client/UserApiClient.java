package com.github.aceton41k.client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserApiClient extends BaseApiClient {

    @Step("Update post")
    public Response me() {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/me");
    }
}
