package annotationdiscovery;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise {

    /**
     * Complete your code here if necessary
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface OpenResources {
        /**
         * Complete your code here if necessary
         */
    }

    public Set<Method> getAllAnnotatedMethods(Object input) {
        Set<Method> annotatedMethods = new HashSet<>();
        Method[] methods = input.getClass().getDeclaredMethods();
        for(Method method: methods) {
            if(method.isAnnotationPresent(OpenResources.class)) {
                annotatedMethods.add(method);
            }
        }
        /**
         * Complete your code here
         */
        return annotatedMethods;
    }
}