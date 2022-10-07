package fields.data;

import java.lang.reflect.Field;

public class ObjectSizeCalculator {
    private static final long HEADER_SIZE = 12;
    private static final long REFERENCE_SIZE = 4;

    public long sizeOfObject(Object input) throws IllegalAccessException {
        /**
         * Complete your code here
         */
        long ans = HEADER_SIZE + REFERENCE_SIZE;
        for(Field field: input.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.getType().isPrimitive()) {
                ans += sizeOfPrimitiveType(field.getType());
            } else {
                ans += sizeOfString((String) field.get(input));
            }
        }
        return ans;
    }


    /*************** Helper methods ********************************/
    private long sizeOfPrimitiveType(Class<?> primitiveType) {
        if (primitiveType.equals(int.class)) {
            return 4;
        } else if (primitiveType.equals(long.class)) {
            return 8;
        } else if (primitiveType.equals(float.class)) {
            return 4;
        } else if (primitiveType.equals(double.class)) {
            return 8;
        } else if (primitiveType.equals(byte.class)) {
            return 1;
        } else if (primitiveType.equals(short.class)) {
            return 2;
        }
        throw new IllegalArgumentException(String.format("Type: %s is not supported", primitiveType));
    }

    private long sizeOfString(String inputString) {
        int stringBytesSize = inputString.getBytes().length;
        return HEADER_SIZE + REFERENCE_SIZE + stringBytesSize;
    }

    public static void main(String[] args) throws IllegalAccessException {
        AccountSummary accountSummary = new AccountSummary("John", "Smith", (short) 20, 100_000);
        ObjectSizeCalculator ob = new ObjectSizeCalculator();
        System.out.println(ob.sizeOfObject(accountSummary));
//        int x = 2;
//        System.out.println(ob.sizeOfObject(2));
    }
}