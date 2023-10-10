package template.framework.test.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.util.MimeType;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import template.framework.test.config.PropertyReader;

import java.lang.reflect.Type;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Вебсокет клиент для получения сообщений.
 */
@Slf4j
public class StompClient {
    @Getter
    private String username;
    int timeoutSecs = 50;

    private final StompSession stompSession;
    StompSession.Subscription subSettings;
    private final BlockingQueue<WebSocketMessage> messageQueue = new ArrayBlockingQueue<>(10);
    private StompSession.Subscription subscriptionsSettings;

    public StompClient() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient stompClient =
                new WebSocketStompClient(new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());

        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();

        String url = PropertyReader.getWebSocketUrl();
        var stompHeaders = new StompHeaders();
        stompHeaders.add("projectId", "1");
        stompHeaders.add("channelId", "9");
        stompHeaders.add("userAgent", "Chrome");
        stompHeaders.add("token", "undefined");
        stompHeaders.setAcceptVersion("1.1", "1.2");
        stompHeaders.setHeartbeat(new long[]{0L, 15000L});

        stompSession = stompClient.connectAsync(url, webSocketHttpHeaders, stompHeaders, sessionHandler)
                .get(10L, TimeUnit.SECONDS);
    }

    private final StompSessionHandler sessionHandler = new StompSessionHandler() {
        @Override
        public void afterConnected(@Nullable StompSession session, @Nullable StompHeaders connectedHeaders) {
            log.info("Connected to server");
            username = connectedHeaders.get("user-name").get(0);
            StompHeaders stompHeaders = new StompHeaders();
            stompHeaders.setId("sub-0");
            stompHeaders.setDestination("/user/queue/settings");
            subSettings = stompSession.subscribe(stompHeaders, sessionHandler);
            stompHeaders.setId("sub-1");
            stompHeaders.setDestination("/user/queue/response");
            stompSession.subscribe(stompHeaders, sessionHandler);
        }

        @Override
        public Type getPayloadType(@Nullable StompHeaders headers) {
            if (headers != null) {
                log.debug("GOT from destination: {}", headers.getDestination());
                if (headers.getContentType() == MimeType.valueOf("application/json"))
                    return JsonObject.class;
                if (headers.getContentType() == MimeType.valueOf("text/plain"))
                    return String.class;
            }

            return Object.class;
        }

        @Override
        public void handleFrame(@Nullable StompHeaders headers, Object payload) {
            if (payload != null) {
                log.info("Received message: {}", payload);
                if (payload instanceof JsonObject) {
//                    if(((JsonObject) payload).jso("settings.skillGroups.1.alias_disclaimer").contains("Немного терпения"))
                    subSettings.unsubscribe();
                }
            }
//            ObjectMapper mapper = new ObjectMapper();
//            WebSocketMessage fileUploadMessage;
//            if (payload instanceof List<?>) {
//                fileUploadMessage = mapper.convertValue(((List<?>) payload).get(0), WebSocketMessage.class);
//                messageQueue.add(fileUploadMessage);
//            }
//            if (payload instanceof byte[]) {
//                String message = new String((byte[]) payload, StandardCharsets.UTF_8);
//                if (message.contains("\"type\":\"heartbeat\"")) {
//                    System.out.println("Received heartbeat");
//                }
//            }
        }

        @Override
        public void handleException(@Nullable StompSession session, @Nullable StompCommand command,
                                    @Nullable StompHeaders headers, @Nullable byte[] payload, Throwable exception) {
            log.debug(exception.getMessage());
        }

        @Override
        public void handleTransportError(@Nullable StompSession session, Throwable exception) {
            log.debug(exception.getMessage());
        }

    };

    @Step()
    public StompSession.Subscription subscribe(String destination) {
        StompHeaders subscribe = new StompHeaders();
        subscribe.setDestination(destination);
        subscriptionsSettings = stompSession.subscribe(subscribe, sessionHandler);
        return subscriptionsSettings;
    }

    public void unsubscribe(StompSession.Subscription subscriptions) {
        subscriptions.unsubscribe();
    }

    @Step()
    public StompSession.Receiptable send(String destination, Object payload) {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.setDestination(destination);
        log.info("sending payload: {}", payload);
        return stompSession.send(stompHeaders, payload);
    }

    @Step("Get status message")
    public WebSocketMessage getFileUploadMessage() {
        WebSocketMessage response;
        try {
            response = messageQueue.poll(timeoutSecs, SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (response != null)
            return response;
        else {
            disconnect();
            throw new NoSuchElementException
                    (String.format("Could not receive status message after %d sec of waiting", timeoutSecs));
        }
    }

    @Step()
    public void disconnect() {
        stompSession.disconnect();
        log.info("Disconnected");
    }

    @Step()
    public void clearQueue() {
        messageQueue.clear();
    }
}