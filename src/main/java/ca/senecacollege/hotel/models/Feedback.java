package ca.senecacollege.hotel.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
//    @Cascade({CascadeType.MERGE})
    private Reservation reservation;

    @Column
    private int rating = 0;
    private LocalDate date;

    @Column(columnDefinition = "TEXT", length = 2000)
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

    public Integer getRating(){
        return rating;
    }

    public String getReservationAsString(){
        return reservation.toString();
    }

    public Guest getGuest(){
        return guest;
    }

    public String getGuestAsString(){
        return guest.toString();
    }

    public String getComments() {
        return comments;
    }

    public LocalDate getDate() {
        return date;
    }
    public Sentiment getSentiment(){
        return sentiment;
    }

    public Reservation getReservation() { return reservation; }
}
