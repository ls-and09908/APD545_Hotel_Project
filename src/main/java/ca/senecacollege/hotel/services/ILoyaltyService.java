package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.repositories.GuestRepository;

public interface ILoyaltyService {
    boolean findLoyalGuest(Guest checkingGuest);
    Guest getLoyalGuest(int loyaltyNum);
    int getNewLoyaltyNum();
    int getPointsFromPayment(double paymentAmt);
    void earnPoints(int pts, Guest g);
    int spendPoints(double amt, Guest g);
    double pointsToDollars(int pts);
    void removePoints(double amt, Guest g);
}
