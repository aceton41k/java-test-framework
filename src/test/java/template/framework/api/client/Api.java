package template.framework.api.client;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import io.restassured.http.*;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import template.framework.config.PropertyReader;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class Api {
    String url = "%s://%s".formatted(PropertyReader.getScheme(), PropertyReader.getHost());
    Headers headersAuth;
    String userHashHeader;
    Response httpResponse;
    private String userHashCookie;

    public void authUser(String login, String password, int userId) {
        httpResponse = given()
                .formParam("user_login", login)
                .formParam("user_pass", password)
                .when()
                .post(url + "/workspace?sec=auth_submit");
        httpResponse.then().statusCode(200);
        String str = httpResponse.headers()
                .getList("Set-Cookie")
                .stream()
                .filter(e -> e.getValue().contains("user"))
                .toList()
                .get(0)
                .getValue();
        String pattern = "user=([a-f0-9]+)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(str);
        if (matcher.find()) {
            userHashCookie = matcher.group(1);
            headersAuth = new Headers(List.of(new Header("User-Hash", userHashCookie),
                    new Header("User-Id", String.valueOf(userId)),
                    new Header("User-Login", login)));
        } else throw new RuntimeException("Не удалось получить userHash");
    }

    public void operatorSendMessage(long dialogId, String text) {

        String body = "{\"dialog_id\":%s,\"hint_uid\":null,\"media_thumb\":null,\"media_url\":null,\"text\":\"%s\"}".formatted(dialogId, text);
        given()
                .baseUri(url)
                .headers(headersAuth)
                .contentType(ContentType.JSON)
                .body(body)
                .post("bff/chat/dialog/send-message");

    }

    public void setOperatorOnline() {
        given()
                .log().all()
                .headers(headersAuth)
                .contentType(ContentType.JSON)
                .put(url + "/bff/user/online")
                .then()
                .statusCode(200)
                .body(containsString("success"));
    }
}
