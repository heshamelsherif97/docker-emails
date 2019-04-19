package CommandPattern;


import CommandPattern.Emails.Controller.PropertiesHandler;
import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;

public class DBConnection {
    private static DBConnection dbInstance = new DBConnection();
    private static Connection con;
    private static PGPoolingDataSource source;
    private static int maxNumber;

    private DBConnection() {
        // private constructor //
        source = new PGPoolingDataSource();
        source.setServerName("localhost");
//        source.setServerName("156.223.15.15");
//        source.setPortNumber(26257);
//        source.setDatabaseName("emails?sslmode=disable");
//        source.setUser("testuser");
        source.setPortNumber(5432);
        source.setDatabaseName("scalable2?sslmode=disable");
        source.setUser("postgres");
        source.setPassword("");
        maxNumber = Integer.parseInt(PropertiesHandler.getProperty("max_db_threads"));
        source.setMaxConnections(maxNumber);
    }
    public static DBConnection getInstance() {
        return dbInstance;
    }
    public Connection getConnection() {
        try {
            return source.getConnection();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public static void setMaxNumber(int x)
    {
        PropertiesHandler.addProperty("max_db_threads", x+"");
        maxNumber = x;
        source.setMaxConnections(maxNumber);
    }
}