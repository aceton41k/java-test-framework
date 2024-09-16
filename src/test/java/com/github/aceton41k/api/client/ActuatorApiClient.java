package com.github.aceton41k.api.client;

import com.github.aceton41k.api.model.actuator.ActuatorInfoResponse;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ActuatorApiClient extends BaseApiClient {

    public Response getHealth() {
        return given()
                //.spec(reqSpecBuilder.build())
                .get("/actuator/health");
    }

    public ActuatorInfoResponse getInfo() {
        return given()
                //.spec(reqSpecBuilder.build())
                .get("/actuator/info")
                .then()
                .statusCode(200)
                .extract()
                .as(ActuatorInfoResponse.class);
    }
}
