package ca.senecacollege.hotel.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import javafx.beans.property.StringProperty;

@Entity
public class Room implements ChargeSource {
    @Id
    @Column(name = "ROOM_NUM")
    private int roomNumber;

    private transient RoomStatus roomStatus;
    private transient RoomType roomType;
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

        if(roomType == RoomType.PENTHOUSE ){
            maxOccupancy = 2;
            price = 150;
        }
        else if(roomType == RoomType.SINGLE){
            maxOccupancy = 2;
            price = 100;
        }
        else{
            maxOccupancy = 4;
            price = 100;
        }
    }

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

}
