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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "GUEST_ID")
    private Guest guest;
    private int adults;
    private int children;

    private LocalDate checkIn;
    private LocalDate checkOut;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.BOOKING;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "RESERVATION_ADDON", joinColumns = @JoinColumn(name = "RES_NUM"), inverseJoinColumns = @JoinColumn(name="ADDON_ID"))
    private Set<AddOn> addOns = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "RESERVATION_ROOM", joinColumns = @JoinColumn(name = "RES_NUM"), inverseJoinColumns = @JoinColumn(name="ROOM_NUM"))
    private List<Room> rooms = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BILL_NUM")
    private Billing billing;

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Reservation(){
        this.guest = null;
        this.billing = null;
        this.adults = 0;
        this.children = 0;
        this.checkIn = null;
        this.checkOut = null;
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

    public Guest getGuest(){
        return guest;
    }

    public void addAddOn(AddOn a){
        this.addOns.add(a);
    }

    public void addAddOns(List<AddOn> a){
        this.addOns.addAll(a);
    }

    public void addRoom(Room r){
        this.rooms.add(r);
    }

    public void addRooms(List<Room> r){
        this.rooms.addAll(r);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Set<AddOn> getAddOns() {
        return addOns;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public int getReservationNumber(){
        return reservationNumber;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
}
