package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.Exception;

public class ReportEmailForSpamCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject reportEmailForSpam(int id, String email) throws Exception {

        JSONObject result;
        try {
            int s = 0;
            s += con.createStatement().executeUpdate("UPDATE recipient SET spam = 'true' WHERE "
                    +"email_id ="+id+" and recipient_email ='"+email+"' and spam = 'false'");

            s += con.createStatement().executeUpdate("UPDATE bcc SET spam = 'true' WHERE "
                    +"email_id ="+id+" and bcc_email ='"+email+"' and spam = 'false'");
            s += con.createStatement().executeUpdate("UPDATE cc SET spam = 'true' WHERE "
                    +"email_id ="+id+" and cc_email ='"+email+"' and spam = 'false'");


            if(s == 0){
                throw new Exception();
            }
            String message = "{\"message\":\"Email reported\"}";
            result = new JSONObject(message);
        }
        catch(Exception ex){
            System.out.println(ex.toString());
            String message = "{\"message\":\"Failed to report email\"}";
            result = new JSONObject(message);
        }
        return result;
    }


    public JSONObject execute(JSONObject json) {
        try {
            int id = Integer.parseInt(json.getString("emailID"));
            String email = json.getString("email");
            return reportEmailForSpam(id,email);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{\"message\":\"Failed to report email\"}";
            return new JSONObject(message);
        }
    }
}

