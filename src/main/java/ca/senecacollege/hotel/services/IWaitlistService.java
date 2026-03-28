package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.RoomSet;
import ca.senecacollege.hotel.models.Waitlist;

import java.util.List;

public interface IWaitlistService {
    void saveWaitlistRes();
    Waitlist getWaitlist(int waitlistNum);

}
