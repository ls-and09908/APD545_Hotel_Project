package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Waitlist;

import java.util.List;
import java.util.Optional;

public interface IWaitlistRepository {
    List<Waitlist> getAllWaitlists();
    void saveWaitlist(Waitlist w);
    Optional<Waitlist> getWaitlist(int waitlistNum);
    public void removeWaitlist(Waitlist w);
}
