package com.github.aceton41k.config;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionalLoggingFilter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        // Логирование запроса
        log.info("> > > > > >");
        if (requestSpec.getURI() != null && !requestSpec.getURI().isEmpty()) {
            log.info("Request URI: " + requestSpec.getURI());
        }

        if (requestSpec.getMethod() != null && !requestSpec.getMethod().isEmpty()) {
            log.info("Request method: " + requestSpec.getMethod());
        }

        if (requestSpec.getHeaders() != null && requestSpec.getHeaders().exist()) {
            log.info("Request headers: " + requestSpec.getHeaders());
        }

        if (requestSpec.getBody() != null && !requestSpec.getBody().toString().isEmpty()) {
            log.info("Request body: " + requestSpec.getBody());
        }

        // Выполнение запроса
        Response response = ctx.next(requestSpec, responseSpec);
        log.info("< < < < < <");
        // Логирование ответа
        if (response.getStatusCode() != 0) {
            log.info("Response status code: " + response.getStatusCode());
        }

        if (response.getHeaders() != null && response.getHeaders().exist()) {
            log.info("Response headers: " + response.getHeaders());
        }

        if (response.getBody() != null && !response.getBody().asString().isEmpty()) {
            log.info("Response body: " + response.getBody().asString());
        }

        log.info("---------------");

        return response;
    }
}
