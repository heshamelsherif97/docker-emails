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

public class ViewFoldersCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject viewFolders(String email) throws Exception {

        ResultSet res = con.createStatement().executeQuery("select distinct folder from recipient where " +
                "recipient_email = '"+ email + "' and folder is not null;");

        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);

        ResultSet res2 = con.createStatement().executeQuery("select distinct folder from cc where " +
                "cc_email = '"+ email + "';");

        JSONArray jsonresult2 = ResultSetConverter.convertResultSetIntoJSON(res2);

        ResultSet res3 = con.createStatement().executeQuery("select distinct folder from bcc where " +
                "bcc_email = '"+ email + "';");

        JSONArray jsonresult3 = ResultSetConverter.convertResultSetIntoJSON(res3);

        JSONArray jsonresult4 = ResultSetConverter.concatArray(jsonresult,jsonresult2,jsonresult3);

        String out = ResultSetConverter.outify(jsonresult4);

        if(out.equals("{}")){
            throw new Exception();
        }

        JSONObject result = new JSONObject(out);

        return result;
    }


    public JSONObject execute(JSONObject json) {
        try {
            String email = json.getString("email");
            return viewFolders(email);
        } catch (Exception ex) {
            String message = "{ \"message\": \"Error in viewing folders\"}";
            return new JSONObject(message);
        }
    }
}
