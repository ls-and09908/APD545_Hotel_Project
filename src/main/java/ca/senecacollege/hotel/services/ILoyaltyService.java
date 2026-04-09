package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.models.LoyaltyTransaction;
import ca.senecacollege.hotel.repositories.GuestRepository;

import java.util.List;

public interface ILoyaltyService {
    void setHoldTransactions(boolean hold);

    void clearTransactions();

    void saveHeldTransactions();

    boolean findLoyalGuest(Guest checkingGuest);
    Guest getLoyalGuest(int loyaltyNum);
    int getNewLoyaltyNum();
    int getPointsFromPayment(double paymentAmt);
    void earnPoints(int pts, Guest g);
    int spendPoints(double amt, Guest g);
    double pointsToDollars(int pts);
    void removePoints(double amt, Guest g);

    List<LoyaltyTransaction> getLoyaltyTransactions();

    void keepTransaction(LoyaltyTransaction lt);
}
