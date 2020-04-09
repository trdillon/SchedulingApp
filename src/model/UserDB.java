package model;

import util.DBConnection;
import util.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDB {

    /* TODO - use local var or this? line 24 - remove User declaration if not using local
    private static User activeUser;
*/
    public UserDB(){}

    //Login the User
    public static boolean login(String username, String password) {
        try {
            Statement stmt = DBConnection.getConn().createStatement();
            String qry = "SELECT * FROM user WHERE userName = '" + username + "' AND password = '" + password + "'";
            ResultSet rs = stmt.executeQuery(qry);

            if (rs.next()) {
                User activeUser = new User();
                activeUser.setUserName(rs.getString("userName"));
                activeUser.setUserId(rs.getInt("userId"));
                stmt.close();
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

    /* TODO - Remove or use
    //Return the current user
    public static User getActiveUser() {
        return activeUser;
    }
     */
}
