package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchInboxCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject search(String value, String email) throws Exception {
        ResultSet res = con.createStatement().executeQuery("select * from email INNER JOIN " +
                "recipient on email.id = recipient.email_id " +
                "where recipient.recipient_email ='"+email+"' " +
                "and email.type = 'sent' and recipient.deleted = 'false' and " +
                "( email.subject like '%"+value +"%' OR email.body like '%" + value +"%' );");
//        return res;
//        String result = "";
        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        String out = ResultSetConverter.outify(jsonresult);

        if (out.equals("{}")){
            out = "{\"message\":\"No emails found\"}";
        }

        JSONObject result = new JSONObject(out);

        return result;
    }



    public JSONObject execute(JSONObject json) {
        try {
            String value = json.getString("value");
            String email = json.getString("email");
            return search(value,email);
        } catch (Exception ex) {
            String message = "{ \"message\": \"Error in searching inbox\"}";
            return new JSONObject(message);
        }
    }
}
