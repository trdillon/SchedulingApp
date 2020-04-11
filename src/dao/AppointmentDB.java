package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static dao.UserDB.activeUser;
import static util.DBConnection.CONN;


public class AppointmentDB {

    public static ZoneId zid = ZoneId.systemDefault();

    //Add an appointment
    public static Appointment addAppointment(Appointment appointment) {
        int appointmentId = getNextID();
        String query = String.join(" ",
                "INSERT INTO appointment (appointmentId, customerId, userId, title, "
                        + "description, location, contact, type, url, start, end, "
                        + "createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ' ', ?, ?, NOW(), ?, NOW(), ?)");

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, appointmentId);
            statement.setObject(2, appointment.getCustomerId());
            statement.setObject(3, appointment.getUserId());
            statement.setObject(4, appointment.getTitle());
            statement.setObject(5, appointment.getDescription());
            statement.setObject(6, appointment.getLocation());
            statement.setObject(7, appointment.getContact());
            statement.setObject(8, appointment.getType());

            ZonedDateTime startZDT = appointment.getStart().withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endZDT = appointment.getEnd().withZoneSameInstant(ZoneId.of("UTC"));
            statement.setTimestamp(9, Timestamp.valueOf(startZDT.toLocalDateTime()));
            statement.setTimestamp(10, Timestamp.valueOf(endZDT.toLocalDateTime()));

            statement.setString(11, activeUser.getUserName());
            statement.setString(12, activeUser.getUserName());
            statement.executeUpdate();
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
        String query = String.join(" ",
                "UPDATE appointment",
                "SET customerId = ?, userId = ?, title = ?, description = ?, location = ?, " +
                "contact = ?, type = ?, start = ?, end = ?, lastUpdate = NOW(), lastUpdateBy = ?",
                "WHERE appointmentId = ?");

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setObject(1, appointment.getCustomerId());
            statement.setObject(2, appointment.getUserId());
            statement.setObject(3, appointment.getTitle());
            statement.setObject(4, appointment.getDescription());
            statement.setObject(5, appointment.getLocation());
            statement.setObject(6, appointment.getContact());
            statement.setObject(7, appointment.getType());

            ZonedDateTime startZDT = appointment.getStart().withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endZDT = appointment.getEnd().withZoneSameInstant(ZoneId.of("UTC"));
            statement.setTimestamp(8, Timestamp.valueOf(startZDT.toLocalDateTime()));
            statement.setTimestamp(9, Timestamp.valueOf(endZDT.toLocalDateTime()));

            statement.setString(10, activeUser.getUserName());
            statement.setObject(11, appointment.getAppointmentId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Delete an appointment
    public static void deleteAppointment (Appointment appointment) {
        String query = "DELETE FROM appointment WHERE appointmentId = ?";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setObject(1, appointment.getAppointmentId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
    }

    //Get a list of appointments by the week
    public static ObservableList<Appointment> getAppByWeek () {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String query = "SELECT customer.*, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId "
                + "WHERE start BETWEEN NOW() AND (SELECT ADDDATE(NOW(), INTERVAL 7 DAY))";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            ResultSet results = statement.executeQuery();

            while(results.next()) {
                Customer customer = CustomerDB.getCustomerByID(results.getInt("customerId"));
                Appointment appointment = new Appointment();

                appointment.setCustomer(customer);
                appointment.setAppointmentId(results.getInt("appointmentId"));
                appointment.setCustomerId(results.getInt("customerId"));
                appointment.setUserId(results.getInt("userId"));
                appointment.setTitle(results.getString("title"));
                appointment.setDescription(results.getString("description"));
                appointment.setLocation(results.getString("location"));
                appointment.setContact(results.getString("contact"));
                appointment.setType(results.getString("type"));

                LocalDateTime startUTC = results.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = results.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zid);

                appointment.setStart(startLocal);
                appointment.setEnd(endLocal);
                appointments.add(appointment);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appointments;
    }

    //Get a list of appointments by the month
    public static ObservableList<Appointment> getAppByMonth () {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String query = "SELECT customer.*, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId "
                + "WHERE start BETWEEN NOW() AND (SELECT LAST_DAY(NOW()))";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            ResultSet results = statement.executeQuery();

            while(results.next()) {
                Customer customer = CustomerDB.getCustomerByID(results.getInt("customerId"));
                Appointment appointment = new Appointment();

                appointment.setCustomer(customer);
                appointment.setAppointmentId(results.getInt("appointmentId"));
                appointment.setCustomerId(results.getInt("customerId"));
                appointment.setUserId(results.getInt("userId"));
                appointment.setTitle(results.getString("title"));
                appointment.setDescription(results.getString("description"));
                appointment.setLocation(results.getString("location"));
                appointment.setContact(results.getString("contact"));
                appointment.setType(results.getString("type"));

                LocalDateTime startUTC = results.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = results.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zid);

                appointment.setStart(startLocal);
                appointment.setEnd(endLocal);
                appointments.add(appointment);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appointments;
    }

    //Get an appointment by ID
    public Appointment getAppByID(int appID) {
        Appointment appointment = new Appointment();
        String query = "SELECT customer.customerId, customer.customerName, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId "
                + "WHERE appointmentId = ?";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setInt(1, appID);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(results.getInt("customerId"));
                customer.setCustomerName(results.getString("customerName"));
                appointment.setCustomer(customer);
                appointment.setCustomerId(results.getInt("customerId"));
                appointment.setUserId(results.getInt("userId"));
                appointment.setTitle(results.getString("title"));
                appointment.setDescription(results.getString("description"));
                appointment.setLocation(results.getString("location"));
                appointment.setContact(results.getString("contact"));
                appointment.setType(results.getString("type"));

                LocalDateTime startUTC = results.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = results.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zid);

                appointment.setStart(startLocal);
                appointment.setEnd(endLocal);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appointment;
    }

    //Get an appointment by user
    public static ObservableList<Appointment> getAppByUser() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String query = "SELECT user.userId, customer.customerId, appointment.start FROM user "
                + "JOIN appointment ON user.userId = appointment.userId "
                + "JOIN customer ON appointment.customerId = customer.customerId";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            ResultSet results = statement.executeQuery();

            while(results.next()) {
                Appointment appointment = new Appointment();
                appointment.setUserId(results.getInt("userId"));
                appointment.setCustomerId(results.getInt("customerId"));

                LocalDateTime startUTC = results.getTimestamp("start").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);

                appointment.setStart(startLocal);
                appointments.add(appointment);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appointments;
    }

