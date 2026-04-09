package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.LoyaltyTransaction;
import ca.senecacollege.hotel.models.Payment;

import java.util.List;

public interface ILoyaltyRepository {
    public List<LoyaltyTransaction> getAllLoyaltyTransactions();
    public void saveLoyaltyTransaction(LoyaltyTransaction lt);
}
