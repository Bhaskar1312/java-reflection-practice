package com.example.reflection.intro;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        // Class<?> is the introduction to reflection's entrypoint

        //Method 2  .class suffix, even primitives
        Class<String> stringClass = String.class;

        // Method -1 object.getClass(), for primitives, primitive.getClass() - compilation error
        Map<String, Integer> mapObject = new TreeMap<>();
        Class<?> mapClass = mapObject.getClass();//TreeMap class, not Map Interface

        int x = 1;
        Class<?> intClass = int.class;

        // Method 3 Class.forName(...) dynamic lookup for any classpath, for primitives -Runtime error
        // Mistyping name may result in ClassNotFoundException, least safe among 3 methods
        // use case - to define class from user defined runtime like (no need to recompile), just change text files, bean-ampping, log.xml
//         <bean id="vehicle" class = "vehicles.Car"><property name = "noOfWheels" value="4" /></bean>
//        class not part of project, jar available
        Class<?> squareClass = Class.forName("com.example.reflection.intro.Main$Square"); // at Runtime, fully qualified name

        printClassInfo(stringClass, mapClass, squareClass, intClass, squareClass);

        printClassInfo(Collections.class, boolean.class, int[][].class, Color.class);

        var circleObject = new Drawable() {

            @Override
            public int getNumberOfCorners() {
                return 0x7fffffff;
            }
        }.getClass();
        printClassInfo(circleObject);

//        x = 5;
//        Class<?> clazz = x.getClass(); //compilation error
//        System.out.println(clazz.getTypeName());
        // Only objects have the getClass() method

        //since the Builder class is not public, we have to use the Class.forName(..) method.
        Class<?> clazz = Class.forName("com.example.reflection.intro.Product$Builder");
        printClassInfo(clazz);
    }

    private static void printClassInfo(Class<?>... classes) {
        for(Class<?> clazz: classes) {
            System.out.println(String.format("class name: %s, package name: %s",
                            clazz.getSimpleName(),
                            clazz.getPackageName()));
            Class<?>[] implementedInterfaces = clazz.getInterfaces();

            for(Class<?> implementedInterface: implementedInterfaces) {
                System.out.println(String.format("class %s implements interface: %s",
                                clazz.getSimpleName(),
                                implementedInterface.getSimpleName()));
            }

            System.out.println("Is array "+clazz.isArray());
            System.out.println("Is primitive "+clazz.isPrimitive());
            System.out.println("Is enum "+clazz.isEnum());
            System.out.println("Is interface "+clazz.isInterface());
            System.out.println("Is anonymous "+clazz.isAnonymousClass());

            System.out.println();

        }
    }

    private static class Square implements Drawable {
        @Override
        public int getNumberOfCorners() {
            return 4;
        }
    }

    private static interface Drawable {
        int getNumberOfCorners();
    }

    private enum Color {
        BLUE,
        RED,
        GREEN
    }
}

abstract class Product {
    /// ...

    static class Builder {

        /// ...
    }
}
