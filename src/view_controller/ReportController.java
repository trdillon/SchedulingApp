package view_controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.ResourceBundle;

import static util.DBConnection.CONN;

public class ReportController implements Initializable {

    @FXML
    private TextArea reportConsultant;

    @FXML
    private TextArea reportCustomer;

    @FXML
    private TextArea reportMonth;

    public void setReportConsultant() {
        try {
            Statement statement = CONN.createStatement();
            String query = "SELECT appointment.contact, appointment.type, customer.customerName, start, end " +
                    "FROM appointment JOIN customer ON customer.customerId = appointment.customerId " +
                    "GROUP BY appointment.contact, MONTH(start), start";
            ResultSet results = statement.executeQuery(query);

            StringBuilder reportText = new StringBuilder();
            reportText.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n",
                    "Consultant", "Appointment Type", "Customer", "Start", "End"));
            reportText.append(String.join("", Collections.nCopies(110, "_")));
            reportText.append("\n");

            while(results.next()) {
                reportText.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n",
                        results.getString("contact"), results.getString("type"),
                        results.getString("customerName"),
                        results.getString("start"), results.getString("end")));
            }

            statement.close();
            reportConsultant.setText(reportText.toString());
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    public void setReportCustomer() {
        try {
            Statement statement = CONN.createStatement();
            String query = "SELECT customer.customerName, COUNT(*) as 'Total' " +
                    "FROM customer JOIN appointment ON customer.customerId = appointment.customerId " +
                    "GROUP BY customerName";
            ResultSet results = statement.executeQuery(query);

            StringBuilder reportText = new StringBuilder();
            reportText.append(String.format("%1$-65s %2$-65s \n",
                    "Customer", "Total"));
            reportText.append(String.join("", Collections.nCopies(110, "_")));
            reportText.append("\n");

            while(results.next()) {
                reportText.append(String.format("%1$s %2$65d \n",
                        results.getString("customerName"), results.getInt("Total")));
            }

            statement.close();
            reportCustomer.setText(reportText.toString());
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    public void setReportMonth() {
        try {
            Statement statement = CONN.createStatement();
            String query = "SELECT type, MONTHNAME(start) as 'Month', COUNT(*) as 'Total' " +
                    "FROM appointment " +
                    "GROUP BY type, MONTH(start)";
            ResultSet results = statement.executeQuery(query);

            StringBuilder reportText = new StringBuilder();
            reportText.append(String.format("%1$-55s %2$-55s %3$s \n", "Month", "Appointment Type", "Total"));
            reportText.append(String.join("", Collections.nCopies(110, "_")));
            reportText.append("\n");

            while(results.next()) {
                reportText.append(String.format("%1$-55s %2$-60s %3$d \n",
                        results.getString("Month"), results.getString("type"),
                        results.getInt("Total")));
            }

            statement.close();
            reportMonth.setText(reportText.toString());
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        setReportConsultant();
        setReportCustomer();
        setReportMonth();
    }
}