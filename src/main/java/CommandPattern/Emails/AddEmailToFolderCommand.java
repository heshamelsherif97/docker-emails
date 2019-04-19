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

public class AddEmailToFolderCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();


    public static JSONObject addEmailToFolder(int id, String folderName, String email) throws Exception {
        con.createStatement().executeUpdate("UPDATE recipient SET folder = '"+folderName+
                "' where email_id ="+id+" and recipient_email = '"+email+"';");

        con.createStatement().executeUpdate("UPDATE cc SET folder = '"+folderName+
                "' where email_id ="+id+" and cc_email = '"+email+"';");

        con.createStatement().executeUpdate("UPDATE bcc SET folder = '"+folderName+
                "' where email_id ="+id+" and bcc_email = '"+email+"';");

        String message = "{ \"message\": \"Email added to folder\"}";
        return new JSONObject(message);
    }


    public JSONObject execute(JSONObject json) {
        try {
            int id = Integer.parseInt(json.getString("emailID"));
            String folderName = json.getString("folderName");
            String email = json.getString("email");
            return addEmailToFolder(id, folderName, email);
        } catch (Exception ex) {
            String message = "{ \"message\": \"Error in adding email to folder\"}";
            return new JSONObject(message);
        }
    }
}
