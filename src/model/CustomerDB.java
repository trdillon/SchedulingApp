package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerDB {

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    //Return a list of all customers
    public static  ObservableList<Customer> getAllCustomers() {
        allCustomers.clear();
        try {
            Statement stmt = DBConnection.getConn().createStatement();
            String qry = "SELECT customer.customerId, customer.customerName, address.address, address.postalCode, address.phone, city.city"
                    + " FROM customer INNER JOIN address ON customer.addressId = address.addressId"
                    + " INNER JOIN city ON address.cityId = city.cityId";
            ResultSet rs = stmt.executeQuery(qry);

            while(rs.next()) {
                Customer currCustomer = new Customer(
                rs.getInt("customerId"),
                rs.getString("customerName"),
                rs.getString("address"),
                rs.getString("city"),
                rs.getString("postalCode"),
                rs.getString("phone"));
                allCustomers.add(currCustomer);
            }
            stmt.close();
            return allCustomers;
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
            return null;
        }
    }

    //Return a single customer by ID
    public static Customer getCustomerByID(int id) {
        try {
            Statement stmt = DBConnection.getConn().createStatement();
            String qry = "SELECT * FROM customer WHERE customerId = '" + id + "'";
            ResultSet rs = stmt.executeQuery(qry);

            if(rs.next()) {
                Customer currCustomer = new Customer();
                currCustomer.setCustomerName(rs.getString("customerName"));
                stmt.close();
                return currCustomer;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return null;
    }

    //Add a customer
    public static boolean addCustomer(String name, String address, int cityId, String zip, String phone) {
        try {
            Statement stmt = DBConnection.getConn().createStatement();
            String qryAdd = "INSERT INTO address SET address = '" + address + "', phone = '" + phone + "', " +
                    "postalCode = '" + zip + "', cityId = '" + cityId;
            int rsAdd = stmt.executeUpdate(qryAdd);

            if(rsAdd == 1) {
                int addressId = (allCustomers.size() + 1);
                String qryCus = "INSERT INTO customer SET customerName = '" + name + "', addressId = " + addressId;
                int rsCus = stmt.executeUpdate(qryCus);

                if(rsCus == 1) {
                    return true;
                }
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return false;
    }

    //Update a customer
    public static boolean updateCustomer(int id, String name, String address, int cityId, String zip, String phone) {
        try {
            Statement stmt = DBConnection.getConn().createStatement();
            String qryMod = "UPDATE address SET address = '" + address + "', phone = '" + phone + "', " +
                    "postalCode = '" + zip + "', cityId = '" + cityId + "' " +
                    "WHERE addressId = " + id;
            int rsMod = stmt.executeUpdate(qryMod);

            if (rsMod == 1) {
                String qryCus = "UPDATE customer SET customerName = '" + name + "', addressId = " + id +
                        " WHERE customerId = " + id;
                int rsCus = stmt.executeUpdate(qryCus);

                if (rsCus == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return false;
    }

    //Delete a customer
    public static boolean deleteCustomer(int id) {
        try {
            Statement stmt = DBConnection.getConn().createStatement();
            String qryDel = "DELETE FROM address WHERE addressId = " + id;
            int rsDel = stmt.executeUpdate(qryDel);

            if(rsDel == 1) {
                String qryCus = "DELETE FROM customer WHERE customerId = "+ id;
                int rsCus = stmt.executeUpdate(qryCus);

                if(rsCus == 1) {
                    return true;
                }
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return false;
    }
    //TODO - test customerDB
}
