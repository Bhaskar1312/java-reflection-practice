package annoationreading;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ScanPackages({"annoationdiscovery.apps"})
public class Main {
    public static void main(String[] args) throws Throwable {
        initialize();
    }

    private static List<Method> getAllInitialingMethods(Class<?> clazz) {
        List<Method> initializingMethods = new ArrayList<>();
        for(Method method: clazz.getDeclaredMethods()) {
            if(method.isAnnotationPresent(InitializerMethod.class)) {
                initializingMethods.add(method);
            }
        }
        return initializingMethods;
    }

    public static void initialize() throws Throwable {
        ScanPackages scanPackages = Main.class.getAnnotation(ScanPackages.class);
        if(scanPackages == null || scanPackages.value().length == 0) return;

        List<Class<?>> classes = getAllClasses(scanPackages.value());

        for(Class<?> clazz: classes) {
            if(!clazz.isAnnotationPresent(InitializerClass.class)) {
                continue;
            }
            List<Method> methods = getAllInitialingMethods(clazz);

            Object instance = clazz.getDeclaredConstructor().newInstance();

            for(Method method: methods) {
                callInitializingMethod(instance, method);
            }
        }
    }

    private static void callInitializingMethod(Object instance, Method method) throws Throwable{
        RetryOperation retryOperation = method.getAnnotation(RetryOperation.class);

        int numberOfRetries = retryOperation==null? 0: retryOperation.numberOfRetries(); //additional retries after init fails

        while (true) {
            try {
                method.invoke(instance);
                break;
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();
                if(numberOfRetries >0 && Set.of(retryOperation.retryExceptions()).contains(targetException.getClass())) {
                    numberOfRetries--;

                    System.out.println("Retrying...");
                    Thread.sleep(retryOperation.durationBetweenRetriesMs());
                } else if(retryOperation != null) { // numberOfRetries =0
                    throw new Exception(retryOperation.failureMessage(), targetException);
                } else {
                    throw targetException;
                }
            }
        }

    }

    private static List<Class<?>> getAllClasses(String[] packageNames) throws URISyntaxException, IOException, ClassNotFoundException {
        List<Class<?>> allClasses = new ArrayList<>();

        for(String packageName : packageNames) {
            String packageRelativePath = packageName.replace('.', '/');

            URI packageUri = Main.class.getResource(packageRelativePath).toURI(); //assumes main at default package

            if(packageUri.getScheme().equals("file")) {
                Path packageFullPath = Paths.get(packageUri);
                allClasses.addAll(getAllPackageClasses(packageFullPath, packageName));
            } else if(packageUri.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(packageUri, Collections.emptyMap());

                Path packageFullPathInJar = fileSystem.getPath(packageRelativePath);
                allClasses.addAll(getAllPackageClasses(packageFullPathInJar, packageName));

                fileSystem.close();
            }
        }
        return allClasses;
    }
    private static List<Class<?>> getAllPackageClasses(Path packagePath, String packageName) throws IOException, ClassNotFoundException {
        if(!Files.exists(packagePath)) {
            return Collections.emptyList();
        }
        List<Path> files = Files.list(packagePath)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        List<Class<?>> classes = new ArrayList<>();
        for(Path filePath: files) {
            String fileName = filePath.getFileName().toString();

            if(fileName.endsWith(".class")) {
                String classFullName = packageName + "." + fileName.replaceFirst("\\.class", "");
                Class<?> clazz = Class.forName(classFullName);
                classes.add(clazz);
            }
        }
        return classes;
    }
}