    //Get appointments upcoming in the next 15 minutes for alert on login
    public static Appointment getAppAlert() {
        Appointment appointment = new Appointment();
        String query = "SELECT customer.customerName, appointment.* FROM appointment "
                + "JOIN customer ON appointment.customerId = customer.customerId "
                + "WHERE (start BETWEEN ? AND ADDTIME(NOW(), '00:15:00'))";

        try {
            PreparedStatement statement = CONN.prepareStatement(query);
            ZonedDateTime lzt = ZonedDateTime.now(zid);
            ZonedDateTime zdtUTC = lzt.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDateTime localUTC = zdtUTC.toLocalDateTime();
            statement.setTimestamp(1, Timestamp.valueOf(localUTC));
            ResultSet results = statement.executeQuery();

            while(results.next()) {
                Customer customer = new Customer();
                customer.setCustomerName(results.getString("customerName"));
                appointment.setCustomer(customer);
                appointment.setAppointmentId(results.getInt("appointmentId"));
                appointment.setCustomerId(results.getInt("customerId"));
                appointment.setUserId(results.getInt("userId"));
                appointment.setTitle(results.getString("title"));

                LocalDateTime startUTC = results.getTimestamp("start").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);
                appointment.setStart(startLocal);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appointment;
    }

    //Check for overlapping appointments
    public static ObservableList<Appointment> getAppOverlap(LocalDateTime start, LocalDateTime end) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String query = String.join(" ",
                "SELECT * FROM appointment",
                "WHERE (start >= ? AND end <= ?)",
                "OR (start <= ? AND end >= ?)",
                "OR (start BETWEEN ? AND ? OR end BETWEEN ? AND ?)");

        try {
            LocalDateTime startLDT = start.atZone(zid).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endLDT = end.atZone(zid).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            PreparedStatement statement = CONN.prepareStatement(query);
            statement.setTimestamp(1, Timestamp.valueOf(startLDT));
            statement.setTimestamp(2, Timestamp.valueOf(endLDT));
            statement.setTimestamp(3, Timestamp.valueOf(startLDT));
            statement.setTimestamp(4, Timestamp.valueOf(endLDT));
            statement.setTimestamp(5, Timestamp.valueOf(startLDT));
            statement.setTimestamp(6, Timestamp.valueOf(endLDT));
            statement.setTimestamp(7, Timestamp.valueOf(startLDT));
            statement.setTimestamp(8, Timestamp.valueOf(endLDT));
            ResultSet results = statement.executeQuery();

            while(results.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(results.getInt("appointmentId"));
                appointment.setTitle(results.getString("title"));
                appointment.setDescription(results.getString("description"));
                appointment.setLocation(results.getString("location"));
                appointment.setContact(results.getString("contact"));
                appointment.setType(results.getString("type"));

                LocalDateTime startUTC = results.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = results.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zid);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zid);

                appointment.setStart(startLocal);
                appointment.setEnd(endLocal);
                appointments.add(appointment);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return appointments;
    }

    //Get the last used appointmentId and increment by 1 to allocate for a new appointment
    private static int getNextID() {
        int lastId = 0;
        String query = "SELECT MAX(appointmentId) FROM appointment";

        try {
            Statement statement = CONN.createStatement();
            ResultSet results = statement.executeQuery(query);

            if(results.next()) {
                lastId = results.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return (lastId + 1);
    }
}
