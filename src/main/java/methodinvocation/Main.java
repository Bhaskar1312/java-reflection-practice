package methodinvocation;

import methodinvocation.db.DatabaseClient;
import methodinvocation.http.HttpClient;
import methodinvocation.logging.FileLogger;
import methodinvocation.udp.UdpClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Throwable {
        DatabaseClient databaseClient = new DatabaseClient();
        HttpClient httpClient1 = new HttpClient("123.456.78.9");
        HttpClient httpClient2 = new HttpClient("23.456.78.90");
        FileLogger fileLogger = new FileLogger();
        UdpClient udpClient = new UdpClient();

        String requestBody = "request body";

        List<Class<?>> methodParameterTypes = Arrays.asList(new Class<?>[]{String.class});

        Map<Object, Method> requestExecutors =
                groupExecutors(
                        Arrays.asList(databaseClient, httpClient1, httpClient2, fileLogger, udpClient),
                        methodParameterTypes);
        executeAll(requestExecutors, requestBody);
    }

    public static void executeAll(Map<Object, Method> requestExecutors, String requestBody) throws Throwable {

        try {
            for (Map.Entry<Object, Method> requestExecutorEntry : requestExecutors.entrySet()) {

                Object requestExecutor = requestExecutorEntry.getKey();
                Method method = requestExecutorEntry.getValue();

                Boolean result = (Boolean) method.invoke(requestExecutor, requestBody);

                if (result != null && result.equals(Boolean.FALSE)) {
                    System.out.println("Received fail. Aborting...");
                    return;
                }
            }
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    public static Map<Object, Method> groupExecutors(List<Object> requestExecutors, List<Class<?>> methodParameterTypes) {
        Map<Object, Method> instanceToMethod = new LinkedHashMap<>();

        for(Object requestExecutor: requestExecutors) {
            Method[] allMethods = requestExecutor.getClass().getDeclaredMethods();

            for(Method method: allMethods) {
                if(Arrays.asList(method.getParameterTypes()).equals(methodParameterTypes)) {
                    instanceToMethod.put(requestExecutor, method);
                }
            }
        }
        return instanceToMethod;
    }
}
