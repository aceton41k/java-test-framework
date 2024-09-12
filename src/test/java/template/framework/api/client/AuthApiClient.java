package template.framework.api.client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import template.framework.api.model.LoginRequest;
import template.framework.api.model.LoginResponse;
import template.framework.api.model.Post;
import template.framework.config.PropertyReader;

import static io.restassured.RestAssured.given;

public class AuthApiClient extends AbstractApiClient {

    public LoginResponse auth(String email, String password) {
        httpResponse = given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .post("/auth/login");

        if (httpResponse.statusCode() == 200)
            reqSpec.header("Authorization", "Bearer " + httpResponse.as(LoginResponse.class).getToken());

        return httpResponse.as(LoginResponse.class);

    }

    public LoginResponse authDefault() {
        var user = PropertyReader.getProperties().getProperty("api.user");
        var password = PropertyReader.getProperties().getProperty("api.password");
        return auth(user, password);
    }


}

