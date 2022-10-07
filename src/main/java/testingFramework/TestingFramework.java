package testingFramework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestingFramework {
    public void runTestSuite(Class<?> testClass) throws Throwable {
        /**
         * Complete your code here
         */
        Method[] methods = testClass.getMethods(); // public/protected

        Method beforeClassMethod = findMethodByName(methods, "beforeClass");

        if(beforeClassMethod != null) {
            beforeClassMethod.invoke(null);
        }

        Method beforeEachTestMethod = findMethodByName(methods, "setupTest");

        List<Method> testMethods = findMethodsByPrefix(methods, "test");

        for(Method test: testMethods) {
            Object testObject = testClass.getDeclaredConstructor().newInstance(); //default constructor
            if(beforeEachTestMethod != null){
                beforeEachTestMethod.invoke(testObject);
            }
            test.invoke(testObject);
        }

        Method afterClassMethod = findMethodByName(methods, "afterClass");
        if(afterClassMethod != null) {
            afterClassMethod.invoke(null);
        }
    }

    /**
     * Helper method to find a method by name
     * Returns null if a method with the given name does not exist
     */
    private Method findMethodByName(Method[] methods, String name) {
        /**
         * Complete your code here
         */
        for(Method method: methods) {
            if(method.getName().equals(name)
                && method.getParameterCount() == 0
                && method.getReturnType() == void.class) {
                return method;
            }
        }
        return null;
    }

    /**
     * Helper method to find all the methods that start with the given prefix
     */
    private List<Method> findMethodsByPrefix(Method[] methods, String prefix) {
        /**
         * Complete your code here
         */
        List<Method> matchedMethods = new ArrayList<>();
        for(Method method: methods) {
            if(method.getName().startsWith(prefix)
                    && method.getParameterCount() == 0
                    && method.getReturnType() == void.class) {
                matchedMethods.add(method);
            }
        }
        return matchedMethods;
    }

    public static void main(String[] args) throws Throwable {
        TestingFramework tf = new TestingFramework();
        tf.runTestSuite(PaymentServiceTest.class);
    }

}