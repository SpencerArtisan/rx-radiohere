package com.artisan.radiohere.comms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.glassfish.tyrus.server.Server;

@ServerEndpoint(value = "/game")
public class RadiohereServerEndpoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
    }

    @OnMessage
    public String onMessage(String unscrambledWord, Session session) {
        logger.info("Server receives ...." + unscrambledWord);
        return unscrambledWord;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    public static void main(String[] args) {
        Server server = new Server("localhost", 8025, "/websockets", null, RadiohereServerEndpoint.class);
		
		try {
		    server.start();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		    System.out.print("Please press a key to stop the server.");
		    reader.readLine();
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    server.stop();
		}
    }
}
