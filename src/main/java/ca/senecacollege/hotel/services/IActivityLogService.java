package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.models.AuditAction;
import ca.senecacollege.hotel.models.AuditLog;
import ca.senecacollege.hotel.models.Reservation;

import java.time.LocalDateTime;

public interface IActivityLogService {
    void createLog(String msg);
    void loginAttempt(boolean success);
    void createReservation(Reservation res);

    void editReservation(Reservation res);

    void cancelReservation(Reservation res, boolean success);

    void checkoutReservation(Reservation res, boolean success);

    void checkinReservation(Reservation res);

    String reservationMsg(Reservation res);

    String buildLogMessage(AuditLog log);

    void writeLog(AuditLog log);
}
