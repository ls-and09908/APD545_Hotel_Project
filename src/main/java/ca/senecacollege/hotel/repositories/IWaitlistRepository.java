package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Waitlist;

import java.util.List;

public interface IWaitlistRepository {
    List<Waitlist> getAllWaitlists();
    void saveWaitlist(Waitlist w);
    Waitlist getWaitlist(int waitlistNum);
}
