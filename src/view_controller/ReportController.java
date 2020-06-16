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
    private Label lblCustomer;

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
            tvCustomer.getItems().clear();
            tvCustomer.getColumns().clear();
            lblCustomer.setVisible(true);
            lblCustomerTime.setText(DateFormatter.getDTFNow());
            lblCustomerTime.setVisible(true);
            createCustomerReport();
        }
        else if(tabMonth.isSelected()) {
            tvMonth.getItems().clear();
            tvMonth.getColumns().clear();
            lblMonth.setVisible(true);
            lblMonthTime.setText(DateFormatter.getDTFNow());
            lblMonthTime.setVisible(true);
            createMonthReport();
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
        customerData = FXCollections.observableArrayList();
        try {
            String query = "SELECT customer.customerName, appointment.contact, appointment.type, start, end " +
                    "FROM appointment LEFT JOIN customer ON appointment.customerId = customer.customerId " +
                    "ORDER BY customer.customerName";
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
                tvCustomer.getColumns().addAll(col);
            }

            //Add the data to an observable list
            while(rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i = 1 ; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                customerData.add(row);
            }

            //Populate the tableView
            tvCustomer.setItems(customerData);
        }
        catch(SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    public void createMonthReport() {
        monthData = FXCollections.observableArrayList();
        try {
            String query = "SELECT customer.customerName, appointment.contact, appointment.type, start, end " +
                    "FROM appointment LEFT JOIN customer ON appointment.customerId = customer.customerId " +
                    "ORDER BY start";
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
                tvMonth.getColumns().addAll(col);
            }

            //Add the data to an observable list
            while(rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i = 1 ; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                monthData.add(row);
            }

            //Populate the tableView
            tvMonth.setItems(monthData);
        }
        catch(SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }
}