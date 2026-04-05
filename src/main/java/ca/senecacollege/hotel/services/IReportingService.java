package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public interface IReportingService {

    public List<Feedback> getAllFeedback();

//    public List<Payment> getAllPayment(){;

//    public List<Reservation> getAllReservation();

//    public List<AuditLog> getAllAuditLogs();

    public List<Room> getRoomWithOccupancyStatus(LocalDate from, LocalDate to);
    public ObservableList<Reservation> getAllReservationsBetweenDates(LocalDate from, LocalDate to);
    public ObservableList<Payment> getAllPayments();
    public ObservableList<Billing> getAllBillings();
//    public Billing generateBillingWrapper(Reservation r);
}
