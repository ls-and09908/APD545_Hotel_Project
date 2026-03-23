package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Billing;

import java.util.List;

public interface IBillingRepository {
    List<Billing> getAllBillings();
    void saveBill(Billing b);
    Billing getBill(int billNum);
}
