package dao;

import model.User;
import util.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static util.DBConnection.CONN;

public class UserDB {

    public static User activeUser = new User();

    public UserDB() {
    }

    //Login the User
    public static boolean login(String username, String password) {
        try {
            Statement statement = CONN.createStatement();
            String query = "SELECT * FROM user WHERE userName = '" + username + "' AND password = '" + password + "'";
            ResultSet results = statement.executeQuery(query);

            if (results.next()) {
                activeUser = new User();
                activeUser.setUserName(results.getString("userName"));
                activeUser.setUserId(results.getInt("userId"));
                statement.close();
                Logger.log(username, true);
                return true;
            }
            else {
                Logger.log(username, false);
                return false;
            }
        }
        catch(SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
            return false;
        }
    }
}