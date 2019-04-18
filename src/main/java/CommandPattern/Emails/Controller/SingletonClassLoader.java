package CommandPattern.Emails.Controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class SingletonClassLoader {
    private static ClassLoader classLoader = new ClassLoader() {
        @Override
        public Class<?> loadClass(String s) throws ClassNotFoundException {
            return super.loadClass(s);
        }
    };


    public static void update(){
        classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String s) throws ClassNotFoundException {
                return super.loadClass(s);
            }
        };
        try {
            InputStream input = new FileInputStream("src/main/java/CommandPattern/Emails/Controller/configCommandMap.properties");
            if (input == null) {
                throw new Exception();
            }

            Properties prop = new Properties();
            prop.load(input);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                Class loadedClass = null;

                if(!key.equals("timestamp"))
                {
                    loadedClass = classLoader.loadClass("CommandPattern.Emails." + value);
                    System.out.println("Key : " + key + ", Value : " + value);
                }



            }
        }
        catch (Exception e){

        }
    }

}
