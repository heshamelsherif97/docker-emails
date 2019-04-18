package CommandPattern.Emails.Controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ControlCommandMap {
    private static ConcurrentMap<String, Class<?>> cmdMap = new ConcurrentHashMap<>();

    public static void instantiate(){
        cmdMap.put("set_max_thread_count", SetMaxThreadCountCommand.class);
        cmdMap.put("freeze", FreezeCommand.class);
        cmdMap.put("continue", ContinueCommand.class);
        cmdMap.put("set_max_db_connections_count", SetMaxDBConnectionsCountCommand.class);
        cmdMap.put("add_command", AddCommandCommand.class);
        cmdMap.put("update_command", UpdateCommandCommand.class);
        cmdMap.put("delete_command", DeleteCommandCommand.class);
        cmdMap.put("update_class", UpdateClassCommand.class);

    }

    public static Class<?> queryClass(String cmd){
        return cmdMap.get(cmd);
    }
    public static ConcurrentMap getinstance(){
        return cmdMap;
    }
}
