package CommandPattern.Emails.Controller;

import CommandPattern.Command;
import CommandPattern.Emails.RPCServer;
import org.json.JSONObject;

public class FreezeCommand implements Command {

    public JSONObject execute(JSONObject json) {
        PropertiesHandler.addProperty("freeze", "true");
        return new JSONObject();
    }
}
