package dao;

import model.Address;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static dao.UserDB.activeUser;
import static util.DBConnection.CONN;

public class AddressDB {

    //Add a new address
    public static void addAddress(Address address) {
        int addressId = getNextId();
        String query = String.join(" ",
                "INSERT INTO address (addressId, address, address2, cityId, postalCode, phone,"
                        + "createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)");

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, addressId);
            statement.setString(2, address.getAddress());
            statement.setString(3, address.getAddress2());
            statement.setInt(4, address.getCityId());
            statement.setString(5, address.getPostalCode());
            statement.setString(6, address.getPhone());
            statement.setString(7, activeUser.getUserName());
            statement.setString(8, activeUser.getUserName());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Update an address
    public static void updateAddress (Address address) {
        String query = String.join(" ",
                "UPDATE address",
                "SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdate = NOW(), lastUpdateBy = ?",
                "WHERE addressId = ?");

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setString(1, address.getAddress());
            statement.setString(2, address.getAddress2());
            statement.setInt(3, address.getCityId());
            statement.setString(4, address.getPostalCode());
            statement.setString(5, address.getPhone());
            statement.setString(6, activeUser.getUserName());
            statement.setInt(7, address.getAddressId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Get addressId from address string
    public static int getAddressId(String address) {
        int addressId = 0;
        String query = "SELECT addressId FROM address WHERE address = ?";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setString(1, address);
            ResultSet results = statement.executeQuery();

            if(results.next()) {
                addressId = results.getInt("addressId");
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return addressId;
    }

    //Get address obj from addressId int
    public static Address getAddressObj(int addressId) {
        Address addObj = new Address();
        String query = "SELECT * FROM address WHERE addressId = ?";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, addressId);
            ResultSet results = statement.executeQuery();

            if(results.next()) {
                addObj.setAddressId(results.getInt("addressId"));
                addObj.setAddress(results.getString("address"));
                addObj.setAddress2(results.getString("address2"));
                addObj.setCityId(results.getInt("cityId"));
                addObj.setPostalCode(results.getString("postalCode"));
                addObj.setPhone(results.getString("phone"));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return addObj;
    }

    //Get the last used addressId and increment by 1 to allocate for a new address
    private static int getNextId() {
        int lastId = 0;
        String query = "SELECT MAX(addressId) FROM address";

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