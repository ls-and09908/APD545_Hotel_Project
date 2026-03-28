package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.models.Reservation;

import java.util.List;

public interface IGuestRepository {
    List<Guest> getAllGuests();
    void saveGuest(Guest g);
    Guest getGuest(int guestID);
    List<Guest> getLoyalGuests();
    Guest getLoyaltyMember(int loyaltyNum);
    int getNewLoyaltyNumber(); // gets a new loyalty number as +1 from the previous loyalty member number
}
