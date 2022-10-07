package dynamicproxy.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachingInvocationHandler implements InvocationHandler {

    /**
     * Map that maps from a method name to a method cache
     * Each cache is a map from a list of arguments to a method result
     */
    private final Map<String, Map<List<Object>, Object>> cache = new HashMap<>();

    /**
     * Add any additonal member variables here
     */
    private final Object originalObject;
    public CachingInvocationHandler(Object realObject) {
        /**
         * Complete your code here
         */
        originalObject = realObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**
         * Complete your code here
         */
        if(isMethodCacheable(method) && isInCache(method, args)) {
            return getFromCache(method, args);
        } else {
            Object result;
            try {
                result= method.invoke(originalObject, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
            if (!isInCache(method, args)) {
                putInCache(method, args, result);
            }
            return result;
        }



    }

    boolean isMethodCacheable(Method method) {
        /**
         * Complete your code here
         */
        return method.isAnnotationPresent(Cacheable.class);
    }

    /******************************* Helper Methods **************************/

    private boolean isInCache(Method method, Object[] args) {
        if (!cache.containsKey(method.getName())) {
            return false;
        }
        List<Object> argumentsList = Arrays.asList(args);

        Map<List<Object>, Object> argumentsToReturnValue = cache.get(method.getName());

        return argumentsToReturnValue.containsKey(argumentsList);
    }

    private void putInCache(Method method, Object[] args, Object result) {
        if (!cache.containsKey(method.getName())) {
            cache.put(method.getName(), new HashMap<>());
        }
        List<Object> argumentsList = Arrays.asList(args);

        Map<List<Object>, Object> argumentsToReturnValue = cache.get(method.getName());

        argumentsToReturnValue.put(argumentsList, result);
    }

    private Object getFromCache(Method method, Object[] args) {
        if (!cache.containsKey(method.getName())) {
            throw new IllegalStateException(String.format("Result of method: %s is not in the cache", method.getName()));
        }

        List<Object> argumentsList = Arrays.asList(args);

        Map<List<Object>, Object> argumentsToReturnValue = cache.get(method.getName());

        if (!argumentsToReturnValue.containsKey(argumentsList)) {
            throw new IllegalStateException(String.format("Result of method: %s and arguments: %s is not in the cache",
                    method.getName(),
                    argumentsList));
        }

        return argumentsToReturnValue.get(argumentsList);
    }
}