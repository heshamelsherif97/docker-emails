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

public class ViewTrashCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject viewTrash(String email) throws Exception {
//        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email WHERE type = 'deleted'"
//                +"sender = '"+email +"';");


        ResultSet res = con.createStatement().executeQuery("select * from email INNER JOIN recipient on" +
                " email.id = recipient.email_id where recipient.recipient_email = '"+ email + "' and "
                +"recipient.deleted = 'trash';");

        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);

        ResultSet res2 = con.createStatement().executeQuery("select * from email INNER JOIN cc on" +
                " email.id = cc.email_id where cc.cc_email = '"+ email + "' and "
                +"cc.deleted = 'trash';");

        JSONArray jsonresult2 = ResultSetConverter.convertResultSetIntoJSON(res2);

        ResultSet res3 = con.createStatement().executeQuery("select * from email INNER JOIN bcc on" +
                " email.id = bcc.email_id where bcc.bcc_email = '"+ email + "' and "
                +"bcc.deleted = 'trash';");

        JSONArray jsonresult3 = ResultSetConverter.convertResultSetIntoJSON(res3);

        JSONArray jsonresult4 = ResultSetConverter.concatArray(jsonresult,jsonresult2,jsonresult3);

        String out = ResultSetConverter.outify(jsonresult4);

        if (out.equals("{}")){
            out = "{\"message\":\"Trash is empty\"}";
        }

        JSONObject result = new JSONObject(out);
        return result;
    }


    public JSONObject execute(JSONObject json) {
        try {
            String email = json.getString("email");
            return viewTrash(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{ \"message\": \"Error in viewing trash\"}";
            return new JSONObject(message);
        }
    }
}
