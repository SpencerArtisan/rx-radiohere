package com.artisan.radiohere.comms;

import java.util.logging.Logger;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import rx.Observable;

import com.artisan.radiohere.Artist;
import com.artisan.radiohere.ArtistObservableFactory;

@WebSocket
public class RadiohereServerSocket {
    private static Logger logger = Logger.getLogger(RadiohereServerSocket.class.getName());

    @OnWebSocketConnect
    public void onOpen(Session session) {
        logger.info("Client initiates Radiohere... " + session);
    		Observable<Artist> factory = new ArtistObservableFactory().create();
    		factory.subscribe((artist) -> sendToClient(session, artist),
    				(e) -> logger.warning("STREAM ERROR: " + e.getMessage()),
    				() -> logger.info("STREAM FINISHED"));
    }
    
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
    		logger.info("** Received message from client: " + message);
    }

    	private void sendToClient(Session session, Artist artist) {
    		logger.info("Sending to client...");
		String artistJSON = new JSONObject(artist).toString();
        logger.info("Server sending artist to client... " + artistJSON);
		session.getRemote().sendStringByFuture(artistJSON);
    }

    @OnWebSocketClose
    public void onClose(Session session, int code, String closeReason) {
        logger.info(String.format("Session %s closed because of %s", session, closeReason));
    }
}
