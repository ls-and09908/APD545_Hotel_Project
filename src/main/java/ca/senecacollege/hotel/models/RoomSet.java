package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

@Entity(name = "WAITLIST_ROOM")
public class RoomSet {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "ROOM_TYPE")
    private RoomType type;
    private int quantity;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "WAITLIST_ID", nullable = false)
    private Waitlist waitlist = null;

    public RoomSet(){}

    public RoomSet(RoomType type, int quantity){
        this.type = type;
        this.quantity = quantity;
    }

    public RoomSet(RoomType type, int quantity, Waitlist waitlist){
        this.type = type;
        this.quantity = quantity;
        this.waitlist = waitlist;
    }

    public RoomType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public Waitlist getWaitlist() {
        return waitlist;
    }

    public void setWaitlist(Waitlist w){
        this.waitlist = w;
    }
}
