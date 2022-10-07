package fieldannotations;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder(
                                            Arrays.asList("1", "2", "3"),
                                            10,
                                            "Movies",
                                            Arrays.asList("id", "name"));

        String sqlQuery = execute(queryBuilder);

        System.out.println(sqlQuery);
    }

    public static <T> T execute(Object instance) throws InvocationTargetException, IllegalAccessException {
        Class<?> clazz = instance.getClass();

        Map<String, Method> operationToMethod = getOperationToMethod(clazz);
        Map<String, Field> inputToField = getInputToField(clazz);

        Method finalResultMethod = findFinalResultMethod(clazz);

        return (T) executeDependencies(instance, finalResultMethod, operationToMethod, inputToField);
    }

    private static Object executeDependencies(Object instance, Method currentMethod,
                                              Map<String, Method> operationToMethod,
                                              Map<String, Field> inputToField)
            throws InvocationTargetException, IllegalAccessException {
        List<Object> parameterValues = new ArrayList<>(currentMethod.getParameterCount());

        for(Parameter parameter: currentMethod.getParameters()) {
            Object value = null;
            if(parameter.isAnnotationPresent(
                    Annotations.DependOn.class
            )) {
                String dependencyOperationName = parameter.getAnnotation(Annotations.DependOn.class).value();
                Method dependencyMethod = operationToMethod.get(dependencyOperationName);

                value = executeDependencies(instance, dependencyMethod, operationToMethod, inputToField);
            } else if (parameter.isAnnotationPresent(fieldannotations.Annotations.Input.class)) {
                String inputName = parameter.getAnnotation(fieldannotations.Annotations.Input.class).value();
                Field inputField = inputToField.get(inputName);
                inputField.setAccessible(true); //may be private

                value = inputField.get(instance);
            }
            parameterValues.add(value);
        }
        return currentMethod.invoke(instance, parameterValues.toArray());
    }

    private static Map<String, Field> getInputToField(Class<?> clazz) {
        Map<String, Field> inputToField = new HashMap<>();

        for(Field field: clazz.getDeclaredFields()) {
            if(!field.isAnnotationPresent(fieldannotations.Annotations.Input.class)) {
                continue;
            }

            fieldannotations.Annotations.Input input = field.getAnnotation(fieldannotations.Annotations.Input.class);

            inputToField.put(input.value(), field);
        }
        return inputToField;
    }

    private static Map<String, Method> getOperationToMethod(Class<?> clazz) {
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
