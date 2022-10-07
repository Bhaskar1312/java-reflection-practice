package com.example.reflection.constructors;


import java.net.InetSocketAddress;

public class ServerConfiguration {
    //use singleton pattern, only one instance
    private static ServerConfiguration serverConfigurationInstance;

    public static ServerConfiguration getInstance() {
        return serverConfigurationInstance; // this way not only singleton, but also immutable
    }

    private final InetSocketAddress serverAddress;
    private final String greetingMessage;

    private ServerConfiguration(int port, String greetingMessage) {
        this.serverAddress = new InetSocketAddress("localhost", port);
        this.greetingMessage = greetingMessage;
        if(serverConfigurationInstance == null) {
            serverConfigurationInstance = this;
        }
    }
    public InetSocketAddress getServerAddress() {
        return this.serverAddress;
    }
    public String getGreetingMessage() {
        return this.greetingMessage;
    }
}
