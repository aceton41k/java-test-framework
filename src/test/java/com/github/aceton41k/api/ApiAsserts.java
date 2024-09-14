package com.github.aceton41k.api;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.testng.Assert.assertEquals;

public class ApiAsserts {

    public void assertStatusCode(Response response, int expectedCode) {
        assertEquals(response.getStatusCode(), expectedCode, "HTTP status code");
    }

    public void assertInternalServerError(Response response) {
        assertStatusCode(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }


    public void assertStatusCodeUnauthorized(Response response) {
        assertStatusCode(response, HttpStatus.SC_UNAUTHORIZED);
    }

    public void assertStatusNoContent(Response response) {
        assertStatusCode(response, HttpStatus.SC_NO_CONTENT);
    }

    public void assertStatusCodeNotFound(Response response) {
        assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }

    public void assertStatusCodeOk(Response response) {
        assertStatusCode(response, HttpStatus.SC_OK);
    }

}
