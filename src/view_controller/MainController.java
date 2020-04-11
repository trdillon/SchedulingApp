package view_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane bp;

    @FXML
    private Button navBtnExit;

    @FXML
    private void navAppt() throws IOException {
        loadScreen("Appointment");
    }

    @FXML
    private void navCustomer() throws IOException {
        loadScreen("Customer");
    }

    @FXML
    private void navReport() throws IOException {
        loadScreen("Report");
    }

    @FXML
    private void navLog() {
        try {
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "audit_log.txt");
            pb.start();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navExit(){
        Stage stage = (Stage) navBtnExit.getScene().getWindow();
        stage.close();
    }

    public void loadScreen(String screen) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource(screen + ".fxml"));
        bp.setCenter(root);
    }
}