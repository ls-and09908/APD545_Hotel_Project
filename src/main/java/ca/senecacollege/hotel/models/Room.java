package ca.senecacollege.hotel.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Room implements ChargeSource {
    @Id
    @Column(name = "ROOM_NUM")
    private int roomNumber;

    private transient RoomStatus roomStatus;
    private transient RoomType roomType;

    @Override
    public Double getBasePrice() {
        return this.roomType.getPrice();
    }

    @Override
    public Boolean isPaidNightly() {
        return true;
    }

    public Room(int roomNumber, RoomType type){
        this.roomNumber = roomNumber;
        this.roomType = type;
    }
}
