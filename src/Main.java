import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.DBConnection;

public class Main extends Application {

    //Load the login screen
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view_controller/Login.fxml"));
        Scene scene = new Scene(root);

        stage.setTitle("Scheduling App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //Connect and disconnect from the DB
    public static void main(String[] args) {
        DBConnection.connect();
        launch(args);
        DBConnection.disconnect();
    }
}
