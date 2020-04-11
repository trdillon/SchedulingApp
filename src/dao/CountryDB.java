package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static dao.UserDB.activeUser;
import static util.DBConnection.CONN;

public class CountryDB {

    //Add a new country
    public static int addCountry(Country country) {
        int countryId = getNextId();
        String query = String.join(" ",
                "INSERT INTO country (countryId, country, " +
                "createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, NOW(), ?, NOW(), ?)");

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, countryId);
            statement.setString(2, country.getCountry());
            statement.setString(4, activeUser.getUserName());
            statement.setString(5, activeUser.getUserName());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return countryId;
    }

    //Update a country
    public static void updateCountry (Country country) {
        String query = String.join(" ",
                "UPDATE country",
                "SET country = ?, lastUpdate = NOW(), lastUpdateBy = ?",
                "WHERE countryId = ?");

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setString(1,country.getCountry());
            statement.setString(5, activeUser.getUserName());
            statement.setInt(6, country.getCountryId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Delete a country
    public void deleteCountry(Country country) {
        String query = "DELETE FROM country WHERE countryId = ?";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, country.getCountryId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Get a list of all countries
    public static ObservableList<Country> getCountries() {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        String query = "SELECT * FROM country";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            ResultSet results = statement.executeQuery();

            while(results.next()) {
                Country country = new Country();
                country.setCountryId(results.getInt("countryId"));
                country.setCountry(results.getString("country"));
                countries.add(country);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return countries;
    }

    //Get country obj from countryId int
    public static Country getCountryObj(int countryId) {
        Country countryObj = new Country();
        String query = "SELECT * FROM country WHERE countryId = ?";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, countryId);
            ResultSet results = statement.executeQuery();

            if(results.next()) {
                countryObj.setCountryId(results.getInt("countryId"));
                countryObj.setCountry(results.getString("country"));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return countryObj;
    }

    //Get the last used countryId and increment by 1 to allocate for a new country
    private static int getNextId() {
        int lastId = 0;
        String query = "SELECT MAX(countryId) FROM country";

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
