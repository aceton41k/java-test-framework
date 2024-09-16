package com.github.aceton41k.api.client;

import com.github.aceton41k.api.model.actuator.ActuatorInfoResponse;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ActuatorApiClient extends BaseApiClient {

    private final String basePath = "actuator";

    @Step("Get actuator health")
    public Response getHealth() {
        return given()
                .contentType(ContentType.JSON)
                .basePath(basePath)
                .get("/health");
    }

    @Step("Get actuator Info")
    public ActuatorInfoResponse getInfo() {
        return given()
                .contentType(ContentType.JSON)
                .basePath(basePath)
                .get("/info")
                .then()
                .statusCode(200)
                .extract()
                .as(ActuatorInfoResponse.class);
    }
}
