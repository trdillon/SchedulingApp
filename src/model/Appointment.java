package model;

import exception.AppointmentException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class Appointment {

    private SimpleIntegerProperty appID = new SimpleIntegerProperty();
    private SimpleIntegerProperty appCustID = new SimpleIntegerProperty();
    private SimpleIntegerProperty appUserID = new SimpleIntegerProperty();
    private SimpleStringProperty appTitle = new SimpleStringProperty();
    private SimpleStringProperty appDesc = new SimpleStringProperty();
    private SimpleStringProperty appStart = new SimpleStringProperty();
    private SimpleStringProperty appEnd = new SimpleStringProperty();
    private SimpleStringProperty appLocation = new SimpleStringProperty();
    private SimpleStringProperty appContact = new SimpleStringProperty();
    private SimpleStringProperty appType = new SimpleStringProperty();
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Customer customer = new Customer();

    //Constructors
    public Appointment() {}

    public Appointment(int id, int custID, String title, String desc, String start, String end,
                       String location, String contact) {
        setAppID(id);
        setAppCustID(custID);
        setAppTitle(title);
        setAppDesc(desc);
        setAppStart(start);
        setAppEnd(end);
        setAppLocation(location);
        setAppContact(contact);
    }

    //Getters and Setters
    public int getAppID() {
        return appID.get();
    }

    public void setAppID(int appID) {
        this.appID.set(appID);
    }

    public int getAppCustID() {
        return appCustID.get();
    }

    public void setAppCustID(int appCustID) {
        this.appCustID.set(appCustID);
    }

    public int getAppUserID() {
        return appUserID.get();
    }

    public void setAppUserID(int appUserID) {
        this.appUserID.set(appUserID);
    }

    public String getAppTitle() {
        return appTitle.get();
    }

    public SimpleStringProperty getAppTitleProperty() {
        return this.appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle.set(appTitle);
    }

    public String getAppDesc() {
        return appDesc.get();
    }

    public SimpleStringProperty getAppDescProperty() {
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

    public SimpleStringProperty getAppLocationProperty() {
        return this.appLocation;
    }

    public void setAppLocation(String appLocation) {
        this.appLocation.set(appLocation);
    }

    public String getAppContact() {
        return appContact.get();
    }

    public SimpleStringProperty getAppContactProperty() {
        return this.appContact;
    }

    public void setAppContact(String appContact) {
        this.appContact.set(appContact);
    }

    public String getAppType() {
        return appType.get();
    }

    public SimpleStringProperty getAppTypeProperty() {
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
        if(this.appCustID == null) {
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
