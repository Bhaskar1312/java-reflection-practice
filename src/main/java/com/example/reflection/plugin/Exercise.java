package com.example.reflection.plugin;

import java.util.*;

public class Exercise {

    static void findInterfaces(Class<?> input, Set<Class<?>> set) {
        Class<?>[] interfaces = input.getInterfaces();
        if(interfaces.length == 0) {
            return;
        } else {
            for(Class<?> clazz: interfaces) {
                set.add(clazz);
                findInterfaces(clazz, set);
            }
        }
    }
    /**
     * Returns all the interfaces that the current input class implements.
     * Note: If the input is an interface itself, the method returns all the interfaces the
     * input interface extends.
     */
    public static Set<Class<?>> findAllImplementedInterfaces(Class<?> input) {
        Set<Class<?>> allImplementedInterfaces = new HashSet<>();

        findInterfaces(input, allImplementedInterfaces);

        return allImplementedInterfaces;
    }
}