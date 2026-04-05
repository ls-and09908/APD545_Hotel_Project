package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.models.AuditAction;
import ca.senecacollege.hotel.models.AuditLog;
import ca.senecacollege.hotel.repositories.IAuditLogRepository;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityLogService implements IActivityLogService {
    private IAuditLogRepository _logRepo;
    private static final Logger logger = LoggerFactory.getLogger("AUDIT");
    private DateTimeFormatter dateFormat;

    @Inject
    public ActivityLogService(IAuditLogRepository logRepo) {
        this._logRepo = logRepo;
        dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

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
     * @param user
     * @param success
     */
    @Override
    public void loginAttempt(AdminUser user, boolean success) {
        LocalDateTime timestamp = LocalDateTime.now();
        AuditAction action = AuditAction.LOGIN;
        int entityId = user.getUserID();
        String type = user.getClass().getSimpleName();
        String message = success ? "was successful" : "unsuccessful";

        AuditLog log = new AuditLog(user, timestamp, action, type, entityId, message);
        _logRepo.saveAuditLog(log);
        String msg = buildLogMessage(log);
        logger.info(msg);
    }

    @Override
    public String buildLogMessage(AuditLog log) {
        String message = String.format("%s | %09d - ", log.getTimestamp().format(dateFormat), log.getLogNumber());

        if(log.getAction() == AuditAction.LOGIN){
            message = message.concat(String.format("(%s)%s: %s - %s",
                    log.getActorRole(),
                    log.getActorUsername(),
                    log.getActionLabel(),
                    log.getMessage())
            );
        }
        return message;
    }
}
