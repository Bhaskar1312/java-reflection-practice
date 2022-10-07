package com.example.reflection.constructors;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DiscoveryAndObjectCreation {
    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        //intro
//        printConstructorsData(Person.class);

        // create obj
        Address address = createInstanceWithArguments(Address.class, "GandhiBomma", 1729);
//       Person person = (Person) createInstanceWithArguments(Person.class);
//        Person person = (Person) createInstanceWithArguments(Person.class, "John", 20);
        Person person = (Person) createInstanceWithArguments(Person.class, address, "John", 20);
        System.out.println(person);
    }

    public static<T> T createInstanceWithArguments(Class<T> clazz, Object... args) // now casting is redundant in caller method
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        for(Constructor<?> constructor: clazz.getDeclaredConstructors()){
            if(constructor.getParameterTypes().length == args.length) {
                return (T) constructor.newInstance(args);
            }
        }
        System.out.println("No appropriate constructor was found");
        return null;
    }
//    public static Object createInstanceWithArguments(Class<?> clazz, Object... args)
//            throws InvocationTargetException, InstantiationException, IllegalAccessException {
//        for(Constructor<?> constructor: clazz.getDeclaredConstructors()){
//            if(constructor.getParameterTypes().length == args.length) {
//                return constructor.newInstance(args);
//            }
//        }
//        System.out.println("No appropriate constructor was found");
//        return null;
//    }
    public static void printConstructorsData(Class<?> clazz) {
//        Constructor<?>[] constructors = clazz.getDeclaredConstructors(); //both public and non-public
        Constructor<?>[] constructors = clazz.getConstructors(); //ony public
        System.out.println(clazz.getSimpleName()+" all constructors start "+constructors.length);

        for(var constructor: constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            List<String> parameterTypeNames = Arrays.stream(parameterTypes)
                    .map(type -> type.getSimpleName())
                    .collect(Collectors.toList());
            System.out.println(parameterTypeNames);
        }
    }
    public static class Person {
        private final Address address;
        private final String name;
        private final int age;

        public Person() {
            this.name = "anonymous";
            this.age = 0;
            this.address = null;
        }

        Person(String name) {
            this.name = name;
            this.age = 0;
            this.address = null;
        }

        protected Person(String name, int age) {
            this.name = name;
            this.age = age;
            this.address = null;
        }

        public Person(Address address, String name, int age) {
            this.address = address;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "address=" + address +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static class Address {
        private String street;
        private int number;

        public Address(String street, int number) {
            this.street = street;
            this.number = number;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", number=" + number +
                    '}';
        }
    }

}
