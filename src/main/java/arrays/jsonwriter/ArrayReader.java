package arrays.jsonwriter;

import java.lang.reflect.*;
public class ArrayReader {

    public Object getArrayElement(Object array, int index) {
        /**
         * Complete your code here
         */
        int arrayLength = Array.getLength(array);
        while(index <0) index += arrayLength;
        return Array.get(array, index);
    }
}