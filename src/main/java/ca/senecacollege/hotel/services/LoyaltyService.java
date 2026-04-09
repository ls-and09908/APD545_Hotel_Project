package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.models.LoyaltyTransaction;
import ca.senecacollege.hotel.repositories.IGuestRepository;
import ca.senecacollege.hotel.repositories.ILoyaltyRepository;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoyaltyService implements ILoyaltyService {
    private final ILoyaltyRepository _loyaltyRepo;
    private final IGuestRepository _guestRepo;
    private int _ptsEarnRate;
    private int _redemptionCap;
    private int _ptsPerDollar;
    private boolean holdTransactions = false;
    private List<LoyaltyTransaction> pendingTransactions = new ArrayList<>();


    @Inject
    public LoyaltyService(ILoyaltyRepository loyaltyRepo, IGuestRepository _guestRepo, @Named("earnRate") int _ptsEarnRate, @Named("redemptionCap") int _redemptionCap, @Named("conversionRate") int _ptsPerDollar) {
        this._loyaltyRepo = loyaltyRepo;
        this._guestRepo = _guestRepo;
        this._ptsEarnRate = _ptsEarnRate;
        this._redemptionCap = _redemptionCap;
        this._ptsPerDollar = _ptsPerDollar;
    }

    @Override
    public void setHoldTransactions(boolean hold){
        this.holdTransactions = hold;
    }

    @Override
    public void clearTransactions(){
        pendingTransactions.clear();
    }

    @Override
    public void saveHeldTransactions(){
        holdTransactions = false;
        for(LoyaltyTransaction lt : pendingTransactions) {
            _loyaltyRepo.saveLoyaltyTransaction(lt);
        }
        clearTransactions();
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
        if(g.isLoyal()) {
            int balance = g.getLoyaltyPoints() + pts;
            g.setLoyaltyPoints(balance);
            LoyaltyTransaction lt = new LoyaltyTransaction(LocalDateTime.now(), g, pts, balance, "earn");
            keepTransaction(lt);
        }
    }

    @Override
    public int spendPoints(double amt, Guest g){
        int pts = (int) amt*_ptsPerDollar;
        if(g.isLoyal()){
            int balance = g.getLoyaltyPoints() - pts;
            g.setLoyaltyPoints(balance);
            LoyaltyTransaction lt = new LoyaltyTransaction(LocalDateTime.now(), g, pts, balance, "redeem");
            keepTransaction(lt);
        }
        return pts;
    }

    @Override
    public void removePoints(double amt, Guest g) {
        int pts = getPointsFromPayment(amt);
        if(g.isLoyal()){
            int balance = g.getLoyaltyPoints() - pts;
            g.setLoyaltyPoints(balance);
            LoyaltyTransaction lt = new LoyaltyTransaction(LocalDateTime.now(), g, pts, balance, "refund");
            keepTransaction(lt);
        }
    }

    @Override
    public double pointsToDollars(int pts){
        if (pts > _redemptionCap) return _redemptionCap/_ptsPerDollar;
        return pts/_ptsPerDollar;
    }

    @Override
    public List<LoyaltyTransaction> getLoyaltyTransactions(){
        return _loyaltyRepo.getAllLoyaltyTransactions();
    }

    @Override
    public void keepTransaction(LoyaltyTransaction lt){
        if(holdTransactions){
            pendingTransactions.add(lt);
        } else {
            _loyaltyRepo.saveLoyaltyTransaction(lt);
        }
    }

}
