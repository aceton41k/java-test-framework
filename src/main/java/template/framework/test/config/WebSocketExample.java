package template.framework.test.config;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketExample {
    public static void main(String[] args) {
        String url = "wss://omni-dev-2.omnichat.tech/webChat/api/v2/WebSocket/186/aaphkb5g/websocket";

        try {
            WebSocketClient client = new WebSocketClient(new URI(url)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("Connected to server.");
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Received message: " + message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Connection closed. Code: " + code + ", Reason: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("Error occurred: " + ex.getMessage());
                }
            };

            client.connect();
            Thread.sleep(2000);
            client.send("[\"CONNECT\nprojectId:1\nchannelId:9\nuserAgent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36\ntoken:undefined\naccept-version:1.2,1.1,1.0\nheart-beat:10000,10000\n\n\u0000\"]");

            // Keep the program running to maintain the WebSocket connection
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}