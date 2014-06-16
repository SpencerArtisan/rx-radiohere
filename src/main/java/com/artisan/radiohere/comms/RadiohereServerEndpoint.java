package com.artisan.radiohere.comms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.net.ssl.ExtendedSSLSession;
import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.json.JSONObject;

import rx.Observable;

import com.artisan.radiohere.Artist;
import com.artisan.radiohere.ArtistObservableFactory;

@WebServlet(name = "MyEcho WebSocket Servlet", urlPatterns = { "/game" })
public class RadiohereServerEndpoint extends WebSocketServlet {
    private static Logger logger = Logger.getLogger(RadiohereServerEndpoint.class.getName());

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(1000000);
        factory.register(RadiohereServerSocket.class);
    }
    
    public static void main(String[] args) throws Exception{
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ((QueuedThreadPool) server.getThreadPool()).setMaxThreads(10);
        System.out.println(server.getThreadPool());
        context.addServlet(new ServletHolder(new RadiohereServerEndpoint()),"/*");
        server.start();
        server.join();
    }
}
