package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Guest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;

public class GuestRepository {
    //Just storing guests in memory for now. Will update
    private ObservableList<Guest> guestList = FXCollections.observableArrayList();

    public void addGuest(Guest g){
        guestList.add(g);
    }

    //This should theoretically return all the loyal guests of all the guests. Willing to hear another way of doing it with SQL
    public HashSet<Guest> getLoyalGuests(){
        HashSet<Guest> loyalGuests = new HashSet<>();
        for(Guest i : guestList){
            if(i.isLoyal()){
                loyalGuests.add(i);
            }
        }
        return loyalGuests;
    }

}
