package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonManagedReference;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.awt.image.IntegerInterleavedRaster;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.Exception;

public class ViewThreadCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject viewThread(int threadID,String email) throws Exception {
//        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email WHERE " +
//                "thread_id= "+threadID+" ORDER BY timest DESC;");

        String query = "select * from email INNER JOIN recipient on email.id = recipient.email_id " +
                "where email.thread_id ="+threadID+" and email.type = 'sent' and " +
                "(email.sender='"+email+"' or recipient.recipient_email = '"+email+"') " +
                "ORDER BY timest DESC;";

        ResultSet res = con.createStatement().executeQuery(query);

        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        String out = ResultSetConverter.outify(jsonresult);
        if(out.equals("{}")){
            throw  new Exception();
        }



        JSONObject result = new JSONObject(out);

        return result;
    }

    public static void main(String[] args) throws SQLException {

    }

    public JSONObject execute(JSONObject json) {
        try {
            int threadID = Integer.parseInt(json.getString("threadID"));
            String email = json.getString("email");
            return viewThread(threadID, email);
        } catch (Exception ex) {
            //ex.printStackTrace();
            String message = "{ \"message\": \"Error in viewing thread\"}";
            return new JSONObject(message);
        }
    }
}
