package model;

import exception.CustomerException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {

    private IntegerProperty customerId = new SimpleIntegerProperty();
    private StringProperty customerName = new SimpleStringProperty();
    private IntegerProperty customerAddressId = new SimpleIntegerProperty();

    public Customer() {}

    public int getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public IntegerProperty getCustomerIdProperty() {
        return this.customerId;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public StringProperty getCustomerNameProperty() {
        return this.customerName;
    }

    public int getCustomerAddressId() {
        return customerAddressId.get();
    }

    public void setCustomerAddressId(int customerAddressId) {
        this.customerAddressId.set(customerAddressId);
    }

    public static boolean isValidCust(Customer customer, Address address, City city, Country country) throws CustomerException {
        if (customer.getCustomerName().equals("")) {
            throw new CustomerException("The customer name cannot be blank.");
        }
        if (address.getAddress().equals("")) {
            throw new CustomerException("The address cannot be blank.");
        }
        if (address.getPhone().equals("")) {
            throw new CustomerException("The phone number cannot be blank.");
        }
        if (address.getPostalCode().equals("")) {
            throw new CustomerException("The postal/zip code cannot be blank.");
        }
        if (city.getCity().equals("")) {
            throw new CustomerException("The city cannot be blank.");
        }
        if (country.getCountry().equals("")) {
            throw new CustomerException("There must be a country selected.");
        }
        return true;
    }
}