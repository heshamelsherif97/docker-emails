package CommandPattern.Emails;

import CommandPattern.Emails.Controller.MapHandler;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CommandMap {
    private static ConcurrentMap<String, Class<?>> cmdMap;
    private static String timestamp = (new Date()).getTime() + "";

    public static void instantiate(){
        cmdMap = new ConcurrentHashMap<>();

       /* cmdMap.put("ListEmails", ListEmailsCommand.class);
        cmdMap.put("AddEmailToFolder", AddEmailToFolderCommand.class);
        cmdMap.put("RemoveEmailFromFolder", RemoveEmailFromFolderCommand.class);
        cmdMap.put("CreateFolder", CreateFolderCommand.class);
        cmdMap.put("DeleteFolder", DeleteFolderCommand.class);
        cmdMap.put("ViewFolder", ViewFolderCommand.class);
        cmdMap.put("ViewFolders", ViewFoldersCommand.class);
        cmdMap.put("ReportEmailForSpam", ReportEmailForSpamCommand.class);
        cmdMap.put("ViewThread", ViewThreadCommand.class);
        cmdMap.put("OrderEmails", OrderEmailsCommand.class);
        cmdMap.put("DeleteEmail", DeleteEmailCommand.class);
        cmdMap.put("CreateEmail", CreateEmailCommand.class);
        cmdMap.put("ViewTrash", ViewTrashCommand.class);
        cmdMap.put("DeleteFromTrash", DeleteFromTrashCommand.class);
        cmdMap.put("ViewSent", ViewSentCommand.class);
        cmdMap.put("SendEmail", SendEmailCommand.class);
        cmdMap.put("EditEmailDraft", EditEmailDraftCommand.class);
        cmdMap.put("ViewDrafts", ViewDraftsCommand.class);
        cmdMap.put("ReplyToEmail", ReplyToEmailCommand.class);
        cmdMap.put("SearchInbox", SearchInboxCommand.class);
        cmdMap.put("ForwardEmail", ForwardEmailCommand.class);
        cmdMap.put("ScheduleEmails", ScheduleEmailsCommand.class);
        cmdMap.put("CreateRule", CreateRuleCommand.class);*/

        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String s) throws ClassNotFoundException {
                return super.loadClass(s);
            }
        };

        FileInputStream input = null;
        try {
            input = new FileInputStream("src/main/java/CommandPattern/Emails/Controller/configCommandMap.properties");
            if (input == null) {
                throw new Exception();
            }

            Properties prop = new Properties();
            prop.load(input);
            timestamp = MapHandler.getProperty("timestamp");

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                Class loadedClass = null;

                if(!key.equals("timestamp"))
                {
                    loadedClass = classLoader.loadClass("CommandPattern.Emails." + value);
                    System.out.println("Key : " + key + ", Value : " + value);
                    cmdMap.put(key,loadedClass);
                }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }





    }

    public static void addCommand(String name, Class<?> command) {
        cmdMap.put(name, command);
    }

    public static void updateCommand(String name, Class<?> command) {
        cmdMap.replace(name, command);
    }

    public static void deleteCommand(String name) {
        cmdMap.remove(name);
    }

    public static Class<?> queryClass(String cmd){
        if(!MapHandler.getProperty("timestamp").equals(timestamp)){
            instantiate();
            System.out.println("Updating command map");
        }

        return cmdMap.get(cmd);
    }
    public static ConcurrentMap getinstance(){
        return cmdMap;
    }
}
