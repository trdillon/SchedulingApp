package model;

import javafx.beans.property.*;

public class Customer {

    private IntegerProperty customerId = new SimpleIntegerProperty();
    private StringProperty customerName = new SimpleStringProperty();
    private IntegerProperty customerAddressId = new SimpleIntegerProperty();
    private StringProperty customerAddress = new SimpleStringProperty();
    private StringProperty customerCity = new SimpleStringProperty();
    private StringProperty customerZip = new SimpleStringProperty();
    private StringProperty customerPhone = new SimpleStringProperty();
    private BooleanProperty isActive = new SimpleBooleanProperty();

    //Constructors
    public Customer() {}
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

    public void setCustomerAddressIdId(int customerAddressId) {
        this.customerAddressId.set(customerAddressId);
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
