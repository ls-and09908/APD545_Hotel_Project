package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Billing;
import ca.senecacollege.hotel.models.Reservation;

public interface IBillingService {
    Billing generateBill(Reservation r);
    int getWeekendDays(Reservation r);
    void saveBill(Billing b);
    void checkUpdateBillBalance(Billing b);
}
