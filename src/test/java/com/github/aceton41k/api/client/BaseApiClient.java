package com.github.aceton41k.api.client;

import com.github.aceton41k.config.PropertyReader;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import java.util.List;

import static io.restassured.RestAssured.given;


@Getter
public class BaseApiClient {

    /**
     * Common request which stores and reuse all data in request specification
     * <p>Useful for authorized requests<p/>
     */
    static final Header userAgent = new Header("User-Agent", "Autotest-api-client");

    protected static RequestSpecification reqSpec = new RequestSpecBuilder()
            .setBaseUri(PropertyReader.getBaseUrl())
            .addHeader(userAgent.getName(), userAgent.getValue())
            .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured()))
            .setRelaxedHTTPSValidation().build();

    protected Response httpResponse;

    /**
     * New request with initial RequestSpecification
     * <p>Useful for customized requests</p>
     */
    @Step("Anonymous request")
    public Response anonymousRequest(String endpoint, Method method) {
        return given()
                .spec(new RequestSpecBuilder()
                        .addHeader(userAgent.getName(), userAgent.getValue()).build())
                .request(method, endpoint);
    }
}