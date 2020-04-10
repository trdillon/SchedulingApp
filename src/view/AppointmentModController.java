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
import static view.AppointmentController.currAppointment;

public class AppointmentModController implements Initializable {

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
    private Button appModBtnSave;

    @FXML
    private Button appModBtnCancel;

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

//TODO - SQL Exception: Cannot add or update a child row: a foreign key constraint fails (`U04IR7`.`appointment`, CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`))
    @FXML
    void handleUpdate(ActionEvent event) {
        try {
            modAppInfo();
            app.isValidApp();
            app.isOverlapping();
            if(app.isValidApp() && app.isOverlapping()) {
                AppointmentDB.updateAppointment(app);
                FXMLLoader appLoader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
                Parent appScreen = appLoader.load();
                Scene appScene = new Scene(appScreen);
                Stage appStage = new Stage();
                appStage.setScene(appScene);
                appStage.show();
                Stage appModStage = (Stage) appModBtnSave.getScene().getWindow();
                appModStage.close();
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
    void handleCancel(ActionEvent event) {
        try {
            FXMLLoader appLoader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
            Parent appScreen = appLoader.load();
            Scene appScene = new Scene(appScreen);
            Stage appStage = new Stage();
            appStage.setScene(appScene);
            appStage.show();
            Stage appAddStage = (Stage) appModBtnCancel.getScene().getWindow();
            appAddStage.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setSelectedApp() {
        customer.setValue(currAppointment.getCustomer());
        title.setText(currAppointment.getAppTitle());
        description.setText(currAppointment.getAppDesc());
        location.setValue(currAppointment.getAppLocation());
        contact.setValue(currAppointment.getAppContact());
        type.setValue(currAppointment.getAppType());
        date.setValue(currAppointment.getStart().toLocalDate());
        start.setValueFactory(valStart);
        valStart.setValue(currAppointment.getStart().toLocalTime());
        end.setValueFactory(valEnd);
        valEnd.setValue(currAppointment.getEnd().toLocalTime());
    }

    public void modAppInfo() {
        app.setCustomerId(customer.getValue().getCustomerId());
        app.setUserId(activeUser.getUserId());
        app.setAppTitle(title.getText());
        app.setAppDesc(description.getText());
        app.setAppLocation(location.getValue());
        app.setAppContact(contact.getValue());
        app.setAppType(type.getValue());
        app.setStart(ZonedDateTime.of(LocalDate.parse(date.getValue().toString(), formatDate), LocalTime.parse(start.getValue().toString(), formatTime), zid));
        app.setEnd(ZonedDateTime.of(LocalDate.parse(date.getValue().toString(), formatDate), LocalTime.parse(end.getValue().toString(), formatTime), zid));
        app.setAppointmentId(currAppointment.getAppointmentId());
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

    //Set the SVF increments for the start/end fields
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
        setSelectedApp();
        contact.setItems(contacts);
        type.setItems(types);
        location.setItems(locations);
    }

}
