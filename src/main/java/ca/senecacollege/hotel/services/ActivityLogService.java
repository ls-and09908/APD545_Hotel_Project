package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.repositories.IAuditLogRepository;
import ca.senecacollege.hotel.utilities.UserContext;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityLogService implements IActivityLogService {
    private IAuditLogRepository _logRepo;
    private static final Logger logger = LoggerFactory.getLogger("AUDIT");
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter resDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private boolean pendingLog = false;
    private PendingAuditActions pendingActions = new PendingAuditActions();

    @Inject
    public ActivityLogService(IAuditLogRepository logRepo) {
        this._logRepo = logRepo;
    }

    private LocalDateTime timestamp(){
        return LocalDateTime.now();
    }

    @Override
    public void setPending(boolean isPending){ pendingLog = isPending; }

    /**
     * Testing function for logging, not to be used in production.
     * @param msg
     */
    @Override
    public void createLog(String msg) {
        logger.info("Action: {}", msg);
    }

    /**
     * Stores the action taken as an audit log, both locally and in the database.
     * @param success
     */
    @Override
    public void loginAttempt(boolean success) {
        int entityId = UserContext.getUser().getUserID();
        String type = UserContext.getUser().getClass().getSimpleName();
        String message = success ? "was successful" : "unsuccessful";

        AuditLog log = new AuditLog(UserContext.getUser(), timestamp(), AuditAction.LOGIN, type, entityId, message);
        writeLog(log);
    }

    @Override
    public void createReservation(Reservation res){
        int entityId = res.getReservationNumber();
        String message = reservationMsg(res);
        AuditLog log = new AuditLog(UserContext.getUser(), timestamp(), AuditAction.CREATE_RES, res.getClass().getSimpleName(), entityId, message);
        writeLog(log);
    }

    @Override
    public void editReservation(Reservation res){
        int entityId = res.getReservationNumber();
        String message = reservationMsg(res);
        AuditLog log = new AuditLog(UserContext.getUser(), timestamp(), AuditAction.EDIT_RES, res.getClass().getSimpleName(), entityId, message);
        writeLog(log);
    }

    @Override
    public void cancelReservation(Reservation res, boolean success){
        int entityId = res.getReservationNumber();
        String message = success ? "SUCCESS" : "FAILURE";
        message += reservationMsg(res);
        AuditLog log = new AuditLog(UserContext.getUser(), timestamp(), AuditAction.CANCELLATION, res.getClass().getSimpleName(), entityId, message);
        writeLog(log);
    }

    @Override
    public void checkoutReservation(Reservation res, boolean success){
        int entityId = res.getReservationNumber();
        String message = success ? "SUCCESS" : "FAILURE";
        message += reservationMsg(res);
        AuditLog log = new AuditLog(UserContext.getUser(), timestamp(), AuditAction.CHECKOUT, res.getClass().getSimpleName(), entityId, message);
        writeLog(log);
    }

    @Override
    public void checkinReservation(Reservation res){
        int entityId = res.getReservationNumber();
        String message = reservationMsg(res);
        AuditLog log = new AuditLog(UserContext.getUser(), timestamp(), AuditAction.CHECKIN, res.getClass().getSimpleName(), entityId, message);
        writeLog(log);
    }

    @Override
    public void processRefund(Billing bill, double amount, PaymentMethod type, boolean success){
        int entityId = bill.getBillNumber();
        String message = success ? "SUCCESS" : "FAILURE";
        message += String.format(" Refunded $%.2f", amount);
        AuditLog log = new AuditLog(UserContext.getUser(), timestamp(), AuditAction.REFUND, bill.getClass().getSimpleName(), entityId, message);
        writeLog(log);
    }

    @Override
    public void processDiscount(Billing bill, boolean success, double percent){
        int entityId = bill.getBillNumber();
        String message = success ? "SUCCESS" : "FAILURE";
        message += String.format(" Apply discount of %.2f", percent) + "%";
        AuditLog log = new AuditLog(UserContext.getUser(), timestamp(), AuditAction.DISCOUNT, bill.getClass().getSimpleName(), entityId, message);
        writeLog(log);
    }

    @Override
    public void processPayment(Billing bill, double amount, PaymentMethod type, boolean success){
        int entityId = bill.getBillNumber();
        String message = success ? "SUCCESS" : "FAILURE";
        message += String.format(" Paid $%.2f using %s", amount,type.asLabel());
        AuditLog log = new AuditLog(UserContext.getUser(), timestamp(), AuditAction.PAYMENT, bill.getClass().getSimpleName(), entityId, message);
        writeLog(log);
    }

    @Override
    public void receiveFeedback(Feedback fb) {
        int entityId = fb.getFeedbackId();
        String message = " on Reservation " + fb.getReservation().getReservationNumber() + " " + fb.getRating() + " stars | " + fb.getSentiment().name();
        AuditLog log = new AuditLog(null, timestamp(), AuditAction.FEEDBACK, fb.getClass().getSimpleName(), entityId, message);
        writeLog(log);
    }

    @Override
    public void search(Object entity, String nameCriteria, String phoneCriteria, String emailCriteria, String statusCriteria, LocalDate fromDate, LocalDate toDate) {
        String message = "Criteria;";
        if(!nameCriteria.isEmpty()){
            message += "[name: " + nameCriteria + "],";
        }
        if(!phoneCriteria.isEmpty()){
            message += "[phone: " + phoneCriteria + "],";
        }
        if(!emailCriteria.isEmpty()){
            message += "[email: " + emailCriteria + "],";
        }
        if(!statusCriteria.isEmpty()){
            message += "[status: " + statusCriteria + "],";
        }
        message += "[dates: " + fromDate.format(resDateFormat) + " - " + toDate.format(resDateFormat) + "]";
        AuditLog log = new AuditLog(null, timestamp(), AuditAction.SEARCH, entity.getClass().getSimpleName(), 0, message);
        writeLog(log);
    }

    @Override
    public String reservationMsg(Reservation res){
        return " Rooms: " + res.getRooms() + " | FROM: " + res.getCheckIn().format(resDateFormat) + " - " + res.getCheckOut().format(resDateFormat);
    }

    @Override
    public String buildLogMessage(AuditLog log) {
        String message = String.format("%s | %09d - ", log.getTimestamp().format(dateFormat), log.getLogNumber());

        switch(log.getAction()){
            case LOGIN:
                message = message.concat(String.format("(%s)%s: %s - %s",
                        log.getActorRole(),
                        log.getActorUsername(),
                        log.getActionLabel(),
                        log.getMessage())
                );
                break;
            case REFUND, PAYMENT, DISCOUNT, CHECKIN, CHECKOUT, CANCELLATION, CREATE_RES, EDIT_RES:
                message = message.concat(String.format("(%s)%s: %s %d - %s",
                        log.getActorRole(),
                        log.getActorUsername(),
                        log.getActionLabel(),
                        log.getEntity(),
                        log.getMessage())
                );
            case FEEDBACK:
                message = message.concat(String.format("(USER)Guest: %s %d - %s",
                        log.getActionLabel(),
                        log.getEntity(),
                        log.getMessage())
                );
                break;
            case SEARCH:
                message = message.concat(String.format("(%s)%s: %s %d - %s",
                        log.getActorRole(),
                        log.getActorUsername(),
                        log.getActionLabel(),
                        log.getEntityType(),
                        log.getMessage())
                );
                break;
        }
        return message;
    }

    @Override
    public void writeLog(AuditLog log){
        if(pendingLog){ this.pendingActions.addLog(log);}
        else {
            _logRepo.saveAuditLog(log);
            String msg = buildLogMessage(log);
            logger.info(msg);
        }
    }

    @Override
    public void writePending(){
        setPending(false);
        for (AuditLog log : this.pendingActions.getLogs()){
            writeLog(log);
        }
        this.pendingActions.emptyLogs();
    }

    @Override
    public void clearPendingLogs(){
        this.pendingActions.emptyLogs();
    }
}
