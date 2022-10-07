package methodinvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Cache {
    private Map<String, Integer> nameToId = new HashMap<>();

    public void invalidate() {
        throw new UnsupportedOperationException(
                "This version of cache does not support invalidation");
    }

    public void addEntry(String name, Integer id) {
        nameToId.put(name, id);
    }

    public Integer readIdOrThrow(String name) throws Exception {
        if (nameToId.containsKey(name)) {
            return nameToId.get(name);
        }

        throw new IllegalArgumentException(String.format("Name: %s is not in the cache", name));
    }

    public int getCacheSize() {
        return nameToId.size();
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Cache cache = new Cache();
        Method readIdOrThrowMethod = Cache.class.getDeclaredMethod("readIdOrThrow", String.class);

        Integer id = (Integer) readIdOrThrowMethod.invoke(cache, "Daniel");
        System.out.println(String.format("Daniel's ID is %s", id));
    }
}