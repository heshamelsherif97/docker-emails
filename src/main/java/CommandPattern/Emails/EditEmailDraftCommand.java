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

public class EditEmailDraftCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject editEmail(int id,String sender, String subject, String body, String folderName, String rec, String cc, String bcc, String att) throws Exception {

        Date time = new java.util.Date();
        java.sql.Timestamp current = new java.sql.Timestamp(time.getTime());

        int s = con.createStatement().executeUpdate("Update email set timest = '" + current +
                "', subject = '" + subject + "', body = '" + body  + "' " +
                "where id = " + id + "and type = 'draft' and sender = '"+sender+"'");
        if (s == 0)
        {
            throw new Exception();
        }
        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email WHERE ID = "+id+";");

        System.out.println("DELETE FROM bcc WHERE email_id = "+id+";");
        //delete old relations
        con.createStatement().executeUpdate("DELETE FROM bcc WHERE email_id = "+id+";");
        con.createStatement().executeUpdate("DELETE FROM cc WHERE email_id = "+id+";");
        con.createStatement().executeUpdate("DELETE FROM recipient WHERE email_id = "+id+";");
        con.createStatement().executeUpdate("DELETE FROM attachment WHERE email_id = "+id+";");

        //update recipients
        String [] recs = rec.split(",");
        for(int i = 0; i < recs.length; i++) {
            con.createStatement().executeUpdate("INSERT INTO recipient(email_id, recipient_email,deleted)" +
                    " VALUES(" + id +",'"+recs[i]+"','false');");
        }


        String [] ccs = cc.split(",");
        for(int i=0; i < ccs.length; i++){
            con.createStatement().executeUpdate("INSERT INTO cc(email_id, cc_email,deleted) VALUES("+id
                    +",'"+ccs[i]+"','false');");
        }

        String [] bccs = bcc.split(",");
        for(int i=0; i < bccs.length; i++){
            con.createStatement().executeUpdate("INSERT INTO bcc(email_id, bcc_email,deleted) VALUES("+id
                    +",'"+bccs[i]+"','false');");
        }

        String [] attachments = att.split(",");
        for(int i=0; i < attachments.length; i++){
            con.createStatement().executeUpdate("INSERT INTO attachment(email_id, attachment_address) VALUES(" + id
                    +",'"+attachments[i]+"');");
        }

        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        JSONObject result = jsonresult.getJSONObject(0);
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
            int id = Integer.parseInt(json.getString("emailID"));

            return editEmail( id,sender,subject, body,folderName,rec,cc,bcc,att);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{ \"message\": \"Error in editing email\"}";
            return new JSONObject(message);
        }
    }
}
