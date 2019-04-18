package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.awt.image.IntegerInterleavedRaster;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.Exception;

public class RemoveEmailFromFolderCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject removeEmailFromFolder(int id, String email) throws Exception {

        int s = con.createStatement().executeUpdate("UPDATE recipient SET folder = NULL where email_id " +
                "="+id+" and recipient_email = '"+email+"';");

        s += con.createStatement().executeUpdate("UPDATE cc SET folder = NULL where email_id " +
                "="+id+" and cc_email = '"+email+"';");

        s += con.createStatement().executeUpdate("UPDATE bcc SET folder = NULL where email_id ="
                +id+" and bcc_email = '"+email+"';");


        if (s == 0)
            throw new Exception();
        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email where ID = "+id+";");

        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        JSONObject result = jsonresult.getJSONObject(0);
        System.out.println(result);
        return result;
    }



    public JSONObject execute(JSONObject json) {
        try {
            int id = Integer.parseInt(json.getString("emailID"));
            String email = json.getString("email");
            return removeEmailFromFolder(id, email);
        } catch (Exception ex) {
            String message = "{ \"message\": \"Error in removing email from folder\"}";
            return new JSONObject(message);
        }
    }
}
