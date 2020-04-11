package model;

import dao.AppointmentDB;
import exception.AppointmentException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class Appointment {

    private IntegerProperty appointmentId = new SimpleIntegerProperty();
    private IntegerProperty customerId = new SimpleIntegerProperty();
    private IntegerProperty userId = new SimpleIntegerProperty();
    private StringProperty title = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty location = new SimpleStringProperty();
    private StringProperty contact = new SimpleStringProperty();
    private StringProperty type = new SimpleStringProperty();
    private StringProperty url = new SimpleStringProperty();
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Customer customer = new Customer();

    public Appointment() {
    }

    public final int getAppointmentId() {
        return appointmentId.get();
    }

    public final void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }

    public IntegerProperty appointmentIdProperty() {
        return appointmentId;
    }

    public final int getCustomerId() {
        return customerId.get();
    }

    public final void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public final int getUserId() {
        return userId.get();
    }

    public final void setUserId(int userId) {
        this.userId.set(userId);
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty getAppTitleProperty() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty getAppDescProperty() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty getAppLocationProperty() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getContact() {
        return contact.get();
    }

    public StringProperty getAppContactProperty() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty getAppTypeProperty() {
        return this.type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    //Validate appointment data
    public boolean isValidApp() throws AppointmentException {
        if(this.customerId == null) {
            throw new AppointmentException("There was no customer selected.");
        }
        if(this.title.get().equals("")) {
            throw new AppointmentException("There was no title for the appointment");
        }
        if(this.description.get().equals("")) {
            throw new AppointmentException("There was no description for the appointment.");
        }
        if(this.location.get().equals("")) {
            throw new AppointmentException("There was no location for the appointment.");
        }
        if(this.contact.get().equals("")) {
            throw new AppointmentException("There was no contact for the appointment.");
        }
        isValidTime();
        return true;
    }

    //Validate appointment date & time to prevent scheduling outside of business hours
    public boolean isValidTime() throws AppointmentException {
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate appStartDate = this.start.toLocalDate();
        LocalTime appStartTime = this.start.toLocalTime();
        LocalDate appEndDate = this.end.toLocalDate();
        LocalTime appEndTime = this.end.toLocalTime();
        int weekDay = appStartDate.getDayOfWeek().getValue();

        if(!appStartDate.isEqual(appEndDate)) {
            throw new AppointmentException("The appointment must start and end on the same day.");
        }
        if(weekDay == 6 || weekDay == 7) {
            throw new AppointmentException("The appointment cannot be scheduled on weekends.");
        }
        if(appStartTime.isBefore(midnight.plusHours(8))) {
            throw new AppointmentException("The appointment cannot be scheduled before business hours.");
        }
        if(appEndTime.isAfter(midnight.plusHours(17))) {
            throw new AppointmentException("The appointment cannot be scheduled after business hours.");
        }
        if(appStartDate.isBefore(LocalDate.now()) || appStartTime.isBefore(LocalTime.MIDNIGHT)) {
            throw new AppointmentException("The appointment cannot be scheduled in the past.");
        }
        if(appEndTime.isBefore(appStartTime)) {
            throw new AppointmentException("The appointment cannot end before it starts.");
        }
        return true;
    }

    //Check if appointment overlaps with others
    public boolean isOverlapping() throws AppointmentException {
        ObservableList<Appointment> appointments = AppointmentDB.getAppOverlap(this.start.toLocalDateTime(),
                this.end.toLocalDateTime());

        if(appointments.size() > 0) {
            throw new AppointmentException("The appointment cannot be scheduled at the same time as another appointment.");
        }
        return true;
    }
}