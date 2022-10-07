package com.example.reflection.plugin;

import java.util.Arrays;
import java.util.List;

public class ClassAnalyzer {
    private static final List<String> JDK_PACKAGE_PREFIXES =
            Arrays.asList("com.sun.", "java", "javax", "jdk", "org.w3c", "org.xml");

    public static PopupTypeInfo createPopupTypeInfoFromClass(Class<?> inputClass) {
        PopupTypeInfo popupTypeInfo = new PopupTypeInfo();

        /** Complete the Code **/


        popupTypeInfo.setPrimitive(inputClass.isPrimitive()/** Complete the Code **/)
                .setInterface(inputClass.isInterface()/** Complete the Code **/)
                .setEnum(inputClass.isEnum()/** Complete the Code **/)
                .setName(inputClass.getSimpleName()/** Complete the Code **/)
                .setJdk(isJdkClass(inputClass))
                .addAllInheritedClassNames(getAllInheritedClassNames(inputClass));

        return popupTypeInfo;
    }

    /*********** Helper Methods ***************/

    public static boolean isJdkClass(Class<?> inputClass) {
        /** Complete the code
         Hint: What does inputClass.getPackage() return when the class is a primitive type?
         **/
        return JDK_PACKAGE_PREFIXES.stream().anyMatch(packagePrefix ->
                inputClass.getPackage()==null ||
                        inputClass.getPackage().getName().startsWith(packagePrefix));
    }

    public static String[] getAllInheritedClassNames(Class<?> inputClass) {
        /** Complete the code
         Hints: What does inputClass.getSuperclass() return when the inputClass doesn't inherit from any class?
         What does inputClass.getSuperclass() return when the inputClass is a primitve type?

         **/
        String[] inheritedClasses;
        if(inputClass.isInterface()) {
            inheritedClasses = Arrays.stream(inputClass.getInterfaces())
                    .map(Class::getSimpleName)
                    .toArray(String[]::new);
        } else {
            Class<?> inheritedClass = inputClass.getSuperclass();
            inheritedClasses = inheritedClass!=null?
                    new String[]{inheritedClass.getSuperclass().getSimpleName()} : null;

        }
        return inheritedClasses;
    }
}