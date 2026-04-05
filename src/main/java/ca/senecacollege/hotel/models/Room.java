package ca.senecacollege.hotel.models;

import jakarta.persistence.*;
import javafx.beans.property.StringProperty;

@Entity
public class Room implements ChargeSource {
    @Id
    @Column(name = "ROOM_NUM")
    private int roomNumber;

    private transient RoomStatus roomStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROOM_TYPE")
    private RoomType roomType;

    int maxOccupancy;
    double price;

    @Override
    public Double getBasePrice() {
        return this.getPrice();
    }

    @Override
    public Boolean isPaidNightly() {
        return true;
    }

    public Room(int roomNumber, RoomType type){
        this.roomNumber = roomNumber;
        this.roomType = type;

        switch(type) {
            case PENTHOUSE:
                maxOccupancy = 2;
                price = 350;
                break;
            case SINGLE:
                maxOccupancy = 2;
                price = 100;
                break;
            case DOUBLE:
                maxOccupancy = 4;
                price = 200;
                break;
            case DELUXE:
                maxOccupancy = 2;
                price = 250;
                break;
            default:
                maxOccupancy = 0;
                price = 0;
        }
    }

    public Room(){}

    //Returns type as a String.
    public RoomType getType(){
        return roomType;
    }

    //Returns price as a double
    public double getPrice(){
        return price;
    }

    //returns maxOccupancy as an int
    public int getMaxOccupancy(){
        return maxOccupancy;
    }

    public void setStatus(RoomStatus status){
        roomStatus = status;
    }

    public String getStatus(){
        if(roomStatus != null) {
            return roomStatus.getStatus();
        }

        return null;

    }

    public int getRoomNumber(){
        return roomNumber;
    }
}
