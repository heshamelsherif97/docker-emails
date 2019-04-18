package CommandPattern.Emails.Controller;

import CommandPattern.Command;
import CommandPattern.Emails.RPCServer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import sun.misc.Cache;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.Arrays;

public class UpdateCommandCommand implements Command {

    public JSONObject execute(JSONObject json) {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String s) throws ClassNotFoundException {
                return super.loadClass(s);
            }
        };
        try{
            File file = (File) json.get("file");
            file.createNewFile();
            String name = FilenameUtils.removeExtension(file.getName());
            Class loadedClass = classLoader.loadClass("CommandPattern.Emails." + name);
            MapHandler.addProperty(json.getString("command_name"),FilenameUtils.removeExtension(file.getName()));


        } catch (Exception e){
            e.printStackTrace();
            return new JSONObject("{ \"message\" : \"Error in updating class\" }");
        }

        return new JSONObject();
    }
}
