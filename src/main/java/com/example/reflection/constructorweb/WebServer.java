package com.example.reflection.constructorweb;

import com.example.reflection.constructors.ServerConfiguration;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;


public class WebServer {
    public void startServer() throws IOException {
        HttpServer httpServer = HttpServer.create(ServerConfiguration.getInstance().getServerAddress(), 0);
        httpServer.createContext("greeting").setHandler(exchange -> {

            String responseMessage = ServerConfiguration.getInstance().getGreetingMessage();

            exchange.sendResponseHeaders(200, responseMessage.length());

            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseMessage.getBytes());
            responseBody.flush();
            responseBody.close();
        });

        System.out.println(String.format("Starting server on address %s:%d",
                ServerConfiguration.getInstance().getServerAddress().getHostName(),
                ServerConfiguration.getInstance().getServerAddress().getPort()));
        httpServer.start();
    }
}
