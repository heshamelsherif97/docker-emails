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

public class OrderEmailsCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject orderEmails(String key, String order, String email) throws Exception {
//        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email ORDER BY " + key +" "+ order +";");
        if(key.equals("timest")){
            key = "email." + key;
        }
        else{
            key = "LOWER(email." + key+")";
        }


      ResultSet res = con.createStatement().executeQuery("select * from email INNER JOIN recipient " +
              "on email.id = recipient.email_id where recipient.recipient_email = '"+ email + "' and " +
              "email.type = 'sent' ORDER BY "+ key + " " + order +";");
//        return res;
//        String result = "";
        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        String out = ResultSetConverter.outify(jsonresult);

        if (out.equals("{}")){
            out = "{\"message\":\"Can not order emails\"}";
        }

        JSONObject result = new JSONObject(out);
        System.out.println("OUTTTT"+jsonresult.length());
        System.out.println(out);

        return result;
    }

    public static void main(String[] args) throws SQLException {

    }

    public JSONObject execute(JSONObject json) {
        try {
            String key = json.getString("key");
            String order = json.getString("order");
            String email = json.getString("email");
            return orderEmails(key,order,email);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{ \"message\": \"Error in ordering emails\"}";
            return new JSONObject(message);
        }
    }
}
