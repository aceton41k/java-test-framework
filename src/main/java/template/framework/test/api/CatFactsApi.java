package template.framework.test.api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;
import template.framework.test.model.response.CatFactResponse;

import static io.restassured.RestAssured.given;

public class CatFactsApi extends AbstractApi {

    @Getter
    private CatFactResponse catFactResponse;

    @Override
    String getBaseUrl() {
        return properties.getProperty("cat.facts.url");
    }

    private Response getCatFactHttpResponse() {
        httpResponse = given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/fact");
        return httpResponse;
    }

    @Step
    public CatFactsApi getFactCatBodyResponse() {
        catFactResponse = getCatFactHttpResponse()
                .as(CatFactResponse.class);
        return this;
    }
}
