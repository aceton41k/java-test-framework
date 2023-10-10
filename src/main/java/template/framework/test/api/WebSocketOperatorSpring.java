package template.framework.test.api;

import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class WebSocketOperatorSpring {
    private final WebSocketHandler webSocketHandler;
    @Getter
    private String username;
    @Getter
    private WebSocketSession webSocketSession;

    public WebSocketOperatorSpring(String userHash, int userId) throws ExecutionException, InterruptedException, TimeoutException, URISyntaxException, IOException {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
//        webSocketHttpHeaders.add("Cookie", "user=" + userHash);
//        webSocketHttpHeaders.add("Cookie", "PHPSESSID=8egj8e79uapc9vi5dogkure801");
        String url = "wss://omni-qa1.omnichat.tech/workspace/chat";
       // int userId = 21827;

        webSocketHandler = new WebSocketHandler() {
            @Override
            @Step("Выполнение действйи после подключения к вебсокету")
            public void afterConnectionEstablished(WebSocketSession session) throws IOException, InterruptedException {
                WebSocketMessage<String> message = new TextMessage(String.format("{\"callback\":\"connect\",\"user_id\":%d,\"user_hash\":\"%s\",\"project_id\":1}", userId, userHash), true);
                session.sendMessage(message);
                log.info("message send: {} ", message.getPayload());
                Thread.sleep(1000);
            }

            @Override
            @Step("Обработка сообщений от сервера(connected, message)")
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
                log.debug("message received: {}", message);
            }

            @Override
            @Step("Транспортные ошибки")
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                log.debug(exception.getMessage());
            }

            @Override
            @Step("Выполнение действйи после отключения от вебсокета")
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {

            }

            @Override
            @Step("Метод связанный с поддержкой частичных сообщений")
            public boolean supportsPartialMessages() {
                return false;
            }
        };
        webSocketSession = standardWebSocketClient.execute(webSocketHandler, webSocketHttpHeaders, new URI(url)).get(10L, SECONDS);
    }

    @Step("Метод для закрытия соединения")
    public void disconnect() {
        try {
            webSocketSession.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Disconnected");
    }
}
