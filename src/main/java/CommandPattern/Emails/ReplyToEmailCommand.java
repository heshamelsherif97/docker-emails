package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

public class ReplyToEmailCommand implements Command {

    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject replyToEmail(int id, String sender, String body, String att) throws Exception{

        ResultSet isRecipient = con.createStatement().executeQuery("select * from recipient where " +
                "email_id = " + id + "and " + "recipient_email = '" + sender + "' " +
                "and deleted = 'false';");

        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(isRecipient);
        String out = ResultSetConverter.outify(jsonresult);

        if (out.equals("{}")){
            throw new Exception();
        }

        String  query = "SELECT * FROM email WHERE ID=" + id + " and type = 'sent';" ;
        ResultSet tmp = con.createStatement().executeQuery(query);

        jsonresult = ResultSetConverter.convertResultSetIntoJSON(tmp);

        JSONObject jsObj = jsonresult.getJSONObject(0);
        String rec = jsObj.getString("sender");
        String subject = "RE: " + jsObj.getString("subject");
        String folderName = "NULL";

        Date time = new java.util.Date();
        java.sql.Timestamp timest = new java.sql.Timestamp(time.getTime());

        int thread = jsObj.getInt("thread_id");

        ResultSet idRes = con.createStatement().executeQuery("INSERT INTO email " +
                "(timest, sender, " + "subject, body,thread_id, type) VALUES ('"
                +timest+"','"+sender+"','"+ subject+"','"+body+"',"+thread+", 'sent') " +
                "returning id;");

        jsonresult = ResultSetConverter.convertResultSetIntoJSON(idRes);
        JSONObject result = jsonresult.getJSONObject(0);
        int new_id = result.getInt("id");

        con.createStatement().executeUpdate("INSERT INTO recipient(email_id, recipient_email, deleted)" +
                " VALUES("+new_id +",'"+rec+"', 'false');");

        String [] attachments = att.split(",");

        for(int i=0; i < attachments.length; i++) {

            con.createStatement().executeUpdate("INSERT INTO attachment(email_id, attachment_address) VALUES(" + new_id
                    + ",'" + attachments[i] + "');");
        }

        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email WHERE ID = "+new_id+";");
        jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        result = jsonresult.getJSONObject(0);

        return result;

    }
    public JSONObject execute(JSONObject json) {
        try {
            int id = Integer.parseInt(json.getString("emailID"));
            String sender = json.getString("email");
            String body = json.getString("body");
            String att = json.getString("att");
            return replyToEmail(id, sender, body, att);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{ \"message\": \"Error in replying to email\"}";
            return new JSONObject(message);
        }
    }
}