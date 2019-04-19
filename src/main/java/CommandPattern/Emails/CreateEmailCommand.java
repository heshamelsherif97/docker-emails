package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import com.couchbase.client.java.document.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.Exception;
import java.sql.Timestamp;
import java.util.Date;

public class CreateEmailCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject createEmail(String sender, String subject, String body, String folderName, String rec, String cc, String bcc, String att) throws Exception {
        ResultSet tmp = con.createStatement().executeQuery("SELECT * FROM email;");
        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(tmp);
        tmp = con.createStatement().executeQuery("SELECT MAX(thread_id) FROM email;");
        jsonresult = ResultSetConverter.convertResultSetIntoJSON(tmp);
        JSONObject tmpJSON = jsonresult.getJSONObject(0);
        System.out.println(tmpJSON);
        Date time = new java.util.Date();
        java.sql.Timestamp current = new java.sql.Timestamp(time.getTime());


        ResultSet idRes = con.createStatement().executeQuery("INSERT INTO email(timest,sender,subject,body,type)VALUES("+ "'"+current+"','"+sender+"','"+subject+"','"+body+"',"+"'draft') returning id;");

        //ResultSet idRes = con.createStatement().executeQuery("SELECT ID FROM email WHERE SENDER = '"+sender+"' and SUBJECT = '"+subject+"' and BODY = '"+body+
          //     "' and FOLDER = '"+folderName+"' and TIMEST = '"+current+"';");


        //ResultSet idRes = con.createStatement().executeQuery("SELECT ID FROM email WHERE SENDER = '"+sender+"' and TIMEST = '+"+current+
          //      "';");

        jsonresult = ResultSetConverter.convertResultSetIntoJSON(idRes);
        JSONObject result = jsonresult.getJSONObject(0);
        int new_id = result.getInt("id");

        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email WHERE ID = "+new_id+";");

        if(rec.length() > 0){
            String [] recs = rec.split(",");
            for(int i = 0; i < recs.length; i++) {
                con.createStatement().executeUpdate("INSERT INTO recipient(email_id, recipient_email)" +
                         " VALUES(" + new_id +",'"+recs[i].replaceAll(" ", "")+"');");
            }
        }
        else
        {
            throw new Exception();
        }

        if(cc.length() > 0){
            String [] ccs = cc.split(",");
            for(int i=0; i < ccs.length; i++){
                con.createStatement().executeUpdate("INSERT INTO cc(email_id, cc_email)" +
                        " VALUES("+new_id+",'"+ccs[i]+"');");
            }
        }

        if(bcc.length() > 0) {
            String[] bccs = bcc.split(",");
            for (int i = 0; i < bccs.length; i++) {
                con.createStatement().executeUpdate("INSERT INTO bcc(email_id, bcc_email)" +
                        " VALUES(" + new_id+ ",'" + bccs[i] + "');");
            }
        }

        if(att.length() > 0) {
            String[] attachments = att.split(",");
            for (int i = 0; i < attachments.length; i++) {
                tmp = con.createStatement().executeQuery("SELECT * FROM attachment;");
                jsonresult = ResultSetConverter.convertResultSetIntoJSON(tmp);
                con.createStatement().executeUpdate("INSERT INTO attachment(email_id, attachment_address) VALUES(" + new_id
                        + ",'" + attachments[i] + "');");
            }
        }
//        return res;
//        String result = "";
         jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        result = jsonresult.getJSONObject(0);
        System.out.println(result);

        return result;
    }

    public static void main(String[] args) throws SQLException {

    }

    public JSONObject execute(JSONObject json) {
        try {
            String folderName = json.getString("folderName");
            String sender = json.getString("email");
            String subject = json.getString("subject");
            String body = json.getString("body");
            String rec = json.getString("recipient");
            String cc = json.getString("cc");
            String bcc = json.getString("bcc");
            String att = json.getString("attachments");
            return createEmail( sender,subject, body,folderName,rec,cc,bcc,att);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{ \"message\": \"Error in creating email\"}";
            return new JSONObject(message);
        }
    }
}
