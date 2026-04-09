package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IActivityLogService {
    void setPending(boolean isPending);

    void createLog(String msg);
    void loginAttempt(boolean success);
    void createReservation(Reservation res);

    void editReservation(Reservation res);

    void cancelReservation(Reservation res, boolean success);

    void checkoutReservation(Reservation res, boolean success);

    void checkinReservation(Reservation res);

    void processRefund(Billing bill, double amount, PaymentMethod type, boolean success);

    void processDiscount(Billing bill, boolean success, double percent);

    void processPayment(Billing bill, double amount, PaymentMethod type, boolean success);

    void receiveFeedback(Feedback fb);

    void search(String nameCriteria, String phoneCriteria, String emailCriteria, String statusCriteria, LocalDate fromDate, LocalDate toDate);

    String reservationMsg(Reservation res);

    String buildLogMessage(AuditLog log);

    void writeLog(AuditLog log);

    void writePending();

    void clearPendingLogs();
}
