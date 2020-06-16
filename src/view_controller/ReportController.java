package view_controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import util.DateFormatter;

import static util.DBConnection.CONN;

public class ReportController {

    @FXML
    private TableView tvConsultant;

    @FXML
    private TableView tvCustomer;

    @FXML
    private TableView tvMonth;

    @FXML
    private Tab tabConsultant;

    @FXML
    private Tab tabCustomer;

    @FXML
    private Tab tabMonth;

    @FXML
    private Label lblConsultant;

    @FXML
    private Label lblConsultantTime;

    @FXML
    private Label lblCustomerTime;

    @FXML
    private Label lblMonth;

    @FXML
    private Label lblMonthTime;

    private ObservableList<ObservableList> consultantData;
    private ObservableList<ObservableList> customerData;
    private ObservableList<ObservableList> monthData;


    @FXML
    private void handleCreateReport() {
        if(tabConsultant.isSelected()) {
            tvConsultant.getItems().clear();
            tvConsultant.getColumns().clear();
            lblConsultant.setVisible(true);
            lblConsultantTime.setText(DateFormatter.getDTFNow());
            lblConsultantTime.setVisible(true);
            createConsultantReport();
        }
        else if(tabCustomer.isSelected()) {

        }
        else if(tabMonth.isSelected()) {

        }
    }

    public void createConsultantReport() {
        consultantData = FXCollections.observableArrayList();
        try {
            String query = "SELECT appointment.contact, appointment.type, customer.customerName, start, end " +
                    "FROM appointment LEFT JOIN customer ON appointment.customerId = customer.customerId " +
                    "ORDER BY appointment.contact";
            ResultSet rs = CONN.createStatement().executeQuery(query);

            //Create the table columns
            for(int i = 0 ; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tvConsultant.getColumns().addAll(col);
            }

            //Add the data to an observable list
            while(rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i = 1 ; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                consultantData.add(row);
            }

            //Populate the tableView
            tvConsultant.setItems(consultantData);
        }
        catch(SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    public void createCustomerReport() {

    }

    public void createMonthReport() {

    }



/*
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
*/
}