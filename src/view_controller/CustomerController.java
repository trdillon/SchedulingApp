package view_controller;

import dao.CustomerDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML
    private Button custBtnAdd;

    @FXML
    private Button custBtnMod;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> customerName;

    public static Customer currCustomer;

    @FXML
    void handleCustAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(CustomerAddController.class.getResource("CustomerAdd.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage currStage = (Stage) custBtnAdd.getScene().getWindow();
            currStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCustMod() {
        currCustomer = customerTable.getSelectionModel().getSelectedItem();
        if(currCustomer != null) {
            try {
                FXMLLoader loader = new FXMLLoader(CustomerAddController.class.getResource("CustomerMod.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currStage = (Stage) custBtnMod.getScene().getWindow();
                currStage.close();
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
    void handleCustDel() {
        Alert confirmDel = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDel.setTitle("Delete customer");
        confirmDel.setHeaderText("Are you sure you want to delete this customer?");
        confirmDel.setContentText("Press OK to delete the customer. \nPress cancel to return to the previous screen.");
        confirmDel.showAndWait();

        if(confirmDel.getResult() == ButtonType.OK) {
            try {
                currCustomer = customerTable.getSelectionModel().getSelectedItem();
                CustomerDB.deleteCustomer(currCustomer);
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
        customerName.setCellValueFactory(cellData -> cellData.getValue().getCustomerNameProperty());
        customerTable.setItems(CustomerDB.getAllCustomers());
    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        getCustomers();
    }
}