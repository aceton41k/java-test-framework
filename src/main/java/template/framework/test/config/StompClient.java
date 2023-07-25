package template.framework.test.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.http.Cookies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import template.framework.test.model.ws.Message;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Вебсокет клиент для получения сообщений.
 */
@Slf4j
public class StompClient {

    int timeoutSecs = 50;

    private StompSession stompSession;
    private final Cookies cookies;
    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(10);

    public StompClient(Cookies cookies) {
        this.cookies = cookies;
    }

    public void connect() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setDefaultHeartbeat(new long[]{10000L, 10000L});
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());

        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        webSocketHttpHeaders.set("Cookie", cookies.get("SESSION").toString());
        String url = PropertyReader.getWebSocketUrl();

        stompSession = stompClient.connectAsync(url, webSocketHttpHeaders, new StompHeaders(), sessionHandler)
                .get(10L, TimeUnit.SECONDS);
    }

    private final StompSessionHandler sessionHandler = new StompSessionHandler() {

        @Override
        public Type getPayloadType(@Nullable StompHeaders headers) {
            return new TypeReference<List<Message>>() {
            }.getType();
        }

        @Override
        public void afterConnected(@Nullable StompSession session, @Nullable StompHeaders connectedHeaders) {
            log.info("Connected to server");
            StompHeaders subscribe = new StompHeaders();
            subscribe.setDestination("/user/file-upload-editor");
            subscribe.setId("file-upload-editor");
            stompSession.subscribe(subscribe, sessionHandler);
        }

        @Override
        public void handleFrame(@Nullable StompHeaders headers, Object payload) {
            log.info("Received message");
            ObjectMapper mapper = new ObjectMapper();
            Message fileUploadMessage;
            if (payload instanceof List<?>) {
                fileUploadMessage = mapper.convertValue(((List<?>) payload).get(0), Message.class);
                messageQueue.add(fileUploadMessage);
            }
            if (payload instanceof byte[]) {
                String message = new String((byte[]) payload, StandardCharsets.UTF_8);
                if (message.contains("\"type\":\"heartbeat\"")) {
                    System.out.println("Received heartbeat");
                }
            }
        }

        @Override
        public void handleException(@Nullable StompSession session, @Nullable StompCommand command,
                                    @Nullable StompHeaders headers, @Nullable byte[] payload, Throwable exception) {
            System.out.println(exception.getMessage());
        }


        @Override
        public void handleTransportError(@Nullable StompSession session, Throwable exception) {
            System.out.println(exception.getMessage());
        }

    };

    @Step("Get status message")
    public Message getFileUploadMessage() {
        Message response;
        try {
            response = messageQueue.poll(timeoutSecs, SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (response != null)
            return response;
        else {
            disconnect();
            throw new NoSuchElementException(String.format("Could not receive status message after %d sec of waiting [SZ-857]", timeoutSecs));
        }
    }

    public void disconnect() {
        stompSession.disconnect();
        log.info("Disconnected");
    }

    public void clearQueue() {
        messageQueue.clear();
    }
}
