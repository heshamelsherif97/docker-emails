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

public class ListEmailsCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();


    public static JSONObject viewInbox(String email) throws Exception {


        ResultSet res = con.createStatement().executeQuery("select * from email INNER JOIN recipient on" +
                " email.id = recipient.email_id where recipient.recipient_email = '"+ email + "' and email.type = 'sent' and recipient.spam = 'false' and recipient.deleted = 'false';");

        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);

        ResultSet res2 = con.createStatement().executeQuery("select * from email INNER JOIN cc on" +
                " email.id = cc.email_id where cc.cc_email = '"+ email + "' and email.type = 'sent' and cc.spam" +
                " = 'false' and cc.deleted = 'false';");

        JSONArray jsonresult2 = ResultSetConverter.convertResultSetIntoJSON(res2);

        ResultSet res3 = con.createStatement().executeQuery("select * from email INNER JOIN bcc on" +
                " email.id = bcc.email_id where bcc.bcc_email = '"+ email + "' and email.type = 'sent' and bcc.spam = 'false' and bcc.deleted = 'false';");

        JSONArray jsonresult3 = ResultSetConverter.convertResultSetIntoJSON(res3);

        JSONArray jsonresult4 = ResultSetConverter.concatArray(jsonresult,jsonresult2,jsonresult3);

        String out = ResultSetConverter.outify(jsonresult4);

        if (out.equals("{}")){
            out = "{\"message\":\"Inbox is empty\"}";
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
            String email = json.getString("email");
            return viewInbox(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "Error in viewing inbox";
            return new JSONObject(message);
        }
    }
}
