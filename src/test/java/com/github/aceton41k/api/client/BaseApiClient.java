package com.github.aceton41k.api.client;

import com.github.aceton41k.config.PropertyReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import java.util.List;

import static io.restassured.RestAssured.given;


@Getter
public class BaseApiClient {
    protected static RequestSpecification reqSpec;
    protected RequestSpecBuilder reqSpecBuilder = new RequestSpecBuilder()
            .setBaseUri(PropertyReader.getBaseUrl())
            .addHeader("User-Agent", "Autotest-api-client")
            .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured()))
            .setRelaxedHTTPSValidation()
    ;
    protected Response httpResponse;

    public BaseApiClient() {
        reqSpec = reqSpecBuilder.build();
    }

    public Response baseRequest(String endpoint, Method method) {
        RequestSpecification reqSpecBase = reqSpecBuilder.build();
        return given().spec(reqSpecBase).request(method, endpoint);
    }
}