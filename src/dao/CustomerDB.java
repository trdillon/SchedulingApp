package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static dao.UserDB.activeUser;
import static util.DBConnection.CONN;

public class CustomerDB {

    //Add a customer
    public static Customer addCustomer(Customer customer) {
        int customerId = getNextID();
        String query = String.join(" ",
                "INSERT INTO customer (customerId, customerName, addressId, active, createDate, " +
                        "createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, 1, NOW(), ?, NOW(), ?)");

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, customerId);
            statement.setString(2, customer.getCustomerName());
            statement.setInt(3, customer.getCustomerAddressId());
            statement.setString(4, activeUser.getUserName());
            statement.setString(5, activeUser.getUserName());
            statement.executeUpdate();
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
        String query = String.join(" ",
                "UPDATE customer",
                "SET customerName = ?, addressId = ?, lastUpdate = NOW(), lastUpdateBy = ?",
                "WHERE customerId = ?");

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setString(1, customer.getCustomerName());
            statement.setInt(2, customer.getCustomerAddressId());
            statement.setString(3, activeUser.getUserName());
            statement.setInt(4, customer.getCustomerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Delete a customer
    public static void deleteCustomer(Customer customer) {
        String query = "DELETE FROM customer WHERE customerId = ?";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, customer.getCustomerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Get a list of all customers
    public static  ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String query = "SELECT * FROM customer";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            ResultSet results = statement.executeQuery();

            while(results.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(results.getInt("customerId"));
                customer.setCustomerName(results.getString("customerName"));
                customer.setCustomerAddressId(results.getInt("addressId"));
                customers.add(customer);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return customers;
    }

    //Get a single customer by ID
    public static Customer getCustomerByID(int customerId) {
        Customer customer = new Customer();
        String query = "SELECT * FROM customer WHERE customerId = ?";

        try{
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, customerId);
            ResultSet results = statement.executeQuery();

            if(results.next()) {
                customer.setCustomerId(results.getInt("customerId"));
                customer.setCustomerName(results.getString("customerName"));
                customer.setCustomerAddressId(results.getInt("addressId"));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return customer;
    }

    //Get the last used customerId and increment by 1 to allocate for a new customer
    private static int getNextID() {
        int lastID = 0;
        String query = "SELECT MAX(customerId) FROM customer";

        try {
            Statement statement = CONN.createStatement();
            ResultSet results = statement.executeQuery(query);

            if(results.next()) {
                lastID = results.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return (lastID + 1);
    }
}