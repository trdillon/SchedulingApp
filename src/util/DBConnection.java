package util;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //Database variables
    private static final String USER = "U04IR7";
    private static final String PASS = "53688249095";
    private static final String DBNAME = "U04IR7";
    private static final String URL = "jdbc:mysql://3.227.166.251/" + DBNAME;
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    public static Connection CONN;

    //Connect to the DB
    public static void connect() {
        try {
            Class.forName(DRIVER);
            CONN = (Connection) DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connection successful.");
        }
        catch(ClassNotFoundException e) {
            System.out.println("Class Not Found: " + e.getMessage());
        }
        catch(SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Disconnect from the DB
    public static void disconnect() {
        try {
            CONN.close();
            System.out.println("Connection terminated.");
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }
}