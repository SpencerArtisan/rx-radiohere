package com.artisan.radiohere.comms;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

@ClientEndpoint
public class RadiohereClientEndpoint {
    private static CountDownLatch latch;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
    }

    @OnMessage
    public String onMessage(String scrambledWord, Session session) {
        logger.info("Client receives ...." + scrambledWord);
        return null;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s close because of %s", session.getId(), closeReason));
        latch.countDown();
    }

    public static void main(String[] args) throws Exception {
        latch = new CountDownLatch(1);
        ClientManager.createClient().connectToServer(
        			RadiohereClientEndpoint.class, 
        			new URI("ws://localhost:8025/websockets/game"));
        latch.await();
    }
}
