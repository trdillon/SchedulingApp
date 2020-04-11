package model;

import exception.CustomerException;
import javafx.beans.property.*;

public class Customer {

    private IntegerProperty customerId = new SimpleIntegerProperty();
    private StringProperty customerName = new SimpleStringProperty();
    private IntegerProperty customerAddressId = new SimpleIntegerProperty();
    private BooleanProperty isActive = new SimpleBooleanProperty();

    //Constructors
    public Customer() {}
    //TODO - safe delete customer constructor with parameters and unused getters & setters
/*
    public Customer(int id, String name, String address, String city, String zip, String phone) {
        setCustomerId(id);
        setCustomerName(name);
        setCustomerAddress(address);
        setCustomerCity(city);
        setCustomerZip(zip);
        setCustomerPhone(phone);
    }
*/
    //Getters and Setters
    public int getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
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

    public boolean isActive() {
        return isActive.get();
    }

    public void setActive(boolean customerActive) {
        this.isActive.set(customerActive);
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
