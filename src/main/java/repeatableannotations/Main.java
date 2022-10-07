package repeatableannotations;

import annoationreading.ScanPackages;
import repeatableannotations.loaders.Cache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ScanPackages({"loaders"})
public class Main {
    public static void main(String[] args) {
        schedule();
    }

    public static void schedule() {
        List<Class<?>> allClasses = List.of(Cache.class);
        List<Method> scheduledExecutorMethods = getScheduledExecutorMethods(allClasses);
        for(Method method: scheduledExecutorMethods) {
            scheduleMethod(method);
        }
    }

    private static void scheduleMethod(Method method) {
        Annotations.ExecuteOnSchedule[] schedules = method.getAnnotationsByType(Annotations.ExecuteOnSchedule.class);

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        for(Annotations.ExecuteOnSchedule schedule: schedules) {
            scheduledExecutorService.scheduleAtFixedRate(
                    ()->runWhenScheduled(method),
                    schedule.delaySeconds(),
                    schedule.periodSeconds(),
                    TimeUnit.SECONDS
            );
        }
    }

    private static void runWhenScheduled(Method method) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        System.out.println(String.format("Executing at %s", dateFormat.format(currentDate)));
        try {
            method.invoke(null);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static List<Method> getScheduledExecutorMethods(List<Class<?>> allClasses) {
        List<Method> scheduledMethods = new ArrayList<>();
        for(Class<?>  clazz: allClasses) {
            if(!clazz.isAnnotationPresent(Annotations.ScheduledExecutorClass.class)) {
                continue;
            }
            for(Method method: clazz.getDeclaredMethods()) {
                if(method.getAnnotationsByType(Annotations.ExecuteOnSchedule.class).length!=0) {
                    scheduledMethods.add(method);
                }
            }
        }
        return scheduledMethods;
    }
}
