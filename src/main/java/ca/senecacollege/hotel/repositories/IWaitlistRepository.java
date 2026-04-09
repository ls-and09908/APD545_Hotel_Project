package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.RoomSet;
import ca.senecacollege.hotel.models.RoomType;
import ca.senecacollege.hotel.models.Waitlist;

import java.util.List;
import java.util.Optional;

public interface IWaitlistRepository {
    List<Waitlist> getAllWaitlists();
    void saveWaitlist(Waitlist w, Integer guestID, List<RoomSet> set);
    Optional<Waitlist> getWaitlist(int waitlistNum);
    public void removeWaitlist(Waitlist w);
}
