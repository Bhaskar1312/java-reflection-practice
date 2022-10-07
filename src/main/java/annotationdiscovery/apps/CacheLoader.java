package annotationdiscovery.apps;

import annotationdiscovery.InitializerClass;
import annotationdiscovery.InitializerMethod;

@InitializerClass
public class CacheLoader {

    @InitializerMethod
    public void loadCache() {
        System.out.println("Loading data from cache");
    }

    public void reloadCache() {
        System.out.println("Reload Cache");
    }
}
