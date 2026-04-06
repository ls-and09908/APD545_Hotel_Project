package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;

public interface IBillingService {
    Billing generateBill(Reservation r);
    Billing updateBillCharges(Reservation r);
    int getWeekendDays(Reservation r);
    void saveBill(Billing b);
    void checkUpdateBillBalance(Billing b);
    boolean addPaymentToBill(Double amount, PaymentMethod type, Billing bill);
    boolean applyDiscount(Billing b, double percent, Role user);
    double getDeposit(Billing b);
}
