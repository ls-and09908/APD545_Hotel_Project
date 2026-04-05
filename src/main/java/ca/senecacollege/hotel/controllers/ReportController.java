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
import java.time.LocalDate;
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

        List<Reservation> reservationDates = FXCollections.observableArrayList(_reportingService.getAllReservationsBetweenDates(earlierRange,laterRange));

        TableColumn<Room, LocalDate> date = new TableColumn<>("Date");
        date.setMinWidth(500);
        TableColumn<Room, String> status = new TableColumn<>("Status");
        status.setMinWidth(500);
        reportTable.setItems(allRooms);
        reportTable.getColumns().addAll(date, status);
        status.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getStatus()));
        //TODO display Date,  Occupancy%
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
            b = _reportingService.generateBillingWrapper(b.getReservation());
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
        subtitleLbl.setText("Occupancy");
    }
    @FXML
    public void activityPress(){
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

    }
    @FXML
    private void exportToCSV(){
        try {
            File file = new File("testFile.csv");
            Writer writer = new BufferedWriter(new FileWriter(file));

            //This writes header
            for(Object c: reportTable.getColumns()){
                TableColumn<?,?> col = (TableColumn<?, ?>) c;
                writer.write(col.getText() + ",");
            }
            writer.write("\n");

            System.out.println(reportTable.getColumns().size());
            System.out.println(reportTable.getItems().size());
            if(reportTable.getColumns().size() > reportTable.getItems().size()) {
                for (int i = 0; i < reportTable.getColumns().size(); i++) {
                    TableColumn<?, ?> col = (TableColumn<?, ?>) reportTable.getColumns().get(i);
                    for (int j = 0; j < reportTable.getItems().size(); j++) {
                        String display = String.valueOf(col.getCellData(j));
                        writer.write(display + ",");
                    }
                    writer.write("\n");
                }
            }
            else {
                for (int i = 0; i < reportTable.getItems().size(); i++) {
                    for (int j = 0; j < reportTable.getColumns().size(); j++) {
                        TableColumn<?, ?> col = (TableColumn<?, ?>) reportTable.getColumns().get(j);
                        writer.write(String.valueOf(col.getCellData(i) + ","));
                    }
                    writer.write("\n");
                }
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
