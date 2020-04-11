package view_controller;

import dao.AppointmentDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
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
    private Button appBtnDelete;

    @FXML
    private TabPane tp;

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
    private final DateTimeFormatter formatDT = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a z");

    public static Appointment currAppointment;

    //Handle buttons
    @FXML
    private void handleAppAdd(ActionEvent event) {
        try {
            FXMLLoader appAddLoader = new FXMLLoader(AppointmentAddController.class.getResource("AppointmentAdd.fxml"));
            Parent appAddScreen = appAddLoader.load();
            Scene appAddScene = new Scene(appAddScreen);
            Stage appAddStage = new Stage();

            appAddStage.setTitle("Add an appointment");
            appAddStage.setScene(appAddScene);
            appAddStage.setResizable(false);
            appAddStage.show();

            Stage appStage = (Stage) appBtnAdd.getScene().getWindow();
            appStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAppDel(ActionEvent event) {
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
    }

    @FXML
    void handleAppMod(ActionEvent event) {
        if(tpWeekly.isSelected()) {
            currAppointment = appTableWeek.getSelectionModel().getSelectedItem();
            if(currAppointment != null) {
                try {
                    FXMLLoader appModLoader = new FXMLLoader(AppointmentModController.class.getResource("AppointmentMod.fxml"));
                    Parent appModScreen = appModLoader.load();
                    Scene appModScene = new Scene(appModScreen);
                    Stage appModStage = new Stage();

                    appModStage.setTitle("Modify an appointment");
                    appModStage.setScene(appModScene);
                    appModStage.setResizable(false);
                    appModStage.show();

                    Stage appStage = (Stage) appBtnMod.getScene().getWindow();
                    appStage.close();
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
                    FXMLLoader appModLoader = new FXMLLoader(AppointmentModController.class.getResource("AppointmentMod.fxml"));
                    Parent appModScreen = appModLoader.load();
                    Scene appModScene = new Scene(appModScreen);
                    Stage appModStage = new Stage();

                    appModStage.setTitle("Modify an appointment");
                    appModStage.setScene(appModScene);
                    appModStage.setResizable(false);
                    appModStage.show();

                    Stage appStage = (Stage) appBtnMod.getScene().getWindow();
                    appStage.close();
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
        //Set the weekly & monthly tables using lambda expressions for efficiency
        appWeekTitle.setCellValueFactory(cellData -> cellData.getValue().getAppTitleProperty());
        appWeekContact.setCellValueFactory(cellData -> cellData.getValue().getAppContactProperty());
        appWeekLocation.setCellValueFactory(cellData -> cellData.getValue().getAppLocationProperty());
        appWeekCustomer.setCellValueFactory(cellData -> cellData.getValue().getCustomer().getCustomerNameProperty());
        appWeekStart.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStart().format(formatDT)));

        appWeekEnd.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnd().format(formatDT)));

        appMonthTitle.setCellValueFactory(cellData -> cellData.getValue().getAppTitleProperty());
        appMonthContact.setCellValueFactory(cellData -> cellData.getValue().getAppContactProperty());
        appMonthLocation.setCellValueFactory(cellData -> cellData.getValue().getAppLocationProperty());
        appMonthCustomer.setCellValueFactory(cellData -> cellData.getValue().getCustomer().getCustomerNameProperty());
        appMonthStart.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStart().format(formatDT)));

        appMonthEnd.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnd().format(formatDT)));

        appTableWeek.setItems(AppointmentDB.getAppByWeek());
        appTableMonth.setItems(AppointmentDB.getAppByMonth());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAppointments();
    }
}
