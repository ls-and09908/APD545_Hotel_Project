package ca.senecacollege.hotel.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RoomStatus {
    private StringProperty status;

    //Constructs the RoomStatus with a passed status
    public RoomStatus(String status){
        this.status = new SimpleStringProperty(status);
    }

    //Returns the status as a String
    public String getStatus(){
        return status.get();
    }
}
