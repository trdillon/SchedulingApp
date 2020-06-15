package view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static util.DBConnection.CONN;

public class SearchController {

    @FXML
    private TextField txtConsultantSearch;

    @FXML
    private TableView<Appointment> tblConsultantResults;

    @FXML
    private TextField txtCustomerSearch;


    public void searchConsultant() {
        String query = "SELECT appointment.contact, appointment.type, customer.customerName, start, end " +
                "FROM appointment JOIN customer ON customer.customerId = appointment.appointmentId " +
                "WHERE appointment.contact LIKE '% " + txtConsultantSearch.getText() + " %' ";
        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            ResultSet results = statement.executeQuery();
        }
        catch(SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }
}