package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import model.UserDB;

import java.io.IOException;
import java.net.URL;
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

    //Handle the login
    @FXML
    public void handleLogin(ActionEvent eventLogin) throws IOException {
        String username = userText.getText();
        String password = passText.getText();
        activeUser.setUserName(username);
        boolean isValidUser = UserDB.login(username, password);

        if(isValidUser){
            ((Node) (eventLogin.getSource())).getScene().getWindow().hide();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(errorTitle);
            alert.setHeaderText(errorHeader);
            alert.setContentText(errorText);
            alert.showAndWait();
        }
    }

    //Create an active user object
    public static User activeUser = new User();

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
