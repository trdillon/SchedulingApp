package view_controller;

import dao.AddressDB;
import dao.CityDB;
import dao.CountryDB;
import dao.CustomerDB;
import exception.CustomerException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Address;
import model.City;
import model.Country;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static view_controller.CustomerController.currCustomer;

public class CustomerModController implements Initializable {

    @FXML
    private TextField customerName;

    @FXML
    private TextField address;

    @FXML
    private TextField address2;

    @FXML
    private TextField city;

    @FXML
    private ComboBox<Country> country;

    @FXML
    private TextField postalCode;

    @FXML
    private TextField phone;

    @FXML
    private Button custModBtnCancel;

    @FXML
    private Button custModBtnSave;

    private final Address currAddress = AddressDB.getAddressObj(currCustomer.getCustomerAddressId());
    private final City currCity = CityDB.getCityObj(currAddress.getCityId());
    private final Country currCountry = CountryDB.getCountryObj(currCity.getCountryId());

    @FXML
    void handleUpdate() {
        try {
            getCustData();
            if(Customer.isValidCust(currCustomer, currAddress, currCity, currCountry)) {
                CustomerDB.updateCustomer(currCustomer);
                AddressDB.updateAddress(currAddress);
                CityDB.updateCity(currCity);
                FXMLLoader loader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currStage = (Stage) custModBtnSave.getScene().getWindow();
                currStage.close();
            }
        }
        catch (CustomerException | IOException e) {
            Alert appAlert = new Alert(Alert.AlertType.ERROR);
            appAlert.setTitle("Error");
            appAlert.setHeaderText("There was an error processing the save request.");
            appAlert.setContentText(e.getMessage());
            appAlert.showAndWait();
        }
    }

    @FXML
    void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage currStage = (Stage) custModBtnCancel.getScene().getWindow();
            currStage.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Populate the customer data to pass to the add handler
    public void getCustData() {
        currCustomer.setCustomerName(customerName.getText());
        currAddress.setAddress(address.getText());
        currAddress.setAddress2(address2.getText());
        currAddress.setPostalCode(postalCode.getText());
        currAddress.setPhone(phone.getText());
        currCity.setCity(city.getText());
        currCity.setCountryId(country.getSelectionModel().getSelectedItem().getCountryId());
    }

    //Populate the GUI with app data
    public void setCustData() {
        customerName.setText(currCustomer.getCustomerName());
        address.setText(currAddress.getAddress());
        address2.setText(currAddress.getAddress2());
        postalCode.setText(currAddress.getPostalCode());
        phone.setText(currAddress.getPhone());
        city.setText(currCity.getCity());
        country.setValue(currCountry);
    }

    //Get the country string from country obj to populate the combo box
    public void countryStringName() {
        country.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country country) {
                return country.getCountry();
            }

            @Override
            public Country fromString(String string) {
                return country.getValue();
            }
        });
    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        country.setItems(CountryDB.getCountries());
        countryStringName();
        setCustData();
    }
}