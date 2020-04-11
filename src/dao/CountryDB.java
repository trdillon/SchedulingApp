package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static util.DBConnection.CONN;

public class CountryDB {

    //Get a list of all countries
    public static ObservableList<Country> getCountries() {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        String query = "SELECT * FROM country";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                Country country = new Country();
                country.setCountryId(results.getInt("countryId"));
                country.setCountry(results.getString("country"));
                countries.add(country);
            }
        } catch (SQLException e) {
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

            if (results.next()) {
                countryObj.setCountryId(results.getInt("countryId"));
                countryObj.setCountry(results.getString("country"));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return countryObj;
    }
}