package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.Exception;

public class DeleteEmailCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject deleteEmail(int id, String email) throws Exception {

       JSONObject result;
        try {
            // MOVE TO TRASH!
//            int s = con.createStatement().executeUpdate("UPDATE email SET type = 'deleted' WHERE ID="+id
//                    +" and sender = '"+email+"';");
            int s = 0;
            s += con.createStatement().executeUpdate("UPDATE recipient SET deleted = 'trash' WHERE "
                    +"email_id ="+id+" and recipient_email ='"+email+"' and deleted = 'false'");

            s += con.createStatement().executeUpdate("UPDATE bcc SET deleted = 'trash' WHERE "
                    +"email_id ="+id+" and bcc_email ='"+email+"' and deleted = 'false'");

            s += con.createStatement().executeUpdate("UPDATE cc SET deleted = 'trash' WHERE "
                    +"email_id ="+id+" and cc_email ='"+email+"' and deleted = 'false'");

            if (s == 0)
                throw new Exception();
            String message = "{\"message\":\"Email deleted\"}";
            result = new JSONObject(message);
        }
        catch(Exception ex){
            System.out.println(ex.toString());
            String message = "{\"message\":\"Failed to delete email\"}";
            result = new JSONObject(message);
        }
        return result;

    }

    public static void main(String[] args) throws SQLException {

    }

    public JSONObject execute(JSONObject json) {
        try {
            int id = Integer.parseInt(json.getString("email_id"));
            String email = json.getString("email");
            return deleteEmail(id,email);
        } catch (Exception ex) {
            String message = "{ \"message\": \"Error in deleting email\"}";
            return new JSONObject(message);
        }
    }
}

