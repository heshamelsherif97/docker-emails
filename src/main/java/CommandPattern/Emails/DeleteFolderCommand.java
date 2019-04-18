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

public class DeleteFolderCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();



    public static JSONObject DeleteFolder(String name, String email) throws Exception {
        JSONObject result;
        try {
            con.createStatement().executeUpdate("UPDATE recipient SET folder = NULL where " +
                    "folder = '"+name+"' and recipient_email = '"+email+"';");

            con.createStatement().executeUpdate("UPDATE cc SET folder = NULL where " +
                    "folder = '"+name+"' and cc_email = '"+email+"';");

            con.createStatement().executeUpdate("UPDATE bcc SET folder = NULL where " +
                    "folder = '"+name+"' and bcc_email = '"+email+"';");

            String message = "{\"message\":\"Folder deleted\"}";
            result = new JSONObject(message);
        }
        catch(Exception ex){
            System.out.println(ex.toString());
            String message = "{\"message\":\"Failed to delete folder\"}";
             result = new JSONObject(message);
        }

//        return res;
//        String result = "";
//        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
//        JSONObject result = jsonresult.getJSONObject(0);
//        System.out.println(result);
//        int i = 1;
//        JSONObject result = new JSONObject();
//        while (res.next()) {
//            String s = res.getString("sender");
////            System.out.println(s);
//            result.put(i+"", s);
//            i++;
////            result += res.getString("sender") + "\n";
////            System.out.println(res.getString("sender"));
//        }
//        System.out.println(result.toString());
        return result;
    }



    public JSONObject execute(JSONObject json) {
        try {
            String name = json.getString("folderName");
            String email = json.getString("email");
            return DeleteFolder(name, email);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{ \"message\": \"Error in deleting folder\"}";
            return new JSONObject(message);
        }
    }
}
