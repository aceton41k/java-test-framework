package com.github.aceton41k.api.client;

import com.github.aceton41k.config.PropertyReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import java.util.List;
import java.util.Properties;


public class BaseApiClient {
    protected static RequestSpecification reqSpec;
    @Getter
    protected Response httpResponse;

    @Getter
    protected static Properties properties = PropertyReader.getProperties();

    public BaseApiClient() {
        reqSpec = new RequestSpecBuilder()
                        .setBaseUri(PropertyReader.getBaseUrl())
                        .addHeader("User-Agent", "Autotest-api-client")
                        .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured()))
                        .setRelaxedHTTPSValidation()
                        .build()
                        .log().ifValidationFails();
    }
}