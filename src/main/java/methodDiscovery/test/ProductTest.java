package methodDiscovery.test;

import javafx.scene.chart.LineChart;
import methodDiscovery.api.ClothingProduct;
import methodDiscovery.api.Product;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ProductTest {
    public static void main(String[] args) throws NoSuchMethodException {
//        testGetters(Product.class);
//        testSetters(Product.class);

        testGetters(ClothingProduct.class);
        testSetters(ClothingProduct.class);
    }

    public static void testSetters(Class<?> dataClass) throws NoSuchMethodException {
        List<Field> fields = getAllMethods(dataClass);

        Map<String, Method> methodNameToMethod =  mapMethodNameToMethod(dataClass);

        for(Field field: fields) {
            String setterName = "set"+ capitalizeFirstLetter(field.getName());

            if(!methodNameToMethod.containsKey(setterName)) {
                throw new IllegalStateException(
                        String.format("Field : %s doesn't have a setter method",
                                field.getName()));
            }

            Method setter = null;
            try {
                setter = dataClass.getMethod(setterName, field.getType());
            }catch (NoSuchMethodException e) {
                throw new IllegalStateException(String.format("Setter : %s not found", setterName));
            }

            if(!setter.getReturnType().equals(void.class)) {
                throw new IllegalStateException(String.format("Setter method: %s has to return void"));
            }

        }
    }
    public static void testGetters(Class<?> dataClass) {
        List<Field> fields = getAllMethods(dataClass);

        Map<String, Method> methodNameToMethod =  mapMethodNameToMethod(dataClass);

        for(Field field: fields) {
            String getterName = "get" + capitalizeFirstLetter(field.getName());

            if(!methodNameToMethod.containsKey(getterName)) {
                throw new IllegalStateException(
                        String.format("Field : %s doesn't have a getter method",
                                field.getName()));
            }

            Method getter = methodNameToMethod.get(getterName);

            if(!getter.getReturnType().equals(field.getType())) {
                throw new IllegalStateException(
                        String.format("Getter method : %s() has return type %s but expected %s",
                                getter.getName(),
                                getter.getReturnType().getTypeName(),
                                field.getType().getTypeName()));
            }

            if(getter.getParameterCount() > 1) {
                throw new IllegalStateException(String.format("Getter : %s has %d arguments", getterName));
            }
        }

    }

    private static Map<String, Method> mapMethodNameToMethod(Class<?> dataClass) {
        Method[] methods = dataClass.getMethods();

        Map<String, Method> nametoMethod = new HashMap<>();

        for(Method method: methods) {
            nametoMethod.put(method.getName(), method);
        }
        return nametoMethod;
    }

    private static List<Field> getAllMethods(Class<?> clazz) {
        if(clazz==null || clazz.equals(Object.class)) {
            return Collections.emptyList();
        }

        Field[] currentClassFields = clazz.getDeclaredFields();

        List<Field> inheritedFields = getAllMethods(clazz.getSuperclass());

        List<Field> allFields = new ArrayList<>();

        allFields.addAll(Arrays.asList(currentClassFields));
        allFields.addAll(inheritedFields);

        return allFields;
    }
    private static String capitalizeFirstLetter(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1));
    }
}
