package view_controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;

import static util.DBConnection.CONN;

public class SearchController {

    @FXML
    private TableView tblConsultant;

    @FXML
    private TableView tblCustomer;

    @FXML
    private TextField txtConsultantSearch;

    @FXML
    private TextField txtCustomerSearch;

    private ObservableList<ObservableList> consultantResults;
    private ObservableList<ObservableList> customerResults;

    @FXML
    private void handleConsultant() {
        tblConsultant.getItems().clear();
        tblConsultant.getColumns().clear();
        searchConsultant();
    }

    @FXML
    private void handleCustomer() {
        tblCustomer.getItems().clear();
        tblCustomer.getColumns().clear();
        searchCustomer();
    }

    public void searchConsultant() {
        consultantResults = FXCollections.observableArrayList();
        try {
            String query = "SELECT appointment.contact, appointment.type, customer.customerName, start, end " +
                    "FROM appointment LEFT OUTER JOIN customer ON customer.customerId = appointment.customerId " +
                    "WHERE appointment.contact LIKE '%" + txtConsultantSearch.getText() + "%'";
            ResultSet rs = CONN.createStatement().executeQuery(query);

            //Create the table columns
            for(int i = 0 ; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tblConsultant.getColumns().addAll(col);
            }

            //Add the data to an observable list
            while(rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i = 1 ; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                consultantResults.add(row);
            }

            //Populate the tableView
            tblConsultant.setItems(consultantResults);
        }
        catch(SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    public void searchCustomer() {
        customerResults = FXCollections.observableArrayList();
        try {
            String query = "SELECT customer.customerName, appointment.contact, appointment.type, start, end " +
                    "FROM appointment LEFT OUTER JOIN customer ON customer.customerId = appointment.customerId " +
                    "WHERE customer.customerName LIKE '%" + txtCustomerSearch.getText() + "%'";
            ResultSet rs = CONN.createStatement().executeQuery(query);

            //Create the table columns
            for(int i = 0 ; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tblCustomer.getColumns().addAll(col);
            }

            //Add the data to an observable list
            while(rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i = 1 ; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                customerResults.add(row);
            }

            //Populate the tableView
            tblCustomer.setItems(customerResults);
        }
        catch(SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }
}