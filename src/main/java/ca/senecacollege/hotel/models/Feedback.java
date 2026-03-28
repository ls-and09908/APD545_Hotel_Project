package ca.senecacollege.hotel.models;

import jakarta.persistence.*;
import org.hibernate.mapping.Constraint;

import java.time.LocalDate;

@Entity
public class Feedback {
    @Id
    @GeneratedValue
    @Column(name = "FEEDBACK_ID")
    private int feedbackId;

    @ManyToOne
    @JoinColumn(name = "GUEST_ID")
    private Guest guest;

    @OneToOne
    private Reservation reservation;

    @Column

    private int rating;
    private LocalDate date;
    private String comments;
    private Sentiment sentiment;

}
