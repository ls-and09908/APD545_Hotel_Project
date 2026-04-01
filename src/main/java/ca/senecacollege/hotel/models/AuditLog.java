package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

import java.time.LocalDate;
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
    private String action;
    private String entityType;
    private String entity;
    private String message;
}
