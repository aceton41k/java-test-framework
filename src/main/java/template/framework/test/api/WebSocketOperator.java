package template.framework.test.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import template.framework.test.config.PropertyReader;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Getter
public class WebSocketOperator {
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
    private Long dialogId;
    private Long appealId;

    public WebSocketOperator(String userHash, int userId) throws URISyntaxException {
        var uri = new URI("wss://%s/%s".formatted(
                PropertyReader.getHost(),
                PropertyReader.getWSOperatorPath()));

        WebSocketClient webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                send("{\"callback\":\"connect\",\"user_id\":%d,\"user_hash\":\"%s\",\"project_id\":1}".formatted(userId, userHash));

            }

            @Override
            public void onMessage(String message) {
                //  log.debug("Received message: {}", message);
                if (message.contains("appealCreationDate")) {
                    log.debug("!!!!!!!!!!!!!!!!!!!!!! {}", message);
                    queue.add(message);
                    var data = new JSONObject(message).getJSONObject("data");
                    dialogId = data.getLong("dialogId");
                    appealId = data.getLong("appealId");
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                log.debug("CLOSED reason: {}", reason);
            }

            @Override
            public void onError(Exception ex) {
                log.debug("ERROR: {}", ex.getMessage());
            }
        };
        webSocketClient.connect();
    }
}
