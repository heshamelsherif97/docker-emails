package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();



    public JSONObject execute(JSONObject json) {

        String message = "{ \"message\": \"Yoooo\"}";
        return new JSONObject(message);

    }
}
