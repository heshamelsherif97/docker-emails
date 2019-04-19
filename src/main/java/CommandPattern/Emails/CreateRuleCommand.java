package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import com.couchbase.client.java.query.dsl.functions.StringFunctions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CreateRuleCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();



    public static JSONObject createRule(String to, String from, String subject, String words, String owner,
    String actionName, String field1, String field2) throws Exception {

        con.createStatement().executeUpdate("INSERT INTO rule " +
                "(sent_to, sent_from, subject, words_in, owner,actionName, field1, field2) VALUES" +
                "('"+to+"','" +from +"','"+subject+"','"+words+"','"+owner+ "','"+actionName+"','" +
                field1 +"','"+field2 + "');");

        String message = "{ \"message\": \"Rule created.\"}";
        return new JSONObject(message);
    }

    public JSONObject execute(JSONObject json) {
        try {
            String to =  json.getString("sentTo");
            String from =  json.getString("sentFrom");
            String subject = json.getString("subject");
            String words = json.getString("wordsIn");
            String owner = json.getString("email");
            String actionName = json.getString("actionName");
            String field1 = json.getString("field1");
            String field2 = json.getString("field2");

            String [] actions = {"forward", "folder", "reply", "delete"};
            if(!Arrays.asList(actions).contains(actionName))
                throw new Exception();

            return createRule(to, from, subject, words, owner, actionName, field1,field2);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{ \"message\": \"Error in creating rule\"}";
            return new JSONObject(message);
        }
    }
}
