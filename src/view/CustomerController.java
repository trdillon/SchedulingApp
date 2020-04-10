package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import model.CustomerDB;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML
    private Button custBtnAdd;

    @FXML
    private Button custBtnMod;

    @FXML
    private Button custBtnDelete;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> customerName;

    @FXML
    private TableColumn<Customer, String> customerAddress;

    @FXML
    private TableColumn<Customer, String> customerCity;

    @FXML
    private TableColumn<Customer, String> customerPhone;

    private Customer currCustomer;


    @FXML
    void handleCustAdd(ActionEvent event) {
        try {
            FXMLLoader custAddLoader = new FXMLLoader(CustomerAddController.class.getResource("CustomerAdd.fxml"));
            Parent custAddScreen = custAddLoader.load();
            Scene custAddScene = new Scene(custAddScreen);
            Stage custAddStage = new Stage();

            custAddStage.setTitle("Add a customer");
            custAddStage.setScene(custAddScene);
            custAddStage.setResizable(false);
            custAddStage.show();

            Stage custStage = (Stage) custBtnAdd.getScene().getWindow();
            custStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCustMod(ActionEvent event) {
        currCustomer = customerTable.getSelectionModel().getSelectedItem();
        if(currCustomer != null) {
            try {
                FXMLLoader custModLoader = new FXMLLoader(CustomerModController.class.getResource("CustomerMod.fxml"));
                Parent custModScreen = custModLoader.load();
                Scene custModScene = new Scene(custModScreen);
                Stage custModStage = new Stage();

                custModStage.setTitle("Modify a customer");
                custModStage.setScene(custModScene);
                custModStage.setResizable(false);
                custModStage.show();

                Stage custStage = (Stage) custBtnMod.getScene().getWindow();
                custStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Alert appAlert = new Alert(Alert.AlertType.ERROR);
            appAlert.setTitle("Modification Error");
            appAlert.setHeaderText("Unable to modify the customer.");
            appAlert.setContentText("There was no customer selected to modify.");
            appAlert.showAndWait();
        }
    }

    @FXML
    void handleCustDel(ActionEvent event) {
        Alert confirmDel = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDel.setTitle("Delete customer");
        confirmDel.setHeaderText("Are you sure you want to delete this customer?");
        confirmDel.setContentText("Press OK to delete the customer. \nPress cancel to return to the previous screen.");
        confirmDel.showAndWait();

        if(confirmDel.getResult() == ButtonType.OK) {
            try {
                currCustomer = customerTable.getSelectionModel().getSelectedItem();
                CustomerDB.deleteCustomer(currCustomer.getCustomerId());
                getCustomers();
            }
            catch (NullPointerException e) {
                Alert appAlert = new Alert(Alert.AlertType.ERROR);
                appAlert.setTitle("Deletion Error");
                appAlert.setHeaderText("Unable to delete the customer.");
                appAlert.setContentText("There was no customer selected to delete.");
                appAlert.showAndWait();
            }
        }
        else {
                confirmDel.close();
        }
    }

    //Populate the customer table
    public void getCustomers() {
        customerTable.setItems(CustomerDB.getAllCustomers());

    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerCity.setCellValueFactory(new PropertyValueFactory<>("customerCity"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        getCustomers();
    }

}
