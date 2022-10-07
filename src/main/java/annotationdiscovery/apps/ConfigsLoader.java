package annotationdiscovery.apps;

import annotationdiscovery.InitializerClass;
import annotationdiscovery.InitializerMethod;

@InitializerClass
public class ConfigsLoader {

    @InitializerMethod
    public void loadAllConfigs() {
        System.out.println("Loading all configuration files");
    }
}
