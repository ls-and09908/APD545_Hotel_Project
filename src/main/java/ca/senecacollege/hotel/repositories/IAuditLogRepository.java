package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AuditLog;

import java.util.List;
import java.util.Optional;

public interface IAuditLogRepository {
    List<AuditLog> getAllAuditLogs();
    void saveAuditLog(AuditLog a);
    Optional<AuditLog> getAuditLog(int id);
}
