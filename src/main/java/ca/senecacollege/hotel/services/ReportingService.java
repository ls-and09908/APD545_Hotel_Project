package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.AuditLog;
import ca.senecacollege.hotel.models.Feedback;
import ca.senecacollege.hotel.models.Payment;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.repositories.IAuditLogRepository;
import ca.senecacollege.hotel.repositories.IFeedbackRepository;
import ca.senecacollege.hotel.repositories.IPaymentRepository;
import ca.senecacollege.hotel.repositories.IReservationRepository;
import com.google.inject.Inject;

import java.util.List;

public class ReportingService implements IReportingService {
    private IAuditLogRepository auditRepo;
    private IFeedbackRepository feedbackRepo;
    private IPaymentRepository paymentRepo;
    private IReservationRepository reservationRepo;

    @Inject
    public ReportingService(IAuditLogRepository auditRepo, IFeedbackRepository feedbackRepo, IPaymentRepository paymentRepo, IReservationRepository reservationRepo){
        this.auditRepo = auditRepo;
        this.feedbackRepo = feedbackRepo;
        this.paymentRepo = paymentRepo;
        this.reservationRepo =reservationRepo;
    }

    public List<Feedback> getAllFeedback(){
        return feedbackRepo.getAllFeedbacks();
    }

//    public List<Payment> getAllPayment(){ Uncomment when completed
//        return paymentRepo.getAllPayments();
//    }

    public List<Reservation> getAllReservation(){
        return reservationRepo.getAllReservations();
    }

//    public List<AuditLog> getAllAuditLogs(){ Uncomment when completed
//        return auditRepo.getAllAuditLogs();
//    }

}
