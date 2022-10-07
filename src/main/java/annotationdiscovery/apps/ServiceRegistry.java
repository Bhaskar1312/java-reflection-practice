package annotationdiscovery.apps;

import annotationdiscovery.InitializerClass;
import annotationdiscovery.InitializerMethod;

@InitializerClass
public class ServiceRegistry {

    @InitializerMethod
    public void registerService() {
        System.out.println("Service successfully registered");
    }
}
