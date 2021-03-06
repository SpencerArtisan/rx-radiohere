package com.artisan.radiohere.comms;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.servlet.*;

@WebServlet(name = "MyEcho WebSocket Servlet", urlPatterns = { "/game" })
public class RadiohereServerEndpoint extends WebSocketServlet {
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
