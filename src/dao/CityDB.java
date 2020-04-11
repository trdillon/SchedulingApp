package dao;

import model.City;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static dao.UserDB.activeUser;
import static util.DBConnection.CONN;

public class CityDB {

    //Add a new city
    public static int addCity(City city) {
        int cityId = getNextId();
        String query = String.join(" ",
                "INSERT INTO city (cityId, city, countryId, "
                        + "createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, NOW(), ?, NOW(), ?)");

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, cityId);
            statement.setString(2, city.getCity());
            statement.setInt(3, city.getCountryId());
            statement.setString(4, activeUser.getUserName());
            statement.setString(5, activeUser.getUserName());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return cityId;
    }

    //Update a city
    public static void updateCity (City city) {
        String query = String.join(" ",
                "UPDATE city",
                "SET city = ?, countryId = ?, lastUpdate = NOW(), lastUpdateBy = ?",
                "WHERE cityId = ?");

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setString(1,city.getCity());
            statement.setInt(2, city.getCountryId());
            statement.setString(3, activeUser.getUserName());
            statement.setInt(4, city.getCityId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Get cityId from city string
    public static int getCityId(String city) {
        int cityId = 0;
        String query = "SELECT cityId FROM city WHERE city = ?";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setString(1, city);
            ResultSet results = statement.executeQuery();

            if(results.next()) {
                cityId = results.getInt("cityId");
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return cityId;
    }

    //Get city obj from cityId int
    public static City getCityObj(int cityId) {
        City cityObj = new City();
        String query = "SELECT * FROM city WHERE cityId = ?";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, cityId);
            ResultSet results = statement.executeQuery();

            if(results.next()) {
                cityObj.setCityId(results.getInt("cityId"));
                cityObj.setCity(results.getString("city"));
                cityObj.setCountryId(results.getInt("countryId"));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return cityObj;
    }

    //Get the last used cityId and increment by 1 to allocate for a new city
    private static int getNextId() {
        int lastId = 0;
        String query = "SELECT MAX(cityId) FROM city";

        try {
            Statement statement = CONN.createStatement();
            ResultSet results = statement.executeQuery(query);

            if(results.next()) {
                lastId = results.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return (lastId + 1);
    }
}