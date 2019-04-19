package CommandPattern.Emails;

import CommandPattern.Emails.Controller.PropertiesHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppThreadPool {
    static int numberOfThreads = Integer.parseInt(PropertiesHandler.getProperty("max_app_threads"));
    private static ExecutorService executor = Executors.newFixedThreadPool( numberOfThreads);

    public static ExecutorService getInstance(){
        return executor;
    }

    public static void setNumberOfThreads(int x){
        PropertiesHandler.addProperty("max_app_threads", x+"");
        numberOfThreads = x;
        executor = Executors.newFixedThreadPool(x);
    }

    public static int getNumberOfThreads() {
        return numberOfThreads;
    }

    public static ExecutorService update(){
        int x = Integer.parseInt(PropertiesHandler.getProperty("max_app_threads"));
        if (numberOfThreads != x)
        {
            executor.shutdown();
            numberOfThreads = x;
            executor = Executors.newFixedThreadPool(x);
        }
        return executor;
    }


}
