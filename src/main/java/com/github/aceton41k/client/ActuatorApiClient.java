package com.github.aceton41k.client;

import com.github.aceton41k.model.actuator.ActuatorInfoResponse;
import com.github.aceton41k.model.actuator.health.HealthResponse;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class ActuatorApiClient extends BaseApiClient {

    private final String basePath = "actuator";

    @Step("Get actuator health")
    public HealthResponse getHealth() {
        return given()
                .basePath(basePath)
                .header(userAgent)
                .filters(filters)
                .contentType(ContentType.JSON)
                .get("/health")
                .then()
                .statusCode(200)
                .extract()
                .as(HealthResponse.class);
    }

    @Step("Get actuator Info")
    public ActuatorInfoResponse getInfo() {
        return given()
                .basePath(basePath)
                .header(userAgent)
                .filters(filters)
                .contentType(ContentType.JSON)
                .get("/info")
                .then()
                .statusCode(200)
                .extract()
                .as(ActuatorInfoResponse.class);


    }
}
