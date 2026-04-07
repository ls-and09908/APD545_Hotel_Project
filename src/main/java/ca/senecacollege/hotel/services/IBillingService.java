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
    double getRefundableAmount(Billing b, boolean includeDeposit);

    /**
     * Attempts to refund all refundable payments made on a bill.
     * @param b the bill to apply the refund to
     * @param refundDeposit whether to include the bill's deposit in the refund
     * @return the amount refunded, or 0 if it was unsuccessful
     */
    double refundCancellation(Billing b, boolean refundDeposit);
    double earlyCheckoutRefund();
}
