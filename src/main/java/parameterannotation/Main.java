package parameterannotation;

import parameterannotation.annotations.Annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        BestGamesFinder finder = new BestGamesFinder();

        List<String> bestGamesInDescendingOrder = execute(finder);

        System.out.println(bestGamesInDescendingOrder);
    }

    public static <T> T execute(Object instance) throws InvocationTargetException, IllegalAccessException {
        Class<?> clazz = instance.getClass();

        Map<String, Method> operationToMethod = getOperationTOMethod(clazz);
        Method finalResultMethod = findFinalResultMethod(clazz);

        return (T) executeDependencies(instance, finalResultMethod, operationToMethod);
    }

    private static Object executeDependencies(Object instance, Method currentMethod, Map<String, Method> operationToMethod) throws InvocationTargetException, IllegalAccessException {
        List<Object> parameterValues = new ArrayList<>(currentMethod.getParameterCount());

        for(Parameter parameter: currentMethod.getParameters()) {
            Object value = null;
            if(parameter.isAnnotationPresent(
                    Annotations.DependOn.class
            )) {
                String dependencyOperationName = parameter.getAnnotation(Annotations.DependOn.class).value();
                Method dependencyMethod = operationToMethod.get(dependencyOperationName);

                value = executeDependencies(instance, dependencyMethod, operationToMethod);
            }
            parameterValues.add(value);
        }
        return currentMethod.invoke(instance, parameterValues.toArray());
    }

    private static Map<String, Method> getOperationTOMethod(Class<?> clazz) {
        Map<String, Method> operationNameToMethod = new HashMap<>();

        for(Method method: clazz.getDeclaredMethods()) {
            if(!method.isAnnotationPresent(Annotations.Operation.class)) {
                continue;
            }

            Annotations.Operation operation = method.getAnnotation(Annotations.Operation.class);

            operationNameToMethod.put(operation.value(), method);
        }
        return operationNameToMethod;
    }
    private static Method findFinalResultMethod(Class<?> clazz) {
        for(Method method: clazz.getDeclaredMethods()) {
            if(method.isAnnotationPresent(Annotations.FinalResult.class)) {
                return method;
            }
        }
        throw new RuntimeException("No method found with finalResult annotation");
    }
}
