package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import util.DBConnection;
import util.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDB {

    public static User activeUser = new User();

    public UserDB() {}

    //Login the User
    public static boolean login(String username, String password) {
        try {
            Statement stmt = DBConnection.getConn().createStatement();
            String qry = "SELECT * FROM user WHERE userName = '" + username + "' AND password = '" + password + "'";
            ResultSet rs = stmt.executeQuery(qry);

            if (rs.next()) {
                activeUser = new User();
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

    public static User getActiveUser() {
        return activeUser;
    }

    //Return the list of users
    public static ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String getUsers = "SELECT * FROM user";

        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(getUsers);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                User activeUser = new User();
                activeUser.setUserId(rs.getInt("userId"));
                activeUser.setUserName(rs.getString("userName"));
                activeUser.setPassword(rs.getString("password"));

                users.add(activeUser);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return users;
    }

    //Return a user by ID
    public static User getUser(int userId) {
        String getUser = "SELECT * FROM user WHERE userId = ?";
        User user = new User();

        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(getUser);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return user;
    }
}
