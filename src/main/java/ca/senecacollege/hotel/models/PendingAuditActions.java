package ca.senecacollege.hotel.models;

import java.util.ArrayList;
import java.util.List;

public class PendingAuditActions {
    List<AuditLog> logs = new ArrayList<>();

    public List<AuditLog> getLogs() {
        return logs;
    }

    public void addLog(AuditLog log){
        logs.add(log);
    }

    public void emptyLogs(){
        logs.clear();
    }
}
