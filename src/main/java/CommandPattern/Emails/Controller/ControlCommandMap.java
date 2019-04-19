package CommandPattern.Emails.Controller;

import CommandPattern.Emails.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ControlCommandMap {
    private static ConcurrentMap<String, Class<?>> cmdMap = new ConcurrentHashMap<>();

    public static void instantiate(){
        cmdMap.put("set_max_thread_count", SetMaxThreadCountCommand.class);
        cmdMap.put("freeze", FreezeCommand.class);
        cmdMap.put("continue", ContinueCommand.class);
        cmdMap.put("set_max_db_connections_count", SetMaxDBConnectionsCountCommand.class);
        cmdMap.put("add_class", AddClassCommand.class);
        cmdMap.put("update_class", UpdateClassCommand.class);
        cmdMap.put("delete_class", DeleteClassCommand.class);

    }

    public static Class<?> queryClass(String cmd){
        return cmdMap.get(cmd);
    }
    public static ConcurrentMap getinstance(){
        return cmdMap;
    }
}
