package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane bp;

    @FXML
    private Button navBtnAppt;

    @FXML
    private Button navBtnCustomer;

    @FXML
    private Button navBtnReport;

    @FXML
    private Button navBtnLog;

    @FXML
    private Button navBtnExit;

    @FXML
    private void navAppt(MouseEvent event) throws IOException {
        loadScreen("Appointment");
    }

    @FXML
    private void navCustomer(MouseEvent event) throws IOException {
        loadScreen("Customer");
    }

    @FXML
    private void navReport(MouseEvent event) throws IOException {
        loadScreen("Report");
    }

    @FXML
    private void navLog(ActionEvent event) throws IOException {
        loadScreen("Login");
    }

    @FXML
    private void navExit(ActionEvent event){
        Stage stage = (Stage) navBtnExit.getScene().getWindow();
        stage.close();
    }

    public void loadScreen(String screen) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource(screen + ".fxml"));
        bp.setCenter(root);
    }
}