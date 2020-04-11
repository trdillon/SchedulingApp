package view_controller;

import dao.AppointmentDB;
import dao.UserDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    ResourceBundle rb;
    Locale userLocale;

    @FXML
    private TextField userText;

    @FXML
    private PasswordField passText;

    @FXML
    private Label userLabel;

    @FXML
    private Label passLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loginButton;

    private String errorHeader;
    private String errorTitle;
    private String errorText;
//TODO - Test the 15 min upcoming appointment reminder
    //Handle the login and check for upcoming appointments upon login
    @FXML
    public void handleLogin(ActionEvent event) {
        String username = userText.getText();
        String password = passText.getText();
        boolean validUser = UserDB.login(username, password);

        if(validUser) {
            try {
                Appointment app15 = AppointmentDB.getAppAlert();
                if (!(app15.getAppointmentId() == 0)) {
                    Alert appAlert = new Alert(Alert.AlertType.INFORMATION);
                    appAlert.setTitle("Appointment Reminder");
                    appAlert.setHeaderText("Upcoming appointment soon.");
                    appAlert.setContentText("There is an upcoming appointment:"
                            + "\non " + app15.getStart().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
                            + "\nat " + app15.getStart().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL))
                            + "\nwith " + app15.getCustomer().getCustomerName() + ".");
                    appAlert.showAndWait();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(errorTitle);
            alert.setHeaderText(errorHeader);
            alert.setContentText(errorText);
            alert.showAndWait();
        }
    }

    //Get language data for localization
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userLocale = Locale.getDefault();
        this.rb = ResourceBundle.getBundle("language/lang", this.userLocale);
        this.userLabel.setText(this.rb.getString("username"));
        this.passLabel.setText(this.rb.getString("password"));
        this.loginButton.setText(this.rb.getString("login"));
        this.messageLabel.setText(this.rb.getString("message"));
        this.errorHeader = this.rb.getString("errorHeader");
        this.errorTitle = this.rb.getString("errorTitle");
        this.errorText = this.rb.getString("errorText");
    }
}
