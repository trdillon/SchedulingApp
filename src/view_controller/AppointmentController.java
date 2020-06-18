package view_controller;

import dao.AppointmentDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    @FXML
    private Button appBtnAdd;

    @FXML
    private Button appBtnMod;

    @FXML
    private Tab tpWeekly;

    @FXML
    private TableView<Appointment> appTableWeek;

    @FXML
    private TableColumn<Appointment, String> appWeekTitle;

    @FXML
    private TableColumn<Appointment, String> appWeekContact;

    @FXML
    private TableColumn<Appointment, String> appWeekLocation;

    @FXML
    private TableColumn<Appointment, String> appWeekStart;

    @FXML
    private TableColumn<Appointment, String> appWeekEnd;

    @FXML
    private TableColumn<Appointment, String> appWeekCustomer;

    @FXML
    private Tab tpMonthly;

    @FXML
    private TableView<Appointment> appTableMonth;

    @FXML
    private TableColumn<Appointment, String> appMonthTitle;

    @FXML
    private TableColumn<Appointment, String> appMonthContact;

    @FXML
    private TableColumn<Appointment, String> appMonthLocation;

    @FXML
    private TableColumn<Appointment, String> appMonthStart;

    @FXML
    private TableColumn<Appointment, String> appMonthEnd;

    @FXML
    private TableColumn<Appointment, String> appMonthCustomer;

    @FXML
    private Tab tpAll;

    @FXML
    private TableView<Appointment> appTableAll;

    @FXML
    private TableColumn<Appointment, String> appAllTitle;

    @FXML
    private TableColumn<Appointment, String> appAllContact;

    @FXML
    private TableColumn<Appointment, String> appAllLocation;

    @FXML
    private TableColumn<Appointment, String> appAllStart;

    @FXML
    private TableColumn<Appointment, String> appAllEnd;

    @FXML
    private TableColumn<Appointment, String> appAllCustomer;

    @FXML
    private final DateTimeFormatter formatDT = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a z");

    public static Appointment currAppointment;

    @FXML
    private void handleAppAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(AppointmentAddController.class.getResource("AppointmentAdd.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage currStage = (Stage) appBtnAdd.getScene().getWindow();
            currStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAppDel() {
        Alert confirmDel = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDel.setTitle("Delete appointment");
        confirmDel.setHeaderText("Are you sure you want to delete this appointment?");
        confirmDel.setContentText("Press OK to delete the appointment. \nPress cancel to return to the previous screen.");
        confirmDel.showAndWait();

        if(tpWeekly.isSelected()) {
            if(confirmDel.getResult() == ButtonType.OK) {
                try {
                    Appointment currApp = appTableWeek.getSelectionModel().getSelectedItem();
                    AppointmentDB.deleteAppointment(currApp);
                    getAppointments();
                }
                catch (NullPointerException e) {
                    Alert appAlert = new Alert(Alert.AlertType.ERROR);
                    appAlert.setTitle("Deletion Error");
                    appAlert.setHeaderText("Unable to delete the appointment.");
                    appAlert.setContentText("There was no appointment selected to delete.");
                    appAlert.showAndWait();
                }
            }
            else {
                confirmDel.close();
            }
        }
        else if(tpMonthly.isSelected()) {
            if(confirmDel.getResult() == ButtonType.OK) {
                try {
                    Appointment currApp = appTableMonth.getSelectionModel().getSelectedItem();
                    AppointmentDB.deleteAppointment(currApp);
                    getAppointments();
                }
                catch (NullPointerException e) {
                    Alert appAlert = new Alert(Alert.AlertType.ERROR);
                    appAlert.setTitle("Deletion Error");
                    appAlert.setHeaderText("Unable to delete the appointment.");
                    appAlert.setContentText("There was no appointment selected to delete.");
                    appAlert.showAndWait();
                }
            }
            else {
                confirmDel.close();
            }
        }
        else if(tpAll.isSelected()) {
            if(confirmDel.getResult() == ButtonType.OK) {
                try {
                    Appointment currApp = appTableAll.getSelectionModel().getSelectedItem();
                    AppointmentDB.deleteAppointment(currApp);
                    getAppointments();
                }
                catch (NullPointerException e) {
                    Alert appAlert = new Alert(Alert.AlertType.ERROR);
                    appAlert.setTitle("Deletion Error");
                    appAlert.setHeaderText("Unable to delete the appointment.");
                    appAlert.setContentText("There was no appointment selected to delete.");
                    appAlert.showAndWait();
                }
            }
            else {
                confirmDel.close();
            }
        }
    }

    @FXML
    void handleAppMod() {
        if(tpWeekly.isSelected()) {
            currAppointment = appTableWeek.getSelectionModel().getSelectedItem();
            if(currAppointment != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(AppointmentModController.class.getResource("AppointmentMod.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                    Stage currStage = (Stage) appBtnMod.getScene().getWindow();
                    currStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Alert appAlert = new Alert(Alert.AlertType.ERROR);
                appAlert.setTitle("Modification Error");
                appAlert.setHeaderText("Unable to modify the appointment.");
                appAlert.setContentText("There was no appointment selected to modify.");
                appAlert.showAndWait();
            }
        }
        else if(tpMonthly.isSelected()){
            currAppointment = appTableMonth.getSelectionModel().getSelectedItem();
            if(currAppointment != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(AppointmentModController.class.getResource("AppointmentMod.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                    Stage currStage = (Stage) appBtnMod.getScene().getWindow();
                    currStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Alert appAlert = new Alert(Alert.AlertType.ERROR);
                appAlert.setTitle("Modification Error");
                appAlert.setHeaderText("Unable to modify the appointment.");
                appAlert.setContentText("There was no appointment selected to modify.");
                appAlert.showAndWait();
            }
        }
        else if(tpAll.isSelected()){
            currAppointment = appTableAll.getSelectionModel().getSelectedItem();
            if(currAppointment != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(AppointmentModController.class.getResource("AppointmentMod.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                    Stage currStage = (Stage) appBtnMod.getScene().getWindow();
                    currStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Alert appAlert = new Alert(Alert.AlertType.ERROR);
                appAlert.setTitle("Modification Error");
                appAlert.setHeaderText("Unable to modify the appointment.");
                appAlert.setContentText("There was no appointment selected to modify.");
                appAlert.showAndWait();
            }
        }
    }

    public void getAppointments() {
        //Set the weekly table view columns
        appWeekTitle.setCellValueFactory(cellData -> cellData.getValue().getAppTitleProperty());
        appWeekContact.setCellValueFactory(cellData -> cellData.getValue().getAppContactProperty());
        appWeekLocation.setCellValueFactory(cellData -> cellData.getValue().getAppLocationProperty());
        appWeekCustomer.setCellValueFactory(cellData -> cellData.getValue().getCustomer().getCustomerNameProperty());
        appWeekStart.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStart().format(formatDT)));
        appWeekEnd.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnd().format(formatDT)));

        //Set the monthly table view columns
        appMonthTitle.setCellValueFactory(cellData -> cellData.getValue().getAppTitleProperty());
        appMonthContact.setCellValueFactory(cellData -> cellData.getValue().getAppContactProperty());
        appMonthLocation.setCellValueFactory(cellData -> cellData.getValue().getAppLocationProperty());
        appMonthCustomer.setCellValueFactory(cellData -> cellData.getValue().getCustomer().getCustomerNameProperty());
        appMonthStart.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStart().format(formatDT)));
        appMonthEnd.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnd().format(formatDT)));

        //Set the all table view columns
        appAllTitle.setCellValueFactory(cellData -> cellData.getValue().getAppTitleProperty());
        appAllContact.setCellValueFactory(cellData -> cellData.getValue().getAppContactProperty());
        appAllLocation.setCellValueFactory(cellData -> cellData.getValue().getAppLocationProperty());
        appAllCustomer.setCellValueFactory(cellData -> cellData.getValue().getCustomer().getCustomerNameProperty());
        appAllStart.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStart().format(formatDT)));
        appAllEnd.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnd().format(formatDT)));

        //Populate the table views
        appTableWeek.setItems(AppointmentDB.getAppByWeek());
        appTableMonth.setItems(AppointmentDB.getAppByMonth());
        appTableAll.setItems(AppointmentDB.getAppAll());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAppointments();
    }
}