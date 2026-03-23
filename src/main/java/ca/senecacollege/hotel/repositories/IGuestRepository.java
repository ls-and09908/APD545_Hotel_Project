package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.models.Reservation;

import java.util.List;

public interface IGuestRepository {
    List<Guest> getAllGuests();
    void saveGuest(Guest r);
    Guest getGuest(int guestID);

}
