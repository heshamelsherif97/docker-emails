package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewDraftsCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject viewDrafts(String email) throws Exception {
        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email WHERE type= 'draft' and"
                +" sender = '"+email +"';");
//        return res;
//        String result = "";
        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        String out = ResultSetConverter.outify(jsonresult);

        if (out.equals("{}")){
            out = "{\"message\":\"There are no saved drafts\"}";
        }

        JSONObject result = new JSONObject(out);

        return result;
    }

    public static void main(String[] args) throws SQLException {

    }

    public JSONObject execute(JSONObject json) {
        try {
            String email = json.getString("email");
            return viewDrafts(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{\"message\":\"Error in viewing drafts\"}";
            return new JSONObject(message);
        }
    }
}