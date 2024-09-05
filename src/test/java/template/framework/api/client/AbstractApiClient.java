package template.framework.api.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import template.framework.api.ApiAsserts;
import template.framework.config.PropertyReader;

import java.util.List;
import java.util.Properties;


public abstract class AbstractApiClient extends ApiAsserts {

    @Getter
    protected static Properties properties = PropertyReader.getProperties();

    protected final RequestSpecification reqSpec =
            new RequestSpecBuilder()
                    .setBaseUri(getBaseUrl())
                    .addHeader("User-Agent", "Autotest-api-client")
                    .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured()))
                    .setRelaxedHTTPSValidation()
                    .build()
                    .log().ifValidationFails();

    abstract String getBaseUrl();

    public ApiAsserts withAsserts() {
        return new ApiAsserts();
    }
}