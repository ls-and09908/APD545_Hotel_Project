package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Waitlist {
    @Id
    @GeneratedValue
    @Column(name = "WAITLIST_NUM")
    private int waitlistNum;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "GUEST_ID")
    private Guest guest;
    private int adults;
    private int children;
    private LocalDate checkIn;
    private LocalDate checkOut;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RoomSet> rooms = new ArrayList<>();

    public Waitlist(){}

    public Waitlist(Guest guest, int adults, int children, LocalDate checkIn, LocalDate checkOut){
        this.guest = guest;
        this.adults = adults;
        this.children = children;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Guest getGuest() {
        return guest;
    }

    public int getChildren(){
        return children;
    }
    public int getAdults(){
        return adults;
    }
    public LocalDate getCheckIn(){
        return checkIn;
    }
    public LocalDate getCheckOut(){
        return checkOut;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setRooms(List<RoomSet> neededRooms){
        rooms = neededRooms;
    }
    public List<RoomSet> getRooms(){
        return rooms;
    }

    public void alert(){
        System.out.println("Rooms available");
    }
}
