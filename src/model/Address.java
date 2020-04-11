package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Address {

    private IntegerProperty addressId = new SimpleIntegerProperty();
    private StringProperty address = new SimpleStringProperty();
    private StringProperty address2 = new SimpleStringProperty();
    private IntegerProperty cityId = new SimpleIntegerProperty();
    private StringProperty postalCode = new SimpleStringProperty();
    private StringProperty phone = new SimpleStringProperty();

    public Address() {
    }

    public int getAddressId() {
        return addressId.get();
    }

    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty getAddressProperty() {
        return this.address;
    }

    public String getAddress2() {
        return address2.get();
    }

    public void setAddress2(String address2) {
        this.address2.set(address2);
    }

    public int getCityId() {
        return cityId.get();
    }

    public void setCityId(int cityId) {
        this.cityId.set(cityId);
    }

    public IntegerProperty getCityIdProperty() {
        return this.cityId;
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public StringProperty getPhoneProperty() {
        return this.phone;
    }
}