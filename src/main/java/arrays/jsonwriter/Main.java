package arrays.jsonwriter;

import arrays.data.Actor;
import arrays.data.Movie;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Actor actor1 = new Actor("Elijah Wood", new String[]{"Lord of the Rings", "The Good Son"});
        Actor actor2 = new Actor("Ian McKellen", new String[]{"X-Men", "Hobbit"});
        Actor actor3 = new Actor("Orlando Bloom", new String[]{"Pirates of the Caribbean", "Kingdom of Heaven"});

        Movie movie = new Movie("Lord of the Rings", 8.8f, new String[]{"Action", "Adventure", "Drama"},
                new Actor[]{actor1, actor2, actor3});

        String json = objectToJson(movie, 0);

        System.out.println(json);
    }
    public static String objectToJson(Object instance, int indentSize) throws IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();

        sb.append(indent(indentSize));
        sb.append('{');
        sb.append('\n');

        for(int i=0;i< fields.length;i++) {
            Field field = fields[i];
            field.setAccessible(true);

            if(field.isSynthetic()) {
                continue;
            }

            sb.append(indent(indentSize+1));
            sb.append(formatStringValue(field.getName()));
            sb.append(":");

            if(field.getType().isPrimitive()) {
                sb.append(formatPrimitiveValue(field.get(instance), field.getType()));
            } else if(field.getType().equals(String.class)) {
                sb.append(formatStringValue(field.get(instance).toString()));
            } else if(field.getType().isArray()) {
                sb.append(arrayToJson(field.get(instance), indentSize+1));
            } else {
                sb.append(objectToJson(field.get(instance), indentSize+1));
            }

            if(i!= fields.length-1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(indent(indentSize));
        sb.append("}");
        return sb.toString();
    }

    private static String arrayToJson(Object arrayInstance, int indentSize) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();

        int arrayLength = Array.getLength(arrayInstance);

        Class<?> componentType = arrayInstance.getClass().getComponentType();

        sb.append("{");
        sb.append("\n");

        for(int i=0;i<arrayLength;i++) {
            Object element = Array.get(arrayInstance, i);
            if(componentType.isPrimitive()) {
                sb.append(indent(indentSize+1));
                sb.append(formatPrimitiveValue(element, componentType));
            } else if(componentType.equals(String.class)) {
                sb.append(indent(indentSize+1));
                sb.append(formatStringValue(element.toString()));
            } else {
                sb.append(objectToJson(element, indentSize+1));
            }
            if(i!= arrayLength-1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append(indent(indentSize));
        sb.append("}");
        return sb.toString();
    }

    private static String indent(int indentSize) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<indentSize;i++) sb.append('\t');
        return sb.toString();
    }

    private static String formatPrimitiveValue(Object instance, Class<?> type) {
        if(type.equals(boolean.class) || type.equals(int.class) || type.equals(long.class)
        || type.equals(short.class)) return instance.toString();
        else if(type.equals(double.class) || type.equals(float.class)) {
            return String.format("%.02f", instance);
        }
        throw new RuntimeException(String.format("Type: %s is unsupported", type.getTypeName()));
    }

    private static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }
}
