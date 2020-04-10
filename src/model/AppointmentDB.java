package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static model.UserDB.activeUser;


public class AppointmentDB {

    public static ZoneId zid = ZoneId.systemDefault();

    //Create a list of appointments by the week
    public static ObservableList<Appointment> getAppByWeek () {
        ObservableList<Appointment> appWeek = FXCollections.observableArrayList();
        String getApp = "SELECT customer.*, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId "
                + "WHERE start BETWEEN NOW() AND (SELECT ADDDATE(NOW(), INTERVAL 7 DAY))";

        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(getApp);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Customer currCustomer = CustomerDB.getCustomerByID(rs.getInt("customerId"));
                Appointment currApps = new Appointment();

                currApps.setCustomer(currCustomer);
                currApps.setAppointmentId(rs.getInt("appointmentId"));
                currApps.setCustomerId(rs.getInt("customerId"));
                currApps.setUserId(rs.getInt("userId"));
                currApps.setAppTitle(rs.getString("title"));
                currApps.setAppDesc(rs.getString("description"));
                currApps.setAppLocation(rs.getString("location"));
                currApps.setAppContact(rs.getString("contact"));
                currApps.setAppType(rs.getString("type"));

                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zid);

                currApps.setStart(startLocal);
                currApps.setEnd(endLocal);
                appWeek.add(currApps);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appWeek;
    }

    //Create a list of appointments by the month
    public static ObservableList<Appointment> getAppByMonth () {
        ObservableList<Appointment> appMonth = FXCollections.observableArrayList();
        String getApp = "SELECT customer.*, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId "
                + "WHERE start BETWEEN NOW() AND (SELECT LAST_DAY(NOW()))";

        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(getApp);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Customer currCustomer = CustomerDB.getCustomerByID(rs.getInt("customerId"));
                Appointment currApps = new Appointment();

                currApps.setCustomer(currCustomer);
                currApps.setAppointmentId(rs.getInt("appointmentId"));
                currApps.setCustomerId(rs.getInt("customerId"));
                currApps.setUserId(rs.getInt("userId"));
                currApps.setAppTitle(rs.getString("title"));
                currApps.setAppDesc(rs.getString("description"));
                currApps.setAppLocation(rs.getString("location"));
                currApps.setAppContact(rs.getString("contact"));
                currApps.setAppType(rs.getString("type"));

                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zid);

                currApps.setStart(startLocal);
                currApps.setEnd(endLocal);
                appMonth.add(currApps);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appMonth;
    }

    //Get an appointment by ID
    public Appointment getAppByID(int appID) {
        String getApp = "SELECT customer.customerId, customer.customerName, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId "
                + "WHERE appointmentId = ?";
        Appointment currApp = new Appointment();
        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(getApp);
            stmt.setInt(1, appID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer currCustomer = new Customer();
                currCustomer.setCustomerId(rs.getInt("customerId"));
                currCustomer.setCustomerName(rs.getString("customerName"));
                currApp.setCustomer(currCustomer);
                currApp.setCustomerId(rs.getInt("customerId"));
                currApp.setUserId(rs.getInt("userId"));
                currApp.setAppTitle(rs.getString("title"));
                currApp.setAppDesc(rs.getString("description"));
                currApp.setAppLocation(rs.getString("location"));
                currApp.setAppContact(rs.getString("contact"));
                currApp.setAppType(rs.getString("type"));

                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zid);

                currApp.setStart(startLocal);
                currApp.setEnd(endLocal);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return currApp;
    }

    //Get an appointment by user
    public static ObservableList<Appointment> getAppByUser() {
        ObservableList<Appointment> appUser = FXCollections.observableArrayList();
        String getApps = "SELECT user.userId, customer.customerId, appointment.start FROM user "
                + "JOIN appointment ON user.userId = appointment.userId "
                + "JOIN customer ON appointment.customerId = customer.customerId";

        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(getApps);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Appointment currApps = new Appointment();
                currApps.setUserId(rs.getInt("userId"));
                currApps.setCustomerId(rs.getInt("customerId"));

                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);

                currApps.setStart(startLocal);
                appUser.add(currApps);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appUser;
    }

    //Get appointments upcoming in the next 15 minutes for alert
    public static Appointment getApp15() {
        String getApps = "SELECT customer.customerName, appointment.* FROM appointment "
                + "JOIN customer ON appointment.customerId = customer.customerId "
                + "WHERE (start BETWEEN ? AND ADDTIME(NOW(), '00:15:00'))";

        Appointment currApp = new Appointment();
        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(getApps);
            ZonedDateTime lzt = ZonedDateTime.now(zid);
            ZonedDateTime zdtUTC = lzt.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDateTime localUTC = zdtUTC.toLocalDateTime();
            stmt.setTimestamp(1, Timestamp.valueOf(localUTC));
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Customer currCustomer = new Customer();
                currCustomer.setCustomerName(rs.getString("customerName"));
                currApp.setCustomer(currCustomer);
                currApp.setAppointmentId(rs.getInt("appointmentId"));
                currApp.setCustomerId(rs.getInt("customerId"));
                currApp.setUserId(rs.getInt("userId"));
                currApp.setAppTitle(rs.getString("title"));

                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);
                currApp.setStart(startLocal);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return currApp;
    }

    //Check for overlapping appointments
    public static ObservableList<Appointment> getOverlappingApps(LocalDateTime start, LocalDateTime end) {
        ObservableList<Appointment> appOver = FXCollections.observableArrayList();
        String getApps = "SELECT * FROM appointment "
                + "WHERE (start >= ? AND end <= ?) "
                + "OR (start <= ? AND end >= ?) "
                + "OR (start BETWEEN ? AND ? OR end BETWEEN ? AND ?)";

        try {
            LocalDateTime startLDT = start.atZone(zid).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endLDT = end.atZone(zid).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(getApps);
            stmt.setTimestamp(1, Timestamp.valueOf(startLDT));
            stmt.setTimestamp(2, Timestamp.valueOf(endLDT));
            stmt.setTimestamp(3, Timestamp.valueOf(startLDT));
            stmt.setTimestamp(4, Timestamp.valueOf(endLDT));
            stmt.setTimestamp(5, Timestamp.valueOf(startLDT));
            stmt.setTimestamp(6, Timestamp.valueOf(endLDT));
            stmt.setTimestamp(7, Timestamp.valueOf(startLDT));
            stmt.setTimestamp(8, Timestamp.valueOf(endLDT));
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Appointment currApp = new Appointment();
                currApp.setAppointmentId(rs.getInt("appointmentId"));
                currApp.setAppTitle(rs.getString("title"));
                currApp.setAppDesc(rs.getString("description"));
                currApp.setAppLocation(rs.getString("location"));
                currApp.setAppContact(rs.getString("contact"));
                currApp.setAppType(rs.getString("type"));

                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zid);

                currApp.setStart(startLocal);
                currApp.setEnd(endLocal);
                appOver.add(currApp);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appOver;
    }

    //Get the last used appointment ID
    private static int getLastAppID() {
        int lastAppID = 0;
        String getApp = "SELECT MAX(appointmentId) FROM appointment";

        try {
            Statement stmt = DBConnection.getConn().createStatement();
            ResultSet rs = stmt.executeQuery(getApp);

            if(rs.next()) {
                lastAppID = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return (lastAppID + 1);
    }

    //Add an appointment
    public static Appointment addAppointment(Appointment appointment) {
        String addApp = String.join(" ",
                "INSERT INTO appointment (appointmentId, customerId, userId, title, "
                + "description, location, contact, type, url, start, end, "
                + "createDate, createdBy, lastUpdate, lastUpdateBy) ",
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ' ', ?, ?, NOW(), ?, NOW(), ?)");

        int appointmentId = getLastAppID();
        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(addApp);
            stmt.setInt(1, appointmentId);
            stmt.setObject(2, appointment.getCustomerId());
            stmt.setObject(3, appointment.getUserId());
            stmt.setObject(4, appointment.getAppTitle());
            stmt.setObject(5, appointment.getAppDesc());
            stmt.setObject(6, appointment.getAppLocation());
            stmt.setObject(7, appointment.getAppContact());
            stmt.setObject(8, appointment.getAppType());

            ZonedDateTime startZDT = appointment.getStart().withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endZDT = appointment.getEnd().withZoneSameInstant(ZoneId.of("UTC"));
            stmt.setTimestamp(9, Timestamp.valueOf(startZDT.toLocalDateTime()));
            stmt.setTimestamp(10, Timestamp.valueOf(endZDT.toLocalDateTime()));

            stmt.setString(11, activeUser.getUserName());
            stmt.setString(12, activeUser.getUserName());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appointment;
    }

    //Update an appointment
    public static void updateAppointment(Appointment appointment) {
        String modApp = String.join(" ",
                "UPDATE appointment",
                "SET customerId = ?, userId = ?, title = ?, description = ?, location = ?, "
                + "contact = ?, type = ?, start = ?, end = ?, lastUpdate = NOW(), lastUpdateBy = ?",
                "WHERE appointmentId = ?");

        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(modApp);
            stmt.setObject(1, appointment.getCustomerId());
            stmt.setObject(2, appointment.getUserId());
            stmt.setObject(3, appointment.getAppTitle());
            stmt.setObject(4, appointment.getAppDesc());
            stmt.setObject(5, appointment.getAppLocation());
            stmt.setObject(6, appointment.getAppContact());
            stmt.setObject(7, appointment.getAppType());

            ZonedDateTime startZDT = appointment.getStart().withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endZDT = appointment.getEnd().withZoneSameInstant(ZoneId.of("UTC"));
            stmt.setTimestamp(8, Timestamp.valueOf(startZDT.toLocalDateTime()));
            stmt.setTimestamp(9, Timestamp.valueOf(endZDT.toLocalDateTime()));

            stmt.setString(10, activeUser.getUserName());
            stmt.setObject(11, appointment.getAppointmentId());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Delete an appointment
    public static void deleteAppointment (Appointment appointment) {
        String delApp = "DELETE FROM appointment WHERE appointmentId = ?";

        try {
            PreparedStatement stmt = DBConnection.getConn().prepareStatement(delApp);
            stmt.setObject(1, appointment.getAppointmentId());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }
}
