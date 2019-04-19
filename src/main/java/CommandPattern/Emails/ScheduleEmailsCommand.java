package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleEmailsCommand implements Command  {
    static Connection con = DBConnection.getInstance().getConnection();
    public static JSONObject scheduleEmail(int id, java.sql.Timestamp timest) throws Exception{

//        String  query = "UPDATE email SET timest="+timest+" WHERE ID="+id+";" ;
//        con.createStatement().executeQuery(query);
//        query = "UPDATE email SET folder = sent WHERE ID="+id+";" ;
//        con.createStatement().executeQuery(query);

        ResultSet res = con.createStatement().executeQuery("SELECT * FROM email WHERE ID = "+id+";");
        JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(res);
        JSONObject result = jsonresult.getJSONObject(0);
        System.out.println(result);

        return result;

    }
    public JSONObject execute(JSONObject json) {
        try {
            System.out.println("TIME NOW: ");
            int id = Integer.parseInt(json.getString("id"));
            String email = json.getString("email");
            String timestStr = json.getString("timest");
            Date target_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss+0000").parse(timestStr);
            System.out.println("TARGET DATE:"+target_date);
            Date now_date = new java.util.Date();

            long difference = target_date.getTime() - now_date.getTime();
            System.out.println("\ndifference: " + difference);
            java.sql.Timestamp timest = new java.sql.Timestamp (target_date.getTime());

            Timer timer = new Timer ();
            TimerTask task = new ScheduleEmailTimerTask(id, timest, email);
            timer.schedule(task, difference);

            return scheduleEmail( id, timest);

        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{ \"message\": \"Error in scheduling email\"}";
            return new JSONObject(message);
        }
    }
}
class ScheduleEmailTimerTask extends TimerTask {

    int id;
    String email;
    java.sql.Timestamp timest;
    static Connection con;

    public ScheduleEmailTimerTask (int id, java.sql.Timestamp timest, String email){
        this.id = id;
        this.timest = timest;
        this.email = email;
    }
    public void run () {
        try {
            SendEmailCommand.sendEmail(id, email);
//            con = DBConnection.getInstance().getConnection();
//            String query = "UPDATE email SET timest='" + timest + "' WHERE ID=" + id + ";";
//            con.createStatement().executeUpdate(query);
//            query = "UPDATE email SET type = 'sent' WHERE ID=" + id + ";";
//            con.createStatement().executeUpdate(query);
        }
        catch (Exception ex){
            ex.printStackTrace();
            String message = "Error in scheduling email";
        }
    }
}