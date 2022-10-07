package com.example.reflection.plugin;

public class Test {
    public static void main(String[] args) {
        Class<?> clazz = int.class;
        System.out.println(clazz.getPackageName());
        System.out.println(ClassAnalyzer.getAllInheritedClassNames(clazz));
    }
}
