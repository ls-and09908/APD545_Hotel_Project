package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.repositories.IReservationRepository;
import ca.senecacollege.hotel.services.IFeedbackService;
import ca.senecacollege.hotel.services.IReportingService;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReportController implements SceneManagerAware {
    @FXML
    Label subtitleLbl;
    @FXML
    private TableView reportTable;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private ComboBox<String> roomTypeComboBox;
    @FXML
    private Button resetBtn;

    private IFeedbackService _feedbackService;
    private IReportingService _reportingService;
    private SceneManager scenemanager;
    private String revenueReportPeriod = "Day";

    @Inject
    public ReportController(IFeedbackService feedbackService, IReportingService reportingService){
        _feedbackService = feedbackService;
        _reportingService = reportingService;
    }

    @Override
    public void setSceneManager(SceneManager scenemanager){
        this.scenemanager = scenemanager;
    }

    public void initialize(){
        clearTable();
        ObservableList<String> comboBoxOptions = FXCollections.observableArrayList();
        comboBoxOptions.add("Single");
        comboBoxOptions.add("Double");
        comboBoxOptions.add("Penthouse");
        roomTypeComboBox.setItems(comboBoxOptions);
    }

    private void clearTable(){
        reportTable.getItems().clear();
        reportTable.getColumns().clear();
    }

    private void loadFeedbackReport(){
        clearTable();
        ObservableList<Feedback> allFeedback = FXCollections.observableArrayList(_reportingService.getAllFeedback());
        TableColumn<Feedback, String> guests = new TableColumn("Guest");
        guests.setMinWidth(250);
        TableColumn<Feedback, String> reservations = new TableColumn<>("Reservations");
        reservations.setMinWidth(250);
        TableColumn<Feedback, Integer> rating = new TableColumn<>("Rating");
        rating.setMinWidth(100);
        TableColumn<Feedback, LocalDate> date = new TableColumn<>("Date");
        date.setMinWidth(100);
        TableColumn<Feedback, String> comments = new TableColumn<>("Comments");
        comments.setMinWidth(250);
        TableColumn<Feedback, Sentiment> sentiment = new TableColumn<>("Sentiment");
        sentiment.setMinWidth(250);
        reportTable.setItems(allFeedback);
        reportTable.getColumns().addAll(guests, reservations, rating, date, comments, sentiment);

        reservations.setCellValueFactory(f -> new SimpleObjectProperty<>(f.getValue().getReservationAsString()));
        guests.setCellValueFactory(f -> new SimpleObjectProperty<>(f.getValue().getGuestAsString()));
        rating.setCellValueFactory(f -> new SimpleObjectProperty<>(f.getValue().getRating()));
        comments.setCellValueFactory(f -> new SimpleObjectProperty<>(f.getValue().getComments()));
        date.setCellValueFactory(f -> new SimpleObjectProperty<>(f.getValue().getDate()));
        sentiment.setCellValueFactory(f -> new SimpleObjectProperty<>(f.getValue().getSentiment()));
    }

    private void loadOccupancy(){
        clearTable();
        LocalDate earlierRange = fromDatePicker.getValue() == null ? LocalDate.now(): fromDatePicker.getValue();
        LocalDate laterRange = toDatePicker.getValue() == null ? earlierRange.plusYears(1) : toDatePicker.getValue();
        ObservableList<Room> allRooms = FXCollections.observableArrayList(_reportingService.getRoomWithOccupancyStatus(earlierRange, laterRange));
        int occupiedRooms = 0;
        for(Room r: allRooms){
            if(r.getStatus().equals("Occupied")) occupiedRooms+=1;
        }
        subtitleLbl.setText(subtitleLbl.getText() + " - Occupancy: " + new DecimalFormat("#").format((occupiedRooms/40.00) * 100) + "%");

        TableColumn<Room, LocalDate> date = new TableColumn<>("Date");
        date.setMinWidth(500);
        TableColumn<Room, String> status = new TableColumn<>("Status");
        status.setMinWidth(500);
        reportTable.setItems(allRooms);
        reportTable.getColumns().addAll(date, status);
        status.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getStatus()));
        //TODO display Date
    }

    @FXML
    private void loadAudits(){
        clearTable();
        ObservableList<AuditLog> allAuditLogs = FXCollections.observableArrayList(_reportingService.getAllAuditLogs());
        TableColumn<AuditLog, LocalDateTime> time = new TableColumn<>("Timestamp");
        TableColumn<AuditLog, String> actor = new TableColumn<>("Actor");
        TableColumn<AuditLog, String> action = new TableColumn<>("Action");
        TableColumn<AuditLog, Integer> entity = new TableColumn<>("Entity");
        TableColumn<AuditLog, String> type = new TableColumn<>("Entity Identifier");
        TableColumn<AuditLog, String> message = new TableColumn<>("Message");
        reportTable.setItems(allAuditLogs);
        time.setCellValueFactory(a -> new SimpleObjectProperty<>(a.getValue().getTimestamp()));
        actor.setCellValueFactory(a -> new SimpleObjectProperty<>(a.getValue().getActor().getUsername()));
        action.setCellValueFactory(a -> new SimpleObjectProperty<>(a.getValue().getAction().getLabel()));
        entity.setCellValueFactory(a -> new SimpleObjectProperty<>(a.getValue().getEntity()));
        type.setCellValueFactory(a -> new SimpleObjectProperty<>(a.getValue().getEntityType()));
        message.setCellValueFactory(a -> new SimpleObjectProperty<>(a.getValue().getMessage()));
        reportTable.getColumns().addAll(time, actor, action, entity, type, message);
    }

    private void loadRevenue(){
        clearTable();
        ObservableList<Billing> allBillings  = _reportingService.getAllBillings();
        ObservableList<String> displayStrings = FXCollections.observableArrayList();
        int reservationCount = 0;
        double allSubtotal = 0;
        double allTax = 0;
        double allDiscounts = 0;
        double allTotal = 0;

        for(Billing b: allBillings){
            LocalDate checkIn = b.getReservation().getCheckIn();
            if(checkIn.isEqual(LocalDate.now()) && revenueReportPeriod.equals("Day")){
                reservationCount+=1;
                allTotal += b.getTotalPayments();
                allSubtotal += b.getTotalCharges();
            }
            else if((checkIn.isAfter(LocalDate.now().minusMonths(1)) &&
                    checkIn.isBefore(LocalDate.now().plusDays(1)))
                    && revenueReportPeriod.equals("Month")){
                reservationCount+=1;
                allTotal += b.getTotalPayments();
                allSubtotal += b.getTotalCharges();
            }
            else if((checkIn.isAfter(LocalDate.now().minusYears(1)) &&
                    checkIn.isBefore(LocalDate.now().plusDays(1)))
                    && revenueReportPeriod.equals("Year")){
                reservationCount+=1;
                allTotal += b.getTotalPayments();
                allSubtotal += b.getTotalCharges();
            }

        }

        String totalString = "Total: $" + String.valueOf(allTotal);
        String subtotalString = "Subtotal: $" + String.valueOf(allSubtotal);
        String taxString = "Total Taxes: $" + String.valueOf(allTax);
        String discountString = "Total Discounts: " + String.valueOf(allDiscounts) + "%";
        displayStrings.addAll("Reservations: " + String.valueOf(reservationCount), totalString, subtotalString, taxString, discountString);

        TableColumn<String, String> period = new TableColumn<>(revenueReportPeriod);

        reportTable.setItems(displayStrings);
        reportTable.getColumns().addAll(period);

        period.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));
        period.setMinWidth(200);
        //The system must show period, reservation count, subtotal, tax, discounts, and total
        //for day, week, and month views.
    }

    @FXML
    public void revenuePress(){
        loadRevenue();
        subtitleLbl.setText("Revenue");
    }
    @FXML
    public void occupancyPress(){
        loadOccupancy();
    }
    @FXML
    public void activityPress(){
        loadAudits();
        subtitleLbl.setText("Activity");
    }
    @FXML
    public void feedbackPress(){
        subtitleLbl.setText("Feedback");
        loadFeedbackReport();
    }
    @FXML
    public void exitPress() throws IOException {
        scenemanager.switchScene("/ca/senecacollege/hotel/application/AdminDashboard.fxml",null);
    }

    @FXML
    private void dayPress(){
        revenueReportPeriod = "Day";
    }
    @FXML
    private void monthPress(){
        revenueReportPeriod = "Month";
    }
    @FXML
    private void yearPress(){
        revenueReportPeriod = "Year";
    }
    @FXML
    private void exportToTxt(){
        try {
            File file = new File("testFile.txt");
            Writer writer = new BufferedWriter(new FileWriter(file));

            //This writes header
            for(Object c: reportTable.getColumns()){
                TableColumn<?,?> col = (TableColumn<?, ?>) c;
                writer.write(col.getText() + ",");
            }
            writer.write("\n");

            for (int i = 0; i < reportTable.getItems().size(); i++) {
                for (int j = 0; j < reportTable.getColumns().size(); j++) {
                    TableColumn<?, ?> col = (TableColumn<?, ?>) reportTable.getColumns().get(j);
                    writer.write(String.valueOf(col.getCellData(i) + " "));
                }
                writer.write("\n");
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void exportToCSV(){ //TODO Fix this
        try {
            File file = new File("testFile.csv");
            Writer writer = new BufferedWriter(new FileWriter(file));

            //This writes header
            for(Object c: reportTable.getColumns()){
                TableColumn<?,?> col = (TableColumn<?, ?>) c;
                writer.write(col.getText() + ",");
            }
            writer.write("\n");

                for (int i = 0; i < reportTable.getItems().size(); i++) {
                    for (int j = 0; j < reportTable.getColumns().size(); j++) {
                        TableColumn<?, ?> col = (TableColumn<?, ?>) reportTable.getColumns().get(j);
                        if(j != reportTable.getColumns().size()-1) {
                            writer.write(String.valueOf(col.getCellData(i) + ","));
                        }
                        else{
                            writer.write(String.valueOf(col.getCellData(i)));
                        }
                    }
                    writer.write("\n");
                }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
