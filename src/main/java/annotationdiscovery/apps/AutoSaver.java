package annotationdiscovery.apps;

import annotationdiscovery.InitializerClass;
import annotationdiscovery.InitializerMethod;

@InitializerClass
public class AutoSaver {

    @InitializerMethod
    public void startAutoSavingThreads() {
        System.out.println("Start automatic data saving to disk");
    }
}