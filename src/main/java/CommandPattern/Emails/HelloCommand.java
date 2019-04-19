package CommandPattern.Emails;

import CommandPattern.Command;
import org.json.JSONObject;

import java.sql.SQLException;

public class HelloCommand implements Command {
    //static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject deleteEmail(int id) throws Exception {

    /*    JSONObject result;
        try {
            // MOVE TO TRASH!
            con.createStatement().executeUpdate("UPDATE email SET type = 'deleted' WHERE ID="+id+";");
            String message = "{\"message\":\"Email deleted\"}";
            result = new JSONObject(message);
        }
        catch(Exception ex){
            System.out.println(ex.toString());
            String message = "{\"message\":\"Failed to delete email\"}";
            result = new JSONObject(message);
        }
        return result;*/
    return null;
    }

    public static void main(String[] args) throws SQLException {

    }

    public JSONObject execute(JSONObject json) {
       /* try {
            int id = Integer.parseInt(json.getString("email_id"));
            return deleteEmail(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "Error in deleting email";
            return new JSONObject(message);
        }*/
        System.out.println("Farida");
        return new JSONObject();
    }

    public static void whatevies(){
        System.out.println("Farida");
    }
}

