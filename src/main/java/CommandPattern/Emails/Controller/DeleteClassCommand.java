package CommandPattern.Emails.Controller;

import CommandPattern.Command;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import java.io.File;

public class DeleteClassCommand implements Command {

    public JSONObject execute(JSONObject json) {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String s) throws ClassNotFoundException {
                return super.loadClass(s);
            }
        };

        try{
            File file = (File) json.get("file");
            file.delete();
            MapHandler.removeProperty(FilenameUtils.removeExtension(file.getName()));
        } catch (Exception e){
            e.printStackTrace();
            return new JSONObject("{ \"message\" : \"Error in deleting class\" }");
        }

        return new JSONObject();
    }
}
