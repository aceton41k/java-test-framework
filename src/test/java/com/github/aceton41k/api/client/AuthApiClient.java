package com.github.aceton41k.api.client;

import com.github.aceton41k.api.model.LoginRequest;
import com.github.aceton41k.api.model.LoginResponse;
import com.github.aceton41k.api.model.SignupRequest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class AuthApiClient extends BaseApiClient {

    @Step("Auth")
    public LoginResponse auth(String email, String password) {
        httpResponse = given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .post("/auth/login");

        if (httpResponse.statusCode() == 200)
            reqSpec.header("Authorization", "Bearer " + httpResponse.as(LoginResponse.class).getToken());

        return httpResponse.as(LoginResponse.class);

    }

    @Step("Sign up")
    public void signUp(String email, String password, String fullName) {
         given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(new SignupRequest(email, password, fullName))
                .post("/auth/signup")
                .then()
                .statusCode(200);

    }

}

