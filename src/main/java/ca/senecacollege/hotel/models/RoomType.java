package ca.senecacollege.hotel.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RoomType {
    private StringProperty type;
    int maxOccupancy;
    double price;

    //Constructs RoomType using a String containing the type.
    public RoomType(String type){
        this.type = new SimpleStringProperty(type);
        if(type.equals("Penthouse")){
            maxOccupancy = 2;
            price = 150;
        }
        else if(type.equals("Single")){
            maxOccupancy = 2;
            price = 100;
        }
        else{
            maxOccupancy = 4;
            price = 100;
        }
    }

    //Returns type as a String.
    public String getType(){
        return type.get();
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
