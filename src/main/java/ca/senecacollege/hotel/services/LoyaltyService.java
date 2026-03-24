package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.repositories.GuestRepository;

import java.util.List;

public class LoyaltyService implements ILoyaltyService {
    private GuestRepository allGuests;
    private List<Guest> loyalGuests;


    @Override
    public boolean findLoyalGuest(Guest checkingGuest){
        loyalGuests = allGuests.getLoyalGuests();
        return loyalGuests.contains(checkingGuest);
    }

}
