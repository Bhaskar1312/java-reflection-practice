package configFileParser2;

import java.util.*;
import java.lang.reflect.*;

public class ArrayFlattening {

    public static<T> T concat(Class<?> type, Object... arguments) {

        if (arguments.length == 0) {
            return null;
        }

        /**
         * Complete code here
         */
            List<Object> list = new ArrayList<>();

            for (int i = 0; i < arguments.length; i++) {
                final Class<?> clazz = arguments[i].getClass();
                if (clazz.isArray()) {
                    int elementArrayLength = Array.getLength(arguments[i]);

                    for (int j = 0; j < elementArrayLength; j++) {
                        list.add(Array.get(arguments[i], j));
                    }

                } else {
                    list.add(arguments[i]);
                }
            }

            Object correctArray = Array.newInstance(type, list.size());
            for (int j = 0; j < list.size(); j++) {
                Array.set(correctArray, j, list.get(j));
            }

            return (T) correctArray;

    }

    public static void main(String[] args) {
        int [] result = concat(int.class, 1, 2, 3, new int[] {4, 5, 6}, 7);
        System.out.println(Arrays.toString(result));
        int [] result2 = concat(String.class, new String[]{"a", "b"}, "c", new String[] {"d", "e"});
        System.out.println(Arrays.toString(result2));
    }
}
