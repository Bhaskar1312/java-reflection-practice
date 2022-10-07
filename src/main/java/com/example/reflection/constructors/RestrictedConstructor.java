package com.example.reflection.constructors;

import com.example.reflection.constructorweb.WebServer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// not working, giving an errors
public class RestrictedConstructor {
    public static void main(String[] args) throws
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        initConfiguration();

        WebServer webServer = new WebServer();
        webServer.startServer();
    }
    public static void initConfiguration() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<ServerConfiguration> constructor = ServerConfiguration.class.getDeclaredConstructor(int.class, String.class);

        // for private constructor, set accessible to true
        constructor.setAccessible(true);
        constructor.newInstance(8080, "Welcome!!!");
    }
}
