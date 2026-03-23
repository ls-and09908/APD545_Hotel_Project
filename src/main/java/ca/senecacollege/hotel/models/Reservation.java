package ca.senecacollege.hotel.models;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Reservation {
    @Id
    @GeneratedValue
    @Column(name = "RES_NUM")
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "RESERVATION_ADDON", joinColumns = @JoinColumn(name = "RES_NUM"), inverseJoinColumns = @JoinColumn(name="ADDON_ID"))
    private Set<AddOn> addOns = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "RESERVATION_ROOM", joinColumns = @JoinColumn(name = "RES_NUM"), inverseJoinColumns = @JoinColumn(name="ROOM_NUM"))
    private List<Room> rooms = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "BILL_NUM")
    private Billing billing;

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

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

    public void addAddOn(AddOn a){
        this.addOns.add(a);
    }

    public void addRoom(Room r){
        this.rooms.add(r);
    }

    public int getReservationNumber(){
        return reservationNumber;
    }


}
