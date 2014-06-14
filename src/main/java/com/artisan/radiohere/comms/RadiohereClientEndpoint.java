package com.artisan.radiohere.comms;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;


@WebSocket
public class RadiohereClientEndpoint {
    private static CountDownLatch closeLatch = new CountDownLatch(1);
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @OnWebSocketConnect
    public void onOpen(Session session) {
        logger.info("Connected ... " + session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String scrambledWord) {
        logger.info("Client receives ...." + scrambledWord);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        logger.info(String.format("Session %s close because of %s", session, reason));
        closeLatch.countDown();
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    public static void main(String[] args) {
        String destUri = "ws://localhost:8025/game";
        if (args.length > 0) {
            destUri = args[0];
        }
        WebSocketClient client = new WebSocketClient();
        RadiohereClientEndpoint socket = new RadiohereClientEndpoint();
        try {
            client.start();
            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            System.out.printf("Connecting to : %s%n", echoUri);
            socket.awaitClose(50, TimeUnit.SECONDS);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }}
