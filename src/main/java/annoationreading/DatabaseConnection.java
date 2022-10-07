package annoationreading;

import annotationdiscovery.InitializerClass;
import annotationdiscovery.InitializerMethod;

import java.io.IOException;

@InitializerClass
public class DatabaseConnection {
    private int failCounter = 5;

    @RetryOperation(
            numberOfRetries = 10,
            retryExceptions = Exception.class,
            durationBetweenRetriesMs = 1000,
            failureMessage = "Connection to database 1 failed")
    @InitializerMethod
    public void connectToDatabase1() throws IOException {
        System.out.println("Connecting to database 1");
        if (failCounter > 0) {
            failCounter--;
            throw new IOException("Connection failed");
        }

        System.out.println("Connection to database 1 succeeded");
    }

    @InitializerMethod
    public void connectToDatabase2() {
        System.out.println("Connecting to database 2");
    }
}
