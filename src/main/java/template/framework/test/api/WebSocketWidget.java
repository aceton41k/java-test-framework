package template.framework.test.api;


import io.qameta.allure.Step;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import template.framework.test.config.PropertyReader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class WebSocketWidget {
    private String username;
    private final WebSocketSession webSocketSession;


    public WebSocketWidget() throws ExecutionException, InterruptedException, TimeoutException, URISyntaxException {
        SockJsClient sockJsClient = new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient())));
        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        String url = "wss://%s/%s".formatted(PropertyReader.getHost(), PropertyReader.getWSWidgetPath());

        WebSocketHandler webSocketHandler = new WebSocketHandler() {
            @Override
            @Step("Выполнение действий после подключения к вебсокету")
            public void afterConnectionEstablished(@NonNull WebSocketSession session) throws IOException, InterruptedException {
                Thread.sleep(5000);
                session.sendMessage(new TextMessage("CONNECT\nprojectId:1\nchannelId:9\nuserAgent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36\ntoken:undefined\naccept-version:1.2,1.1,1.0\nheart-beat:0,15000\n\n\u0000"));
                Thread.sleep(500);
            }

            @Override
            @Step("Обработка сообщений от сервера(connected, message)")
            public void handleMessage(WebSocketSession session, org.springframework.web.socket.WebSocketMessage<?> message) throws IOException, InterruptedException {
                if (message.getPayload().toString().contains("CONNECTED")) {
                    session.sendMessage(new TextMessage("SUBSCRIBE\nid:sub-0\ndestination:/user/queue/settings\n\n\u0000"));
                    session.sendMessage(new TextMessage("SUBSCRIBE\nid:sub-1\ndestination:/user/queue/response\n\n\u0000"));
                    Pattern pattern;
                    pattern = Pattern.compile("user-name:(.*)");
                    Matcher matcher = pattern.matcher(message.getPayload().toString());
                    if (matcher.find())
                        username = matcher.group(1);
                    log.debug("!!!!!!!!!!!!!! send settings, ");
                    Thread.sleep(1000);
                    session.sendMessage(new TextMessage("SEND\ndestination:/app/settings\ncontent-length:17\n\n{\"channelId\":\"9\"}\u0000"));
                }
                if (message.getPayload().toString().contains("Немного терпения")) {
                    session.sendMessage(new TextMessage("UNSUBSCRIBE\nid:sub-0\n\n\u0000"));
                }
                if (message.getPayload().toString().matches("(MESSAGE).*?(auth_replay)(\\w+)")) {
                    session.sendMessage(new TextMessage("UNSUBSCRIBE\nid:sub-0\n\n\u0000"));
                }
                if (message.getPayload().toString().matches("(MESSAGE\n.*.user.queue.response)")) {
                    log.info("Received message from server");
                }
            }

            @Override
            @Step("Транспортные ошибки")
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                log.debug(exception.getMessage());
            }

            @Override
            @Step("Выполнение действий после отключения от вебсокета")
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
                log.debug("CLOSED {} reason: {}", closeStatus.getCode(), closeStatus.getReason());
            }

            @Override
            @Step("Метод связанный с поддержкой частичных сообщений")
            public boolean supportsPartialMessages() {
                return false;
            }
        };
        webSocketSession = sockJsClient.execute(webSocketHandler, webSocketHttpHeaders, new URI(url)).get(10L, SECONDS);
    }

    @Step("Создаем обращение из двух методов send и sendMessage")
    public void createAppeal(String fullName, String message) throws InterruptedException, IOException {
        Thread.sleep(5000); // TODO костыль, т.к. подключение xecute выполняется в асинхроне, мы не успеваем получить username и он приходит пустой
        String uuid = RandomStringUtils.randomAlphanumeric(10);
        String authMessage = String.format("SEND\ndestination:/app/request\ncontent-length:214\n\n{\"channelId\":\"9\",\"projectId\":1,\"token\":\"%s\",\"uuid\":\"%s\",\"action\":\"auth\",\"client_data\":{\"skillId\":200,\"phone\":\"+93547416567\",\"full_name\":\"%s\",\"message\":\"%s\"}}\u0000",
                username, uuid, fullName, message);
        String sendMessage = String.format("SEND\ndestination:/app/request\ncontent-length:211\n\n{\"channelId\":\"9\",\"projectId\":1,\"token\":\"%s\",\"uuid\":\"%s\",\"action\":\"send_message\",\"data\":{\"type\":1,\"text\":\"%s\",\"uuid\":\"%s\",\"media_thumb\":null,\"media_url\":null}}\u0000",
                username, uuid, message, uuid);

        webSocketSession.sendMessage(new TextMessage(authMessage));
        Thread.sleep(500);
        webSocketSession.sendMessage(new TextMessage(sendMessage));
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