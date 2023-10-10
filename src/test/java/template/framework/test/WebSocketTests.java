package template.framework.test;

import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tech.omnichat.test.model.WebSocket.MessageData;
import tech.omnichat.test.model.WebSocket.WebSocketMessage;
import template.framework.test.api.*;
import template.framework.test.config.PropertyReader;
import template.framework.test.model.ws.ClientData;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsString;

public class WebSocketTests {

    String url = PropertyReader.getScheme() +"://"+ PropertyReader.getHost();
    String userHashHeader= "725193dd06768ae474d5e1af8146a5ef";
    int userId = 21827;

    public WebSocketTests() {
    }

    @Test
    public void createAppeal() throws ExecutionException, InterruptedException, TimeoutException {
        StompClient stompClient = new StompClient();
        WebSocketMessage settingsMessage = new WebSocketMessage()
                .withChannelId("9");
        stompClient.send("/app/settings", settingsMessage);

        WebSocketMessage authMessage = new WebSocketMessage()
                .withChannelId("9")
                .withProjectId(1)
                .withToken(stompClient.getUsername())
                .withUuid(RandomStringUtils.randomAlphabetic(10))
                .withAction("auth")
                .withClient_data(new ClientData()
                        .withSkillId(2)
                        .withFull_name("Autotestovich")
                        .withPhone("+74324546345")
                        .withMessage("Hello!"));
        Thread.sleep(2000);
        stompClient.send("/app/request", authMessage.toString());

        String uuid = RandomStringUtils.randomAlphanumeric(10);
        WebSocketMessage sendMessage = new WebSocketMessage()
                .withChannelId("9")
                .withProjectId(1)
                .withToken(stompClient.getUsername())
                .withUuid(uuid)
                .withAction("send_message")
                .withData(new MessageData()
                        .withUuid(uuid)
                        .withText("Again")
                        .withType(1));
        Thread.sleep(2000);
        stompClient.send("/app/request", sendMessage);

        Thread.sleep(Long.MAX_VALUE);

    }


    @Test
    public void operatorWs() throws URISyntaxException, IOException, ExecutionException, InterruptedException, TimeoutException {

        WebSocketOperatorSpring webSocketOperator = new WebSocketOperatorSpring(userHashHeader, 21827);
    }

    @Test
    public void operator2() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException, IOException {
        Api api = new Api();
        api.setUserHashHeader(userHashHeader);
        api.authUser("admin1", "Usersecret!", 21827);
        String userHashCookie = api.getUserHashCookie();
        int userId = Integer.parseInt(api.getHeadersAuth().getValue("User-Id"));
        // operator set online
        api.setOperatorOnline();
        // operator connect ws
        WebSocketOperator operator = new WebSocketOperator(userHashCookie, userId);

        // widget create appeal
        WebSocketWidget widget = new WebSocketWidget();
        widget.createAppeal("John Malkovich", "Hello");

        // wait message from widget
        var queue = operator.getQueue();
        await().atMost(Duration.ofSeconds(10)).until(() ->
                !queue.isEmpty());


        api.operatorSendMessage(operator.getDialogId(), "Здратути");

    }

    @Test
    public void widgetTest() throws URISyntaxException, ExecutionException, InterruptedException, TimeoutException, IOException {
        var ws = new WebSocketWidget();
        ws.createAppeal("John Malkovich", "Hello");
        Thread.sleep(Integer.MAX_VALUE);
    }


}
