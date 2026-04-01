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
    private int rating = 0;
    private LocalDate date;
    private String comments;
    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;

    public Feedback(){}

    public Feedback(Guest guest, Reservation reservation, int rating, LocalDate date, String comments, Sentiment sentiment) {
        this.guest = guest;
        this.reservation = reservation;
        this.rating = rating;
        this.date = date;
        this.comments = comments;
        this.sentiment = sentiment;
    }
}
