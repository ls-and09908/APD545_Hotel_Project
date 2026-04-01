package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.Feedback;
import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Sentiment;
import ca.senecacollege.hotel.repositories.IReservationRepository;
import ca.senecacollege.hotel.services.IFeedbackService;
import ca.senecacollege.hotel.services.IReportingService;
import com.google.inject.Inject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.util.List;

public class ReportController {
    @FXML
    Label subtitleLbl;
    @FXML
    private TableView reportTable;

    private IFeedbackService _feedbackService;
    private IReportingService _reportingService;
    @Inject
    public ReportController(IFeedbackService feedbackService, IReportingService reportingService){
        _feedbackService = feedbackService;
        _reportingService = reportingService;
    }

    public void initialize(){
        clearTable();;
    }

    private void clearTable(){
        reportTable.getColumns().clear();
    }

    private void loadFeedbackReport(){
        clearTable();
        List<Feedback> allFeedback = _reportingService.getAllFeedback();
        TableColumn<Feedback, String> guests = new TableColumn("Guest");
        guests.setMinWidth(250);
        TableColumn<Feedback, String> reservations = new TableColumn("Reservations");
        reservations.setMinWidth(250);
        TableColumn<Feedback, Integer> rating = new TableColumn("Rating");
        rating.setMinWidth(100);
        TableColumn<Feedback, LocalDate> date = new TableColumn("Date");
        date.setMinWidth(100);
        TableColumn<Feedback, String> comments = new TableColumn("Comments");
        comments.setMinWidth(250);
        TableColumn<Feedback, Sentiment> sentiment = new TableColumn("Sentiment");
        sentiment.setMinWidth(250);
        reportTable.getColumns().addAll(guests, reservations, rating, date, comments, sentiment);

        guests.setCellValueFactory(g -> new SimpleObjectProperty<>(g.toString()));
        reservations.setCellValueFactory(g -> new SimpleObjectProperty<>(g.getValue().getReservationAsString()));
        rating.setCellValueFactory(g -> new SimpleObjectProperty<>(g.getValue().getRating()));
        date.setCellValueFactory(g -> new SimpleObjectProperty<>(g.getValue().getDate()));
        comments.setCellValueFactory(g -> new SimpleObjectProperty<>(g.getValue().getComments()));
        sentiment.setCellValueFactory(g -> new SimpleObjectProperty<>(g.getValue().getSentiment()));
    }

    private void loadOccupancy(){

    }

    @FXML
    public void revenuePress(){
        subtitleLbl.setText("Revenue");
    }
    @FXML
    public void occupancyPress(){
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
}
