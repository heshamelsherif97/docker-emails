package CommandPattern.Emails.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesHandler {
    static Properties prop = new Properties();
    static OutputStream output;
    static InputStream input;

    public static void loadPropertiesHandler(){
        try {

            output = new FileOutputStream("config.properties");
            input = new FileInputStream("config.properties");

            // set the properties value
            prop.setProperty("max_db_threads", "20");
            prop.setProperty("max_app_threads", "20");
            prop.setProperty("freeze", "false");
            prop.setProperty("restart", "false");

            prop.store(output, null);
        } catch (Exception e) {
        }
    }

    public static void addProperty(String key, String val) {
        try {
            input = new FileInputStream("config.properties");
            output = new FileOutputStream("config.properties");
            prop.load(input);
            prop.setProperty(key, val);
            System.out.println("in addProperty");
            prop.store(output, null);
        } catch(Exception e) {
            System.out.println("error in addProperty");

        }
    }

    public static String getProperty(String key) {
        try{
            input = new FileInputStream("config.properties");
            prop.load(input);
            return prop.getProperty(key);
        } catch(Exception e) {
            return "error";
        }
    }
}
