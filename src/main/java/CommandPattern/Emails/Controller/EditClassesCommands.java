package CommandPattern.Emails.Controller;

import CommandPattern.Emails.CommandMap;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

public class EditClassesCommands {

    public static void handleFile(File file, String command, String commandName) throws Exception{

        String message = "";


        try {

            // Create a new JavaClassLoader
            ClassLoader classLoader = new ClassLoader() {
                @Override
                public Class<?> loadClass(String s) throws ClassNotFoundException {
                    return super.loadClass(s);
                }
            };

            if(command.equals("AddClass")){
                Class loadedClass = classLoader.loadClass("CommandPattern.Emails." + FilenameUtils.removeExtension(file.getName()));
                MapHandler.addProperty(commandName,FilenameUtils.removeExtension(file.getName()));

            } else if(command.equals("UpdateClass")){

                Class loadedClass = classLoader.loadClass("CommandPattern.Emails." + FilenameUtils.removeExtension(file.getName()));
                MapHandler.addProperty(commandName,FilenameUtils.removeExtension(file.getName()));

            } else if (command.equals("DeleteClass")){

                file.delete();
                MapHandler.removeProperty(commandName);

            } else {
                throw new Exception();
            }




            //System.out.println("Loaded class name: " + loadedClass.getName());


            // Create a new instance from the loaded class
            //Constructor constructor = loadedClass.getConstructor();
            //Object myClassObject = constructor.newInstance();

            // Getting the target method from the loaded class and invoke it using its name
//            Class[] cArg = new Class[1];
//            cArg[0] = JSONObject.class;
//            Method method = loadedClass.getMethod("execute", cArg);
//            System.out.println("Invoked method name: " + method.getName());
//            method.invoke(myClassObject, new JSONObject());

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }

        System.out.println(message);

    }

}
