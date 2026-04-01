package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Feedback;
import ca.senecacollege.hotel.models.Reservation;

import java.util.List;

public interface IReportingService {

    public List<Feedback> getAllFeedback();

//    public List<Payment> getAllPayment(){;

    public List<Reservation> getAllReservation();

//    public List<AuditLog> getAllAuditLogs();
}
