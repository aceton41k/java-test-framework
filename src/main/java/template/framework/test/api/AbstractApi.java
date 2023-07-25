package template.framework.test.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import template.framework.test.config.PropertyReader;

import java.util.List;
import java.util.Properties;

/**
 * Класс который инициализирует базовые настройки подключения к API: адрес сденда, логирование, конфиг http клиента и пр.
 */

public abstract class AbstractApi extends ApiAsserts {
    @Getter
    protected static Properties properties = PropertyReader.getProperties();
//    public final String baseUrl = PropertyReader.getBaseUrl();

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