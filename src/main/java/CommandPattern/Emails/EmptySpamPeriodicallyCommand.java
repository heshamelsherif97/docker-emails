package CommandPattern.Emails;

import CommandPattern.Emails.DBConnection;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class EmptySpamPeriodicallyCommand {

    static Connection con = DBConnection.getInstance().getConnection();


    public JSONObject execute(JSONObject json) {
        try {


            Date now_date = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now_date);
            calendar.add(Calendar.DAY_OF_YEAR, 60);
            Date target_date = calendar.getTime();

            long difference = target_date.getTime() - now_date.getTime();
            System.out.println("\ndifference: " + difference);

            Timer timer = new Timer ();
            TimerTask task = new ScheduleSpamDeletionTask();
            timer.schedule(task, 60);

            String message = "{ \"message\": \"Deleting spam!\"}";
            return new JSONObject(message);

        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{ \"message\": \"Error in emptying spam\"}";
            return new JSONObject(message);
        }
    }
}

class ScheduleSpamDeletionTask extends TimerTask {

    static Connection con;

    public void run () {
        try {
            con = DBConnection.getInstance().getConnection();
            String query = "DELETE FROM recipient WHERE spam = 'true';";
            con.createStatement().executeUpdate(query);
            query = "DELETE FROM cc WHERE spam = 'true';";
            con.createStatement().executeUpdate(query);
            query = "DELETE FROM bcc WHERE spam = 'true';";
            con.createStatement().executeUpdate(query);
        }
        catch (Exception ex){
            ex.printStackTrace();
            String message = "Error in emptying spam emails";
        }
    }
}
