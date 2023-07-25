package template.framework.test.api;

import io.qameta.allure.Step;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.*;

/**
 * Класс содержит базовые проверки для тестов.
 */
public class ApiAsserts {
    Response httpResponse;

    public ApiAsserts assertCookieNotExist(Cookies cookies, String cookie) {
        Assert.assertNull(cookies.get(cookie), "cookie");
        return this;
    }

    @Step("Проверка http кода")
    public ApiAsserts assertStatusCode(int code, String message) {
        assertEquals(httpResponse.statusCode(), code, message);
        return this;
    }
    @Step("Проверка cookies")
    public ApiAsserts assertCookies() {
        assertNotNull(httpResponse.getDetailedCookies().get("SESSION"), "SESSION пустая. Возможно не удалось авторизоваться.");
        return this;
    }

    @Step("Проверка http кода")
    public ApiAsserts assertStatusCode(int code) {
        assertEquals(httpResponse.statusCode(), code, "HTTP код");
        return this;
    }

    @Step("Проверка значения в поле JSON ответа")
    public ApiAsserts assertBody(String jsonPath, String expectedValue, String message) {
        assertEquals(httpResponse.jsonPath().getString(jsonPath), expectedValue, message);
        return this;
    }

    @Step("Проверка значения в поле JSON ответа")
    public ApiAsserts assertBody(String jsonPath, Object expectedObject, String message) {
        assertEquals(httpResponse.jsonPath().get(jsonPath), expectedObject, message);
        return this;
    }

    @Step("Проверка значения в поле JSON ответа")
    public ApiAsserts assertBody(String jsonPath, int expectedValue, String message) {
        assertEquals(httpResponse.jsonPath().getInt(jsonPath), expectedValue, message);
        return this;
    }

    @Step("Проверка, что JSON ответа содержит строку.")
    public ApiAsserts assertBodyContainsString(String jsonPath, String expectedValue, String message) {
        assertTrue(httpResponse.jsonPath().getString(jsonPath).contains(expectedValue), message);
        return this;
    }

    @Step("Проверка, что ответ содержит строку.")
    public ApiAsserts assertString(String expectedValue, String message) {
        assertEquals(httpResponse.asString(), expectedValue, message);
        return this;
    }

    public ApiAsserts assertListsAreEqualWithOrder(String jsonPath, List<?> expectedList) {
        assertListsAreEqualWithOrder(jsonPath, expectedList, "List");
        return this;
    }

    @Step("Проверка, что JSON ответа содержит список.")
    public ApiAsserts assertListsAreEqualWithOrder(String jsonPath, List<?> expectedList, String message) {
        assertEquals(httpResponse.jsonPath().getList(jsonPath), expectedList, message);
        return this;
    }

    @Step("Проверка, что JSON ответа содержит список.")
    public ApiAsserts assertListsAreEqualAnyOrder(String jsonPath, List<?> expectedList, String message) {
        assertTrue(new HashSet<>(httpResponse.jsonPath().getList(jsonPath)).containsAll(expectedList), message);
        return this;
    }

    public ApiAsserts assertElementInList(String jsonPath, Object expectedValue) {
        assertElementInList(jsonPath, expectedValue, "Value [" + expectedValue + "] should be in list");
        return this;
    }

    @Step("Проверка, что JSON ответа содержит элемент в списке.")
    public ApiAsserts assertElementInList(String jsonPath, Object expectedValue, String message) {
        assertTrue(httpResponse.jsonPath().getList(jsonPath).contains(expectedValue), message);
        return this;
    }

    @Step("Проверка, что поле JSON ответа не пустое.")
    public ApiAsserts assertBodyValueNotNull(String jsonPath) {
        assertNotNull(httpResponse.jsonPath().getString(jsonPath), "Поле");
        return this;
    }

    @Step("Проверка, что JSON содержит список заданного размера.")
    public ApiAsserts assertListSize(String jsonPath, int expectedSize) {
        assertEquals(httpResponse.jsonPath().getList(jsonPath).size(), expectedSize, "Размер");
        return this;
    }

    public ApiAsserts assertStatusCodeOk() {
        assertStatusCode(200);
        return this;
    }

    public ApiAsserts assertContainsString(String expectedString, String message) {
        assertTrue(httpResponse.getBody().asString().contains(expectedString), message);
        return this;
    }

    public ApiAsserts assertContainsStringRegExp(String regexp, String message) {
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(httpResponse.asString());
        assertTrue(matcher.find(), message);
        return this;
    }

    public ApiAsserts assertBodyCode200() {
        assertBody("code", 200, "HTTP Code");
        return this;
    }


    public <T> T getValue(String jsonPath) {
        return httpResponse.jsonPath().get(jsonPath);
    }

    public <T> T getObject(String jsonPath, Class<T> tClass) {
        return httpResponse.jsonPath().getObject(jsonPath, tClass);
    }
}
