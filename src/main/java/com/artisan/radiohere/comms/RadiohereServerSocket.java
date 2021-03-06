package com.artisan.radiohere.comms;

import java.util.logging.Logger;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import rx.Observable;

import com.artisan.radiohere.Coordinate;
import com.artisan.radiohere.Gig;
import com.artisan.radiohere.GigFactory;

@WebSocket
public class RadiohereServerSocket {
    private static Logger logger = Logger.getLogger(RadiohereServerSocket.class.getName());

    @OnWebSocketConnect
    public void onOpen(Session session) {
        logger.info("Client opens Radiohere websocket... " + session);
    }
    
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
    		logger.info("** Received message from client: " + message);
    		String[] args = message.replaceAll("\"", "").split(",");
    		Coordinate origin = new Coordinate(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
    		Double distance = Double.parseDouble(args[2]);
    		logger.info("Retrieving gigs within " + distance + "km of " + origin);
    		
    		Observable<Gig> factory = new GigFactory().create(origin, distance);
    		factory.subscribe((gig) -> sendToClient(session, gig),
    				(e) -> logger.warning("STREAM ERROR: " + e.getMessage()),
    				() -> logger.info("STREAM FINISHED"));
    }

    	private void sendToClient(Session session, Gig gig) {
    		logger.info("Sending to client...");
		String gigJSON = new JSONObject(gig).toString();
        logger.info("Server sending artist to client... " + gigJSON);
		session.getRemote().sendStringByFuture(gigJSON);
    }

    @OnWebSocketClose
    public void onClose(Session session, int code, String closeReason) {
        logger.info(String.format("Session %s closed because of %s", session, closeReason));
    }
}
