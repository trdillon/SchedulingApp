package view_controller;

import dao.AppointmentDB;
import dao.CustomerDB;
import exception.AppointmentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static dao.UserDB.activeUser;
import static view_controller.AppointmentController.currAppointment;

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
    private static final ZoneId zid = ZoneId.systemDefault();

    private final ObservableList<Customer> customers = CustomerDB.getAllCustomers();

    private final ObservableList<String> contacts = FXCollections.observableArrayList("Raphael",
            "Michelangelo", "Leonardo", "Donatello");

    private final ObservableList<String> types = FXCollections.observableArrayList("Initial Consultation",
            "Risk Assessment", "Asset Management");

    private final ObservableList<String> locations = FXCollections.observableArrayList("New York", "Phoenix",
            "London");

    @FXML
    void handleUpdate() {
        try {
            getAppData();
            app.isValidApp();
            app.isOverlapping();
            if(app.isValidApp() && app.isOverlapping()) {
                AppointmentDB.updateAppointment(app);
                FXMLLoader loader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currStage = (Stage) appModBtnSave.getScene().getWindow();
                currStage.close();
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
    void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage currStage = (Stage) appModBtnCancel.getScene().getWindow();
            currStage.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Populate the app data to pass to the mod handler
    public void getAppData() {
        app.setCustomerId(customer.getValue().getCustomerId());
        app.setUserId(activeUser.getUserId());
        app.setTitle(title.getText());
        app.setDescription(description.getText());
        app.setLocation(location.getValue());
        app.setContact(contact.getValue());
        app.setType(type.getValue());
        app.setStart(ZonedDateTime.of(LocalDate.parse(date.getValue().toString(), formatDate), LocalTime.parse(start.getValue().toString(), formatTime), zid));
        app.setEnd(ZonedDateTime.of(LocalDate.parse(date.getValue().toString(), formatDate), LocalTime.parse(end.getValue().toString(), formatTime), zid));
        app.setAppointmentId(currAppointment.getAppointmentId());
    }

    //Populate the GUI with app data
    public void setAppData() {
        customer.setValue(currAppointment.getCustomer());
        title.setText(currAppointment.getTitle());
        description.setText(currAppointment.getDescription());
        location.setValue(currAppointment.getLocation());
        contact.setValue(currAppointment.getContact());
        type.setValue(currAppointment.getType());
        date.setValue(currAppointment.getStart().toLocalDate());
        start.setValueFactory(valStart);
        valStart.setValue(currAppointment.getStart().toLocalTime());
        end.setValueFactory(valEnd);
        valEnd.setValue(currAppointment.getEnd().toLocalTime());
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
        setAppData();
        contact.setItems(contacts);
        type.setItems(types);
        location.setItems(locations);
    }
}