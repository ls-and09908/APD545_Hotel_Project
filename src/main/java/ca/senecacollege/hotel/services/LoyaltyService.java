package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.repositories.IGuestRepository;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.List;
import java.util.Optional;

public class LoyaltyService implements ILoyaltyService {
    private IGuestRepository _guestRepo;
    private int _ptsEarnRate;
    private int _redemptionCap;
    private int _ptsPerDollar;

    @Inject
    public LoyaltyService(IGuestRepository _guestRepo, @Named("earnRate") int _ptsEarnRate,@Named("redemptionCap") int _redemptionCap,@Named("conversionRate") int _ptsPerDollar) {
        this._guestRepo = _guestRepo;
        this._ptsEarnRate = _ptsEarnRate;
        this._redemptionCap = _redemptionCap;
        this._ptsPerDollar = _ptsPerDollar;
    }

    @Override
    public boolean findLoyalGuest(Guest checkingGuest){
        List<Guest> loyalGuests = _guestRepo.getLoyalGuests();
        return loyalGuests.contains(checkingGuest);
    }

    @Override
    public Guest getLoyalGuest(int loyaltyNum) {
        Optional<Guest> guest = _guestRepo.getLoyaltyMember(loyaltyNum);
        return guest.orElse(null);
    }

    /**
     * Retrieves the next number to be used for a new loyalty member
     */
    @Override
    public int getNewLoyaltyNum() {
        return _guestRepo.getNewLoyaltyNumber().orElse(1) + 1;
    }

    /**
     * Calculates the number of points earned from a dollar amount paid.
     * @param paymentAmt total amount paid in dollars and cents
     * @return the number of points earned
     */
    @Override
    public int getPointsFromPayment(double paymentAmt) {
        if(paymentAmt < 0) return 0;
        return (int) (paymentAmt * _ptsEarnRate);
    }

    @Override
    public void earnPoints(int pts, Guest g){
        if(g.isLoyal()) g.setLoyaltyPoints(g.getLoyaltyPoints() + pts);
    }

    @Override
    public int spendPoints(double amt, Guest g){
        int pts = (int) amt*_ptsPerDollar;
        if(g.isLoyal()) g.setLoyaltyPoints(g.getLoyaltyPoints() - pts);
        return pts;
    }

    @Override
    public void removePoints(double amt, Guest g) {

    }

    @Override
    public double pointsToDollars(int pts){
        if (pts > _redemptionCap) return _redemptionCap/_ptsPerDollar;
        return pts/_ptsPerDollar;
    }

}
