package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewSentCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject viewSent(String email) throws Exception {
        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email WHERE type = 'sent'"
                +" and sender = '"+ email+"';");

        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        String out = ResultSetConverter.outify(jsonresult);

        if (out.equals("{}")){
            out = "{\"message\":\"No emails have been sent\"}";
        }

        JSONObject result = new JSONObject(out);
        return result;
    }

    public static void main(String[] args) throws SQLException {

    }

    public JSONObject execute(JSONObject json) {
        try {
            String email = json.getString("email");
            return viewSent(email);
        } catch (Exception ex) {
            String message = "{ \"message\": \"Error in viewing sent emails\"}";
            return new JSONObject(message);
        }
    }
}
