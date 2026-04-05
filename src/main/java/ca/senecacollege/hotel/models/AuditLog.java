package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AuditLog {
    @Id
    @GeneratedValue
    @Column(name = "LOG_NUMBER")
    private int logNumber;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private AdminUser actor;

    private LocalDateTime timestamp;

    private AuditAction action;
    private String entityType;
    private int entity;

    @Column(columnDefinition = "TEXT", length = 300)
    private String message;

    public AuditLog(){}

    public AuditLog(AdminUser actor, LocalDateTime timestamp, AuditAction action, String entityType, int entity, String message) {
        this.actor = actor;
        this.timestamp = timestamp;
        this.action = action;
        this.entityType = entityType;
        this.entity = entity;
        this.message = message;
    }

    public int getLogNumber() {
        return logNumber;
    }

    public AdminUser getActor() {
        return actor;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public AuditAction getAction() {
        return action;
    }

    public String getActionLabel(){
        return action.label();
    }

    public String getEntityType() {
        return entityType;
    }

    public int getEntity() {
        return entity;
    }

    public String getMessage() {
        return message;
    }

    public String getActorUsername(){
        return actor.getUsername();
    }

    /**
     *
     * @return the actor's role in shortened format for use in logging
     */
    public String getActorRole(){
        return actor.getRoleLabel();
    }
}
