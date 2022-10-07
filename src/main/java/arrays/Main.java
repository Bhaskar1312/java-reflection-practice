package arrays;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] oneDimensionalArray = {1, 2};
        inspectArrayObject(oneDimensionalArray);
        double[][][] twoDimensionalArray = {{{1.5, 2,5}, {1.0, 2,0}, {3.5, 4.5}}, {{1,2,3}, {1,2}}, {{1,2,3}}};
        inspectArrayObject(twoDimensionalArray);

        inspectArrayValues(twoDimensionalArray);
        inspectArrayValues(oneDimensionalArray);
    }


    public static void inspectArrayValues(Object arrayObject) {
        int arrayLength = Array.getLength(arrayObject);

        System.out.print('{');
        for(int i=0;i<arrayLength;i++) {
            Object element = Array.get(arrayObject, i);

            if(element.getClass().isArray()) {
                inspectArrayValues(element);
            } else {
                System.out.print(element);
            }

            if(i != arrayLength-1) {
                System.out.print(" , ");
            }
        }
        System.out.print('}');
    }

    public static void inspectArrayObject(Object arrayObject) {
        Class<?> clazz = arrayObject.getClass();

        System.out.printf("Is array : %s\n", clazz.isArray());

        Class<?> arrayComponent = clazz.getComponentType();

        System.out.printf("This is an array of type: %s\n", arrayComponent.getTypeName());

    }
}
