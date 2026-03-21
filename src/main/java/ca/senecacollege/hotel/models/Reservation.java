package ca.senecacollege.hotel.models;


import java.time.LocalDate;
import java.util.List;

public class Reservation {
    private int reservationNumber;
    private Guest guest;
    private int adults;
    private int children;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status;
    private List<AddOn> addOns;
    private List<Room> rooms;
    private Billing billing;

}
