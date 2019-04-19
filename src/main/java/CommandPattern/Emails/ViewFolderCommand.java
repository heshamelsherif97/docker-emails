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

public class ViewFolderCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject viewFolder(String folderName, String email) throws Exception {
        ResultSet res = con.createStatement().executeQuery("select * from email INNER JOIN recipient on" +
                " email.id = recipient.email_id where recipient.recipient_email = '"+ email + "' and "
                +"recipient.folder = '" +folderName+"';");

        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);

        ResultSet res2 = con.createStatement().executeQuery("select * from email INNER JOIN cc on" +
                " email.id = cc.email_id where cc.cc_email = '"+ email + "' and "
                +"cc.folder = '" +folderName+"';");

        JSONArray jsonresult2 = ResultSetConverter.convertResultSetIntoJSON(res2);

        ResultSet res3 = con.createStatement().executeQuery("select * from email INNER JOIN bcc on" +
                " email.id = bcc.email_id where bcc.bcc_email = '"+ email + "' and "
                +"bcc.folder = '" +folderName+"';");

        JSONArray jsonresult3 = ResultSetConverter.convertResultSetIntoJSON(res3);

        JSONArray jsonresult4 = ResultSetConverter.concatArray(jsonresult,jsonresult2,jsonresult3);

        String out = ResultSetConverter.outify(jsonresult4);

        if (out.equals("{}")){
            out = "{\"message\":\"No emails in this folder\"}";
        }

        JSONObject result = new JSONObject(out);
        System.out.println("OUTTTT"+jsonresult.length());
        System.out.println(out);

        return result;
    }


    public JSONObject execute(JSONObject json) {
        try {
            String folderName = json.getString("folderName");
            String email = json.getString("email");
            return viewFolder(folderName, email);
        } catch (Exception ex) {
            String message = "{ \"message\": \"Error in viewing folder\"}";
            return new JSONObject(message);
        }
    }
}
