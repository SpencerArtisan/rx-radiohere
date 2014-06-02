package com.artisan.radiohere.comms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.glassfish.tyrus.server.Server;
import org.json.JSONObject;

import rx.Observable;

import com.artisan.radiohere.Artist;
import com.artisan.radiohere.ArtistObservableFactory;

@ServerEndpoint(value = "/game")
public class RadiohereServerEndpoint {
    private static Logger logger = Logger.getLogger(RadiohereServerEndpoint.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Client initiates Radiohere... " + session.getId());
    		Observable<Artist> factory = new ArtistObservableFactory().create();
    		factory.subscribe((artist) -> sendToClient(session, artist));
    }
    
    private void sendToClient(Session session, Artist artist) {
    		try {
    			String artistJSON = new JSONObject(artist).toString();
    	        logger.info("Server sending artist to client... " + artistJSON);
			session.getBasicRemote().sendText(artistJSON);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    public static void main(String[] args) {
        logger.info("Starting glassfish server on " + System.getenv("HOSTNAME"));
        Server server = new Server(System.getenv("HOSTNAME"), 8025, "/websockets", null, RadiohereServerEndpoint.class);
		
		try {
		    server.start();
		} catch (Exception e) {
			logger.warning("Error starting server: " + e.getMessage());
		    e.printStackTrace();
		} finally {
//		    server.stop();
		}
    }
}
