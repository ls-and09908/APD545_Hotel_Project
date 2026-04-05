package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.models.AuditAction;
import ca.senecacollege.hotel.models.AuditLog;

import java.time.LocalDateTime;

public interface IActivityLogService {
    void createLog(String msg);
    void loginAttempt(AdminUser user, boolean success);
    String buildLogMessage(AuditLog log);
}
