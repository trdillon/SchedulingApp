package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static model.UserDB.activeUser;

public class CustomerDB {

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    //Return a list of all customers
    public static  ObservableList<Customer> getAllCustomers() {
        allCustomers.clear();
        String getCusts = "SELECT * FROM customer";

        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(getCusts);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Customer currCustomer = new Customer();
                currCustomer.setCustomerId(rs.getInt("customerId"));
                currCustomer.setCustomerName(rs.getString("customerName"));
                currCustomer.setCustomerAddress(rs.getString("address"));
                currCustomer.setCustomerCity(rs.getString("city"));
                currCustomer.setCustomerZip(rs.getString("postalCode"));
                currCustomer.setCustomerPhone(rs.getString("phone"));
                allCustomers.add(currCustomer);
            }
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

    //Return the last customer ID
    private static int getLastCustID() {
        int lastID = 0;
        String getID = "SELECT MAX(customerId) FROM customer";

        try {
            Statement stmt = DBConnection.getConn().createStatement();
            ResultSet rs = stmt.executeQuery(getID);

            if(rs.next()) {
                lastID = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return (lastID + 1);
    }

    //Add a customer
    public static Customer addCustomer(Customer customer) {
        String addCust = String.join(" ",
                "INSERT INTO customer (customerId, customerName, addressId, createDate, " +
                        "createdBy, lastUpdate, lastUpdateBy)",
                        "VALUES (?, ?, ?, NOW(), ?, NOW(), ?)");
        int customerId = getLastCustID();

        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(addCust);
            stmt.setInt(1, customerId);
            stmt.setString(2, customer.getCustomerName());
            stmt.setInt(3, customer.getCustomerAddressId());
            stmt.setString(4, activeUser.getUserName());
            stmt.setString(5, activeUser.getUserName());
            stmt.executeUpdate();
            }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return customer;
    }

    //Update a customer
    public static void updateCustomer(Customer customer) {
        String modCust = String.join(" ",
                "UPDATE customer",
                "SET customerName = ?, addressId = ?, lastUpdate = NOW(), lastUpdateBy = ?",
                "WHERE customerId = ?");

        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(modCust);
            stmt.setString(1, customer.getCustomerName());
            stmt.setInt(2, customer.getCustomerAddressId());
            stmt.setString(3, activeUser.getUserName());
            stmt.setInt(4, customer.getCustomerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Delete a customer
    public static boolean deleteCustomer(int id) {
        try {
            Statement stmt = DBConnection.getConn().createStatement();
            String delCustAdd = "DELETE FROM address WHERE addressId = " + id;
            int rsDel = stmt.executeUpdate(delCustAdd);

            if(rsDel == 1) {
                String delCus = "DELETE FROM customer WHERE customerId = "+ id;
                int rsCus = stmt.executeUpdate(delCus);

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
