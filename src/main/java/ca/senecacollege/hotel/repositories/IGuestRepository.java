package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Guest;

import java.util.List;
import java.util.Optional;

public interface IGuestRepository {
    List<Guest> getAllGuests();
    Optional<Guest> getGuest(int guestID);
    List<Guest> getLoyalGuests();
    Optional<Guest> getLoyaltyMember(int loyaltyNum);
    Optional<Integer> getNewLoyaltyNumber(); // gets a new loyalty number as +1 from the previous loyalty member number
    Optional<Guest> findGuestEmail(String email);
}
