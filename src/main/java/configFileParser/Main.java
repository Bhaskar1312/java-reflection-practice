package configFileParser;

import configFileParser.data.GameConfig;
import configFileParser.data.UserInterfaceConfig;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    private static final Path GAME_CONFIG_PATH = Path.of("resources/configparser/game-properties.cfg");
    private static final Path UI_CONFIG_PATH = Path.of("resources/configparser/user-interface.cfg");

    public static void main(String[] args) throws IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GameConfig gameConfig = createConfigObject(GameConfig.class, GAME_CONFIG_PATH);
        System.out.println(gameConfig);

        UserInterfaceConfig userInterfaceConfig = createConfigObject(UserInterfaceConfig.class, UI_CONFIG_PATH);
        System.out.println(userInterfaceConfig);
    }

    public static <T> T createConfigObject(Class<T> clazz, Path filePath) throws
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException, IOException {
        Scanner sc = new Scanner(filePath);

        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        T configInstance = (T)constructor.newInstance();

        while(sc.hasNextLine()) {
            String configLine = sc.nextLine();

            String[] nameValuePair = configLine.split("=");
            if(nameValuePair.length !=2) continue;

            String propertyName = nameValuePair[0];
            String propertyValue = nameValuePair[1];

            Field field;
            try {
                field = clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException ex) {
                System.out.println(String.format("Property name: %s is unsupported", propertyName));
                continue;
            }

            field.setAccessible(true);

            Object parsedValue = parseValue(field.getType(), propertyValue);

            field.set(configInstance, parsedValue);

        }
        return configInstance;
    }

    private static Object parseValue(Class<?> type, String value) {
//        System.out.println(type+" "+value);
        if(type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(short.class)) {
            return Short.parseShort(value);
        } else if (type.equals(long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (type.equals(String.class)) {
            return value;
        }
        // add support for date, time etc..
        throw new RuntimeException(String.format("Type : %s unsupported", type.getTypeName()));
    }
}
