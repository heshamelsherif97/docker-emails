package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.Exception;

public class CreateFolderCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

//    private static void writeResultSetToJson(final ResultSet rs,
//                                             final JsonGenerator jg)
//            throws SQLException, IOException {
//        final var rsmd = rs.getMetaData();
//        final var columnCount = rsmd.getColumnCount();
//        jg.writeStartArray();
//        while (rs.next()) {
//            jg.writeStartObject();
//            for (var i = 1; i <= columnCount; i++) {
//                jg.writeObjectField(rsmd.getColumnName(i), rs.getObject(i));
//            }
//            jg.writeEndObject();
//        }
//        jg.writeEndArray();
//    }

    public static JSONObject createFolder(String folderName) throws Exception {
        System.out.println(folderName);
        con.createStatement().executeUpdate("INSERT INTO folder(name) VALUES('"+folderName+"');");
        ResultSet res = con.createStatement().executeQuery("SELECT * FROM folder WHERE name = ('"+folderName+"');");
//        return res;
//        String result = "";
        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        JSONObject result = jsonresult.getJSONObject(0);
        System.out.println(result);
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

    public static void main(String[] args) throws SQLException {
        // Connect to the "email" database.
        try {
            createFolder("Folder Name");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JSONObject execute(JSONObject json) {
        try {
            String folderName = json.getString("folderName");
            return createFolder(folderName);
        } catch (Exception ex) {
            String message = "{ \"message\": \"Error in creating folder\"}";
            return new JSONObject(message);
        }
    }
}
