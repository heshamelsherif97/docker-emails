package CommandPattern.Emails;

import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import CommandPattern.Command;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ForwardEmailCommand implements Command  {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject forwardEmail( int id, String sender, String rec, String cc, String bcc)throws Exception{
        ResultSet isRecipient = con.createStatement().executeQuery("select * from recipient where " +
                "email_id = " +id +  "and " + "recipient_email = '" + sender + "';");

        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(isRecipient);
        String out = ResultSetConverter.outify(jsonresult);


        String  query = "SELECT * FROM email WHERE ID="+id+" and TYPE='sent';";
        ResultSet tmp = con.createStatement().executeQuery(query);

        jsonresult = ResultSetConverter.convertResultSetIntoJSON(tmp);
        JSONObject jsObj = jsonresult.getJSONObject(0);

        if (out.equals("{}") && !jsObj.getString("sender").equals(sender)){
            throw new Exception();
        }

        String subject = "Forwarded:" + jsObj.getString("subject");
        String body = jsObj.getString("body");
        String folderName = "NULL";

        Date time = new java.util.Date();
        java.sql.Timestamp timest = new java.sql.Timestamp(time.getTime());

//        JSONObject attTmp = jsonresult.getJSONObject(0);
//        String att = attTmp.getString("attachment_address");


        ResultSet idRes = con.createStatement().executeQuery("INSERT INTO email(timest,sender,subject," +
                "body,type) " + "VALUES('"+timest+"','"+sender+"','"+subject+"','"+body+"', " +
                "'sent') returning id;");

        jsonresult = ResultSetConverter.convertResultSetIntoJSON(idRes);
        JSONObject result = jsonresult.getJSONObject(0);
        int new_id = result.getInt("id");

        String [] recs = rec.split(",");
        for(int i = 0; i < recs.length; i++) {
            con.createStatement().executeUpdate("INSERT INTO recipient(email_id, recipient_email, deleted) " +
                    "VALUES(" + new_id +",'"+recs[i]+"', 'false');");
        }

        String [] ccs = cc.split(",");

        for(int i=0; i < ccs.length; i++){
            con.createStatement().executeUpdate("INSERT INTO cc(email_id, cc_email, deleted) " +
                    "VALUES("+new_id+",'"+ccs[i]+"', 'false');");
        }

        String [] bccs = bcc.split(",");

        for(int i=0; i < bccs.length; i++){
            con.createStatement().executeUpdate("INSERT INTO bcc(email_id, bcc_email, deleted) " +
                    "VALUES("+new_id +",'"+bccs[i]+"', 'false');");
        }

        query = "SELECT * FROM attachment WHERE email_id="+id+";" ;
        tmp = con.createStatement().executeQuery(query);
        jsonresult = ResultSetConverter.convertResultSetIntoJSON(tmp);

        for(int i = 0; i < jsonresult.length(); i++) {
            String att_addr = jsonresult.getJSONObject(i).getString("attachment_address");
            con.createStatement().executeUpdate("INSERT INTO attachment(email_id, attachment_address)" +
                    " VALUES(" + new_id + ",'" + att_addr + "');");
        }

        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email WHERE ID = "+new_id+";");
        jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        result = jsonresult.getJSONObject(0);
        System.out.println(result);

        return result;

    }

    public JSONObject execute(JSONObject json) {
        try {
            int id = Integer.parseInt(json.getString("id"));
            String sender = json.getString("email");
            String rec = json.getString("recipient");
            String cc = json.getString("cc");
            String bcc = json.getString("bcc");
            return forwardEmail( id,sender,rec,cc,bcc);
        } catch (Exception ex) {
//            ex.printStackTrace();
            String message = "{ \"message\": \"Error in forwarding email\"}";
            return new JSONObject(message);
        }
    }
}
