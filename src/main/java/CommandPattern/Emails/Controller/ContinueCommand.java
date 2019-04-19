package CommandPattern.Emails.Controller;

import CommandPattern.Command;
import CommandPattern.Emails.RPCServer;
import org.json.JSONObject;

public class ContinueCommand implements Command {

    public JSONObject execute(JSONObject json) {
        PropertiesHandler.addProperty("freeze", "false");
        return new JSONObject();
    }
}
