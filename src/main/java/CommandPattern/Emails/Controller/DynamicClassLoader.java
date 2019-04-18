package CommandPattern.Emails.Controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DynamicClassLoader extends ClassLoader{


    public DynamicClassLoader(){super();}

    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class loadClass(String name, File file) throws ClassNotFoundException {
        try {
//            String url = file.getAbsolutePath();
//            url = url.replaceFirst("^\uFFFE", "");
             System.out.println(file.getAbsolutePath());
            System.out.println(file.getPath());
            System.out.println(file.getCanonicalPath());
//            URL myUrl = new URL("https://www.w3.org/TR/PNG/iso_8859-1.txt");
//            URLConnection connection = myUrl.openConnection();
//
            InputStream input = new FileInputStream("/home/vbox/Documents/scalableXmahaba/target/classes/CommandPattern/Emails/HelloCommand.class");

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while(data != -1){
                buffer.write(data);
                data = input.read();
            }

            input.close();

            byte[] classData = buffer.toByteArray();

            return defineClass("CommandPattern.Emails.HelloCommand",
                    classData, 0, classData.length);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}



