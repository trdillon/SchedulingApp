package model;

import exception.AppointmentException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class Appointment {

    private IntegerProperty appointmentId = new SimpleIntegerProperty();
    private IntegerProperty customerId = new SimpleIntegerProperty();
    private IntegerProperty userId = new SimpleIntegerProperty();
    private StringProperty appTitle = new SimpleStringProperty();
    private StringProperty appDesc = new SimpleStringProperty();
    private StringProperty appStart = new SimpleStringProperty();
    private StringProperty appEnd = new SimpleStringProperty();
    private StringProperty appLocation = new SimpleStringProperty();
    private StringProperty appContact = new SimpleStringProperty();
    private StringProperty appType = new SimpleStringProperty();
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Customer customer = new Customer();

    //Constructors
    public Appointment() {}

    public Appointment(int id, int customerId, int userId, String title, String desc, String start, String end,
                       String location, String contact) {
        setAppointmentId(id);
        setCustomerId(customerId);
        setUserId(userId);
        setAppTitle(title);
        setAppDesc(desc);
        setAppStart(start);
        setAppEnd(end);
        setAppLocation(location);
        setAppContact(contact);
    }

    //Getters and Setters
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

    public String getAppTitle() {
        return appTitle.get();
    }

    public StringProperty getAppTitleProperty() {
        return this.appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle.set(appTitle);
    }

    public String getAppDesc() {
        return appDesc.get();
    }

    public StringProperty getAppDescProperty() {
        return this.appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc.set(appDesc);
    }

    public String getAppStart() {
        return appStart.get();
    }

    public void setAppStart(String appStart) {
        this.appStart.set(appStart);
    }

    public String getAppEnd() {
        return appEnd.get();
    }

    public void setAppEnd(String appEnd) {
        this.appEnd.set(appEnd);
    }

    public String getAppLocation() {
        return appLocation.get();
    }

    public StringProperty getAppLocationProperty() {
        return this.appLocation;
    }

    public void setAppLocation(String appLocation) {
        this.appLocation.set(appLocation);
    }

    public String getAppContact() {
        return appContact.get();
    }

    public StringProperty getAppContactProperty() {
        return this.appContact;
    }

    public void setAppContact(String appContact) {
        this.appContact.set(appContact);
    }

    public String getAppType() {
        return appType.get();
    }

    public StringProperty getAppTypeProperty() {
        return this.appType;
    }

    public void setAppType(String appType) {
        this.appType.set(appType);
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

    //TODO - do I need these methods any more?
    //Convert UTC to LDT
    public StringProperty getAppStartProperty() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime ldt = LocalDateTime.parse(this.appStart.getValue(), df);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcZtd = zdt.withZoneSameInstant(zid);
        return new SimpleStringProperty(utcZtd.toLocalDateTime().toString());
    }

    public StringProperty getAppEndProperty() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime ldt = LocalDateTime.parse(this.appEnd.getValue(), df);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcZtd = zdt.withZoneSameInstant(zid);
        return new SimpleStringProperty(utcZtd.toLocalDateTime().toString());
    }

    //Validate appointment data
    public boolean isValidApp() throws AppointmentException {
        if(this.customerId == null) {
            throw new AppointmentException("There was no customer selected.");
        }
        if(this.appTitle.get().equals("")) {
            throw new AppointmentException("There was no title for the appointment");
        }
        if(this.appDesc.get().equals("")) {
            throw new AppointmentException("There was no description for the appointment.");
        }
        if(this.appLocation.get().equals("")) {
            throw new AppointmentException("There was no location for the appointment.");
        }
        if(this.appContact.get().equals("")) {
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
        return true;
    }

    //Check if appointment overlaps with others
    public boolean isOverlapping() throws AppointmentException {
        ObservableList<Appointment> overlapApp = AppointmentDB.getOverlappingApps(this.start.toLocalDateTime(),
                this.end.toLocalDateTime());

        if(overlapApp.size() > 1) {
            throw new AppointmentException("The appointment cannot be scheduled at the same time as another appointment.");
        }
        return true;
    }
}
