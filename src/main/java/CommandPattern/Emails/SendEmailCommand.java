package CommandPattern.Emails;

import CommandPattern.Command;
import CommandPattern.ResultSetConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class SendEmailCommand implements Command {
    static Connection con = DBConnection.getInstance().getConnection();

    public static JSONObject sendEmail(int id, String sender) throws Exception {
        JSONObject result;
        try {
            // Delete completely
            //DELETE FROM account_details WHERE account_id = 1 RETURNING *;
            ResultSet email = con.createStatement().executeQuery("UPDATE email SET type = 'sent' WHERE " +
                    "type = 'draft' and " + "ID="+id+" and sender = '"+sender+"' returning subject, body");

            JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(email);

            System.out.println(jsonresult);
            result = jsonresult.getJSONObject(0);
            String body = result.getString("body");
            String subject = result.getString("subject");

            ResultSet recipients = con.createStatement().executeQuery("SELECT * FROM recipient" +
                    " where email_id = " + id + ";");
            JSONArray recs = ResultSetConverter.convertResultSetIntoJSON(recipients);

            for(int i = 0; i < recs.length(); i++)
            {
                System.out.println("Getting recs in send email");
                applyRule(id,recs.getJSONObject(i).getString("recipient_email"), recs, sender,
                        subject, body );
            }


            email = con.createStatement().executeQuery("select * from email where " +
                     "ID="+id+";");
            jsonresult = ResultSetConverter.convertResultSetIntoJSON(email);

            System.out.println(jsonresult);
            result = jsonresult.getJSONObject(0);
            body = result.getString("body");
            subject = result.getString("subject");


            String encBody = EncryptDecrypt.encrypt("Bar12345Bar12345","RandomInitVector", body);
            String encSubject = EncryptDecrypt.encrypt("Bar12345Bar12345","RandomInitVector", subject);

            result.remove("body");
            result.remove("subject");
            result.put("body", encBody);
            result.put("subject", encSubject);



            //String message = "{\"message\":\"Email sent\"}";
            //result = new JSONObject(message);
        }
        catch(Exception ex){
            ex.printStackTrace();
            String message = "{\"message\":\"Failed to send email\"}";
            result = new JSONObject(message);
        }
        return result;
    }

    public JSONObject execute(JSONObject json) {
        try {
            String sender = json.getString("email");
            int id = Integer.parseInt(json.getString("emailID"));
            return sendEmail(id, sender);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "{ \"message\": \"Error in sending email\"}";
            return new JSONObject(message);
        }
    }

    public static void applyRule(int id, String owner, JSONArray recipients, String sender, String subject, String body){

        try{
            ResultSet recRules = con.createStatement().executeQuery("SELECT * FROM rule where owner = '" + owner + "';");
            JSONArray jsonresult = ResultSetConverter.convertResultSetIntoJSON(recRules);
            System.out.println("JSON RULE: " + jsonresult);
            for(int i = 0; i < jsonresult.length(); i++) {
                System.out.println("Looping over rec rules: " + owner);
                JSONObject rule = jsonresult.getJSONObject(i);

                String action = rule.getString("actionname");
                String field1 = rule.getString("field1");
                String field2 = rule.getString("field2");


                String sent_to = rule.getString("sent_to");
                String sent_from = rule.getString("sent_from");
                String subject_rule = rule.getString("subject");
                String body_rule = rule.getString("words_in");

                if(sent_to.length() > 0){
                    String [] to_arr = sent_to.split(",");
                    for(int j = 0; j < recipients.length(); j++) {
                        String rec = recipients.getJSONObject(j).getString("recipient_email");
                        if(Arrays.asList(to_arr).contains(rec) && rec.length() > 0){
                            // apply rule
                            doAction(id, owner, action, field1, field2);
                        }
                    }
                }

                else if(sent_from.length() > 0){
                    String [] from_arr = sent_from.split(",");
                    if(Arrays.asList(from_arr).contains(sender)){
                        // apply rule
                        doAction(id, owner, action, field1, field2);
                    }
                }

                else if(subject_rule.length() > 0){
                    System.out.println("CHECK SUBJECT");
                    String [] subject_arr = subject_rule.split(",");
                    for(int j = 0; j < subject_arr.length; j++) {
                        if (subject.contains(subject_arr[j])) {
                            doAction(id, owner, action, field1, field2);
                        }
                    }
                }

                else if(body_rule.length() > 0){
                    String [] body_arr = body_rule.split(",");
                    for(int j = 0; j < body_arr.length; j++) {
                        if (body.contains(body_arr[j])) {
                            // apply rule
                            doAction(id, owner, action, field1, field2);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("in catch");
        }

    }

    public static void doAction(int id, String owner, String action, String field1, String field2) throws Exception{
       System.out.print("IN do actionn");
       switch(action){
           case "forward": ForwardEmailCommand.forwardEmail(id,owner,field1,"","");break;
           case "folder": AddEmailToFolderCommand.addEmailToFolder(id, field1, owner);break;
           case "reply": ReplyToEmailCommand.replyToEmail(id,owner,field2,"");break;
           case "delete": DeleteEmailCommand.deleteEmail(id, owner);break;
           default: throw new Exception();
       }
    }
}
