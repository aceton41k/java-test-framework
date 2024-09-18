package com.github.aceton41k.client;

import com.github.aceton41k.config.ConditionalLoggingFilter;
import com.github.aceton41k.config.PropertyReader;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
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

    static final Header userAgent;
    static final List<Filter> filters;

    static {
        userAgent = new Header("User-Agent", "Autotest-api-client");
        filters = List.of(new ConditionalLoggingFilter(), new AllureRestAssured());
    }

    protected static RequestSpecification reqSpec = new RequestSpecBuilder()
            .setBaseUri(PropertyReader.getBaseUrl())
            .addHeader(userAgent.getName(), userAgent.getValue())
            .addFilters(filters)
            .setRelaxedHTTPSValidation().build();

    protected Response httpResponse;

    /**
     * New request with initial RequestSpecification
     * <p>Useful for customized requests</p>
     */
    @Step("Anonymous request")
    public Response anonymousRequest(String endpoint, Method method) {
        return given()
                .filters(filters)
                .header(userAgent)
                .request(method, endpoint);
    }

    /**
     * Overloaded method with defining RequestSpecification
     */
    @Step("Anonymous request")
    public Response anonymousRequest(String endpoint, Method method, RequestSpecification reqSpec) {
        return given()
                .spec(reqSpec)
                .filters(filters)
                .header(userAgent)
                .request(method, endpoint);
    }
}