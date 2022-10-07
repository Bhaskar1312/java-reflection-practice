package dynamicproxy;

import dynamicproxy.external.DatabaseReader;
import dynamicproxy.external.HttpClient;
import dynamicproxy.external.impl.DatabaseReaderImpl;
import dynamicproxy.external.impl.HttpClientImpl;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        HttpClient httpClient = new HttpClientImpl();
//        DatabaseReader databaseReader = new DatabaseReaderImpl();
        HttpClient httpClient = createProxy(new HttpClientImpl());
        DatabaseReader databaseReader = createProxy(new DatabaseReaderImpl());

//        useHttpClient(httpClient);
//        useDatabaseReader(databaseReader);

        List<Integer> list = createProxy(new ArrayList<>());
        list.add(1);list.add(2);
    }
    public static class TimeMeasuringProxyHandler implements InvocationHandler {

        private final Object originalObject;

        public TimeMeasuringProxyHandler(Object originalObject) {
            this.originalObject = originalObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result;

            System.out.println(String.format("Measuring Proxy - Before Executing method : %s()", method.getName()));
            long startTime = System.currentTimeMillis();
            try {
                result = method.invoke(originalObject, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
            long endTime = System.currentTimeMillis();

            System.out.println();
            System.out.println(String.format("Measuring Proxy - Execution of %s() took %dns \n", method.getName(), endTime - startTime));

            return result;
        }
    }

    public static <T> T createProxy(Object originalObject) {
        Class<?>[] allImplementedInterfaces = originalObject.getClass().getInterfaces();

        TimeMeasuringProxyHandler timeMeasuringProxyHandler = new TimeMeasuringProxyHandler(originalObject);

        return (T) Proxy.newProxyInstance(
                originalObject.getClass().getClassLoader(),
                allImplementedInterfaces,
                timeMeasuringProxyHandler
        );
    }

    public static void useHttpClient(HttpClient httpClient) {
        httpClient.initialize();
        String response = httpClient.sendRequest("some request");

        System.out.println(String.format("Http response is : %s", response));
    }

    public static void useDatabaseReader(DatabaseReader databaseReader) throws InterruptedException {
        int rowsInGamesTable = 0;
        try {
            rowsInGamesTable = databaseReader.countRowsInTable("GamesTable");
        } catch (IOException e) {
            System.out.println("Catching exception " + e);
            return;
        }

        System.out.println(String.format("There are %s rows in GamesTable", rowsInGamesTable));

        String[] data = databaseReader.readRow("SELECT * from GamesTable");

        System.out.println(String.format("Received %s", String.join(" , ", data)));
    }
}
