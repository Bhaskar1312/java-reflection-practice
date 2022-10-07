package utilities;

import internal.ImageBuffer;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ReflectiveFactory {

    public static <T> T createObject(Class<T> clazz, Object ... args) throws Throwable {
        Class<?>[] parameterTypes = Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class[]::new);

        Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);

        return (T) constructor.newInstance(args);
    }


        public static void main(String[] args) throws Throwable {
            System.out.println(createObject(ImageBuffer.class, 200));
        }

}