package ca.senecacollege.hotel.models;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
public class Reservation {
    @Id
    @GeneratedValue
    @Column(name = "RESERVATION_NUM")
    private int reservationNumber;

    @ManyToOne
    @JoinColumn(name = "GUEST_ID")
    private Guest guest;
    private int adults;
    private int children;

    private LocalDate checkIn;
    private LocalDate checkOut;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    // TODO set up db with join table mapping
    private transient Set<AddOn> addOns;
    private transient List<Room> rooms;

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    @OneToOne
    @JoinColumn(name = "BILL_NUM")
    private Billing billing;

    public Reservation(Guest guest, int adults, int children, LocalDate checkIn, LocalDate checkOut){
        // TEMPORARY CONSTRUCTOR FOR DB TESTING
        this.billing = new Billing(this);
        this.status = ReservationStatus.BOOKED;
        this.guest = guest;
        this.adults = adults;
        this.children = children;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }


}
