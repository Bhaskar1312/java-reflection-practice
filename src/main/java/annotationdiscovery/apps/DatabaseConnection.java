package annotationdiscovery.apps;

import annotationdiscovery.InitializerClass;
import annotationdiscovery.InitializerMethod;

@InitializerClass
public class DatabaseConnection {
    @InitializerMethod
    public void connectToDatabase1() {
        System.out.println("Connecting to database 1");
    }

    @InitializerMethod
    public void connectToDatabase2() {
        System.out.println("Connecting to database 2");
    }
}
