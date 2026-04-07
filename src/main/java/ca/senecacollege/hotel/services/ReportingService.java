package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.repositories.*;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class ReportingService implements IReportingService {
    private IAuditLogRepository auditRepo;
    private IFeedbackRepository feedbackRepo;
    private IPaymentRepository paymentRepo;
    private IReservationRepository reservationRepo;
    private IRoomRepository roomRepo;
    private IBillingRepository billingRepo;
    IBillingService billingService;

    @Inject
    public ReportingService(IAuditLogRepository auditRepo, IFeedbackRepository feedbackRepo, IPaymentRepository paymentRepo, IReservationRepository reservationRepo, IRoomRepository roomRepo, IBillingRepository billingRepo, IBillingService billingService){
        this.auditRepo = auditRepo;
        this.feedbackRepo = feedbackRepo;
        this.paymentRepo = paymentRepo;
        this.reservationRepo =reservationRepo;
        this.roomRepo = roomRepo;
        this.billingRepo = billingRepo;
        this.billingService = billingService;
    }

    public List<Feedback> getAllFeedback(){
        return feedbackRepo.getAllFeedbacks();
    }

//    public List<Payment> getAllPayment(){ Uncomment when completed
//        return paymentRepo.getAllPayments();
//    }

    public ObservableList<AuditLog> getAllAuditLogs(){
        return FXCollections.observableArrayList(auditRepo.getAllAuditLogs());
    }

    public List<Room> getRoomWithOccupancyStatus(LocalDate from, LocalDate to){
        List<Room> allRooms = roomRepo.getAllRooms();
        List<Room> availableRooms = roomRepo.getAvailableRooms(from, to);
        for(int i = 0;i < allRooms.size(); i++){
            for(int j = 0; j < availableRooms.size(); j++){
                if(allRooms.get(i).getRoomNumber() == availableRooms.get(j).getRoomNumber()){
                    allRooms.get(i).setStatus(new RoomStatus("Available"));
                }
            }
            System.out.println(allRooms.get(i));
            if(allRooms.get(i).getStatus() == null){
                allRooms.get(i).setStatus(new RoomStatus("Occupied"));
            }
        }
        return allRooms;
    }

    public ObservableList<Reservation> getAllReservationsBetweenDates(LocalDate from, LocalDate to){
        List<Reservation> allResos = reservationRepo.getAllReservations();
        ObservableList<Reservation> rc = FXCollections.observableArrayList();
        for(Reservation r: allResos){
            if(r.getCheckIn().isAfter(from) && r.getCheckOut().isBefore(to)){
                rc.add(r);
            }
        }
        return rc;
    }

    public ObservableList<Billing> getAllBillings(){
        return FXCollections.observableArrayList(billingRepo.getAllBillings());
    }

    public ObservableList<Payment> getAllPayments(){
        return FXCollections.observableArrayList(paymentRepo.getAllPayments());
    }

//    public Billing generateBillingWrapper(Reservation r){
//        return billingService.generateBill(r);
//    }

}
