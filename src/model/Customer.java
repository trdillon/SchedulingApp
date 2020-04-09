package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {

    private SimpleIntegerProperty customerID = new SimpleIntegerProperty();
    private SimpleStringProperty customerName = new SimpleStringProperty();
    private SimpleStringProperty customerAddress = new SimpleStringProperty();
    private SimpleStringProperty customerCity = new SimpleStringProperty();
    private SimpleStringProperty customerZip = new SimpleStringProperty();
    private SimpleStringProperty customerPhone = new SimpleStringProperty();
    private SimpleBooleanProperty isActive = new SimpleBooleanProperty();

    //Constructors
    public Customer() {}

    public Customer(int id, String name, String address, String city, String zip, String phone) {
        setCustomerID(id);
        setCustomerName(name);
        setCustomerAddress(address);
        setCustomerCity(city);
        setCustomerZip(zip);
        setCustomerPhone(phone);
    }

    //Getters and Setters
    public int getCustomerID() {
        return customerID.get();
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getCustomerAddress() {
        return customerAddress.get();
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress.set(customerAddress);
    }

    public String getCustomerCity() {
        return customerCity.get();
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity.set(customerCity);
    }

    public String getCustomerZip() {
        return customerZip.get();
    }

    public void setCustomerZip(String customerZip) {
        this.customerZip.set(customerZip);
    }

    public String getCustomerPhone() {
        return customerPhone.get();
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone.set(customerPhone);
    }

    public boolean isActive() {
        return isActive.get();
    }

    public void setActive(boolean customerActive) {
        this.isActive.set(customerActive);
    }
}
