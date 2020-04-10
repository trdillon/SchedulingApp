package view;

import exception.AppointmentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import model.Appointment;
import model.AppointmentDB;
import model.Customer;
import model.CustomerDB;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static model.UserDB.activeUser;

public class AppointmentAddController implements Initializable {

    @FXML
    private ComboBox<Customer> customer;

    @FXML
    private TextField title;

    @FXML
    private ComboBox<String> contact;

    @FXML
    private TextArea description;

    @FXML
    private ComboBox<String> location;

    @FXML
    private ComboBox<String> type;

    @FXML
    private DatePicker date;

    @FXML
    private Spinner<LocalTime> start;

    @FXML
    private Spinner<LocalTime> end;

    @FXML
    private Button appAddBtnSave;

    @FXML
    private Button appAddBtnCancel;

    @FXML
    public static Appointment app = new Appointment();

    @FXML
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private static ZoneId zid = ZoneId.systemDefault();

    private final ObservableList<Customer> customers = CustomerDB.getAllCustomers();

    private final ObservableList<String> contacts = FXCollections.observableArrayList("Raphael",
            "Michelangelo", "Leonardo", "Donatello");

    private final ObservableList<String> types = FXCollections.observableArrayList("Initial Consultation",
            "Risk Assessment", "Asset Management");

    private final ObservableList<String> locations = FXCollections.observableArrayList("New York", "Phoenix",
            "London");

    @FXML
    void handleAppAdd(ActionEvent event) {
        try {
            getAppInfo();
            app.isValidApp();
            app.isOverlapping();
            if(app.isValidApp() && app.isOverlapping()) {
                AppointmentDB.addAppointment(app);
                FXMLLoader appLoader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
                Parent appScreen = appLoader.load();
                Scene appScene = new Scene(appScreen);
                Stage appStage = new Stage();
                appStage.setScene(appScene);
                appStage.show();
                Stage appAddStage = (Stage) appAddBtnSave.getScene().getWindow();
                appAddStage.close();
            }
        }
        catch (AppointmentException | IOException e) {
            Alert appAlert = new Alert(Alert.AlertType.ERROR);
            appAlert.setTitle("Error");
            appAlert.setHeaderText("There was an error processing the save request.");
            appAlert.setContentText(e.getMessage());
            appAlert.showAndWait();
        }
    }

    @FXML
    void handleAppCancel() {
        try {
                FXMLLoader appLoader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
                Parent appScreen = appLoader.load();
                Scene appScene = new Scene(appScreen);
                Stage appStage = new Stage();
                appStage.setScene(appScene);
                appStage.show();
                Stage appAddStage = (Stage) appAddBtnCancel.getScene().getWindow();
                appAddStage.close();
            }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Populate the app data to pass to the add handler
    public void getAppInfo() {
        try {
            app.setCustomer(customer.getValue());
            app.setCustomerId(customer.getValue().getCustomerId());
            app.setUserId(activeUser.getUserId());
            app.setAppTitle(title.getText());
            app.setAppContact(contact.getValue());
            app.setAppDesc(description.getText());
            app.setAppLocation(location.getValue());
            app.setAppType(type.getValue());
            app.setStart(ZonedDateTime.of(LocalDate.parse(date.getValue().toString(), formatDate),
                    LocalTime.parse(start.getValue().toString(), formatTime), zid));
            app.setEnd(ZonedDateTime.of(LocalDate.parse(date.getValue().toString(), formatDate),
                    LocalTime.parse(end.getValue().toString(), formatTime), zid));
        }
        catch (NullPointerException e) {
            Alert appAlert = new Alert(Alert.AlertType.ERROR);
            appAlert.setTitle("Error");
            appAlert.setHeaderText("There was an error processing the save request.");
            appAlert.setContentText(e.getMessage());
            appAlert.showAndWait();
        }
    }

    //Get the customerName string from customer obj to populate the combo box
    public void customerStringName() {
        customer.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer cust) {
                return cust.getCustomerName();
            }

            @Override
            public Customer fromString(String string) {
                return customer.getValue();
            }
        });
    }

    //Set the default time and SVF increments for the start/end fields
    public void setTime() {
        date.setValue(LocalDate.now());
        start.setValueFactory(valStart);
        valStart.setValue(LocalTime.of(8, 0));
        end.setValueFactory(valEnd);
        valEnd.setValue(LocalTime.of(17, 0));
    }

    SpinnerValueFactory valStart = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(formatTime, null));
        }
        @Override
        public void decrement(int steps) {
            LocalTime time = getValue();
            setValue(time.minusHours(steps));
            setValue(time.minusMinutes(16 - steps));
        }

        @Override
        public void increment(int steps) {
            LocalTime time = getValue();
            setValue(time.plusHours(steps));
            setValue(time.plusMinutes(steps + 14));
        }
    };

    SpinnerValueFactory valEnd = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(formatTime, null));
        }
        @Override
        public void decrement(int steps) {
            LocalTime time = getValue();
            setValue(time.minusHours(steps));
            setValue(time.minusMinutes(16 - steps));
        }

        @Override
        public void increment(int steps) {
            LocalTime time = getValue();
            setValue(time.plusHours(steps));
            setValue(time.plusMinutes(steps + 14));
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customer.setItems(customers);
        customerStringName();
        contact.setItems(contacts);
        type.setItems(types);
        location.setItems(locations);
        setTime();
    }
}
