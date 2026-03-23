package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.repositories.GuestRepository;

public interface ILoyaltyService {
    boolean findLoyalGuest(Guest checkingGuest);
}
