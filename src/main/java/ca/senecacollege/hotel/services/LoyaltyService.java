package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.repositories.GuestRepository;
import ca.senecacollege.hotel.repositories.IGuestRepository;
import com.google.inject.Inject;

import java.util.List;

public class LoyaltyService implements ILoyaltyService {
    private IGuestRepository _guestRepo;

    @Inject
    LoyaltyService(IGuestRepository guestRepo){
        this._guestRepo = guestRepo;
    }

    @Override
    public boolean findLoyalGuest(Guest checkingGuest){
        List<Guest> loyalGuests = _guestRepo.getLoyalGuests();
        return loyalGuests.contains(checkingGuest);
    }

    @Override
    public Guest getLoyalGuest(int loyaltyNum) {
        Guest guest = _guestRepo.getLoyaltyMember(loyaltyNum);
        return guest;
    }

    @Override
    public int getNewLoyaltyNum() {
        return _guestRepo.getNewLoyaltyNumber();
    }
}
