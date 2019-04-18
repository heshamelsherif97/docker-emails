package CommandPattern.Emails.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

public class MapHandler {
    static Properties prop = new Properties();
    static OutputStream output;
    static InputStream input;

    public static void loadPropertiesHandler(){
        try {

            output = new FileOutputStream("src/main/java/CommandPattern/Emails/Controller/configCommandMap.properties");
            input = new FileInputStream("src/main/java/CommandPattern/Emails/Controller/configCommandMap.properties");

            prop.store(output, null);
        } catch (Exception e) {
        }
    }

    public static void addProperty(String key, String val) {
        try {
            input = new FileInputStream("src/main/java/CommandPattern/Emails/Controller/configCommandMap.properties");
            prop.load(input);
            input.close();
            prop.setProperty(key, val);


            output = new FileOutputStream("src/main/java/CommandPattern/Emails/Controller/configCommandMap.properties");
            Date time = new java.util.Date();
            prop.setProperty("timestamp", time.getTime()+"");
            System.out.println("in addProperty");
            prop.store(output, null);
            output.close();

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("error in addProperty");

        }
    }

    public static String getProperty(String key) {
        try{
            input = new FileInputStream("src/main/java/CommandPattern/Emails/Controller/configCommandMap.properties");
            prop.load(input);
            return prop.getProperty(key);
        } catch(Exception e) {
            return "error";
        }
    }

    public static void removeProperty(String key) {
        try {
            input = new FileInputStream("src/main/java/CommandPattern/Emails/Controller/configCommandMap.properties");
            prop.load(input);
            prop.remove(key);
            input.close();

            prop.remove(key);
            output = new FileOutputStream("src/main/java/CommandPattern/Emails/Controller/configCommandMap.properties");
            Date time = new java.util.Date();
            prop.setProperty("timestamp", time.getTime()+"");
            System.out.println("in remove Property");
            prop.store(output, null);
            output.close();


        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("error in remove Property");

        }
    }
}
