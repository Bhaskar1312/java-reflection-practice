package fields.jsonwriter;

import com.example.reflection.constructors.DiscoveryAndObjectCreation;
import fields.data.Address;
import fields.data.Company;
import fields.data.Person;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Address address = new Address("Gandhi street", (short) 75);
        String json = objectToJson(address);
        System.out.println(json);

        Company company = new Company("BNY Mellon", "San Jose",  address);
        Person person = new Person("John", true, 29, 100.335f, address, company);
        json = objectToJson(person);
        System.out.println(json);
//        int x = 1;
//        System.out.println(objectToJson(x));
    }

    public static String objectToJson(Object instance) throws IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();

        sb.append('{');

        for(int i=0;i< fields.length;i++) {
            Field field = fields[i];
            field.setAccessible(true);

            if(field.isSynthetic()) {
                //compiler generated
                continue;
            }
            sb.append(formattedStringValue(field.getName()));
            sb.append(':');

            if(field.getType().isPrimitive()) {
                sb.append(formatPrimitiveValue(field, instance));
            } else if(field.getType().equals(String.class)) {
                sb.append(formattedStringValue(field.get(instance).toString()));
            } else {
                sb.append(objectToJson(field.get(instance)));
            }

            if(i != fields.length-1) {
                sb.append(',');
            }
        }
        sb.append('}');
        return sb.toString();
    }

    private static String formatPrimitiveValue(Field field, Object parentInstance) throws IllegalAccessException {
        if(field.getType().equals(boolean.class)
                || field.getType().equals(int.class)
                || field.getType().equals(long.class)
                || field.getType().equals(short.class) ) {
            return field.get(parentInstance).toString();
        } else if (field.getType().equals(float.class)
                || field.getType().equals(double.class) ) {
            return String.format("%.02f", field.get(parentInstance));
        }
        // byte or char
        throw new RuntimeException(String.format("Type : %s is unsupported", field.getType().getName()));
    }
    private static String formattedStringValue(String val) {
        return String.format("\"%s\"", val);
    }
}
