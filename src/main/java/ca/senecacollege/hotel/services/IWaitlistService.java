package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.RoomSet;
import ca.senecacollege.hotel.models.Waitlist;
import javafx.collections.ObservableList;

import java.util.List;

public interface IWaitlistService {
    void saveWaitlistRes(Waitlist w);
    Waitlist getWaitlist(int waitlistNum);
    public ObservableList<Waitlist> getAllWaitlist();
    public void removeWaitlist(Waitlist w);
}
