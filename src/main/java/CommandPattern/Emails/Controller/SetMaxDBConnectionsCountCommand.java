package CommandPattern.Emails.Controller;

import CommandPattern.Command;
import CommandPattern.Emails.DBConnection;
import org.json.JSONObject;

public class SetMaxDBConnectionsCountCommand implements Command {

    public JSONObject execute(JSONObject json) {

        int x = Integer.parseInt(json.getString("db_conn"));
        DBConnection.setMaxNumber(x);

        return new JSONObject();
    }
}
