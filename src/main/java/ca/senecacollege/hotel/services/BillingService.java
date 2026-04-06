package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.repositories.IBillingRepository;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BillingService implements IBillingService {
    private final IBillingRepository _billRepo;
    private final PricingModel _stdPrice;
    private final PricingModel _wkndPrice;
    private final double adminDiscount;
    private final double managerDiscount;
    private final double depositPercent;

    @Inject
    public BillingService(IBillingRepository billRepo, @Named("standard")PricingModel std, @Named("weekend")PricingModel wknd, @Named("adminDiscount")double adminDiscount, @Named("managerDiscount")double managerDiscount, @Named("depositPercent")double depositPercent){
        this._billRepo = billRepo;
        this._stdPrice = std;
        this._wkndPrice = wknd;
        this.adminDiscount = adminDiscount;
        this.managerDiscount = managerDiscount;
        this.depositPercent = depositPercent;
    }

    /**
     * Generates a new bill for a newly created reservation. Should only be called once in the lifetime of a reservation.
     * <p>Assumes the reservation already has a valid check-in and check-out date, and at least one room added to the reservation.</p>
     * <p>Creates charges for rooms and addons on the reservation.
     * Assigns the generated bill to the reservation.</p>
     * @param r Reservation to generate the bill for
     * @return the generated bill
     */
    @Override
    public Billing generateBill(Reservation r){
        Billing bill = new Billing(r);
        r.setBilling(bill);
        return updateBillCharges(r);
    }

    @Override
    public Billing updateBillCharges(Reservation r){
        Billing bill = r.getBilling();
        List<Charge> billCharges = bill.getCharges();

        for (Charge c: billCharges){
            c.setBill(null);
        }
        bill.setCharges(new ArrayList<>());

        int days = Math.toIntExact(ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut()));
        int weekendDays = getWeekendDays(r);

        // recreate charges
        for (Room rm : r.getRooms()){
            // TODO: Add logic for seasonal pricing
            if(weekendDays > 0){
                Charge weekendCharge = new Charge(rm, bill, weekendDays, _wkndPrice);
                bill.addCharge(weekendCharge);
            }
            if(days - weekendDays > 0){
                Charge weekdayCharge = new Charge(rm, bill, days - weekendDays, _stdPrice);
                bill.addCharge(weekdayCharge);
            }
        }

        for (AddOn a : r.getAddOns()){
            Charge addonCharge;
            if(a.isPaidNightly()){
                addonCharge = new Charge(a, bill, days, _stdPrice);
                bill.addCharge(addonCharge);
            } else {
                addonCharge = new Charge(a, bill, 1, _stdPrice);
                bill.addCharge(addonCharge);
            }
        }

        checkUpdateBillBalance(bill);
        return bill;
    }

    /**
     * Calculates the number of weekend nights in a particular reservation
     * @param r Reservation to check
     * @return the number of weekend nights in a reservation
     */
    @Override
    public int getWeekendDays(Reservation r){
        int count = 0;
        LocalDate reservationDay = r.getCheckIn();
        while(!reservationDay.isEqual(r.getCheckOut())){
            DayOfWeek day = reservationDay.getDayOfWeek();
            if(day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY){
                count += 1;
            }
            reservationDay = reservationDay.plusDays(1);
        }
        return count;
    }

    public void saveBill(Billing b){
        _billRepo.saveBill(b);
    }

    /**
     * Recalculates the balance for the specified bill as (total charges)-(total payments)
     * <p>Meant to be called after additional charges or payments have been added to a bill.</p>
     * @param b Bill to update
     */
    @Override
    public void checkUpdateBillBalance(Billing b) {
        b.calculateBalance();
    }

    /**
     * Adds a payment to a bill and updates the bill balance if it is successful, ensuring that the bill balance will not go into negative values.
     * @param bill Bill to apply the payment to
     * @return false if the payment would cause the bill balance to be negative
     */
    @Override
    public boolean addPaymentToBill(Double amount, PaymentMethod type, Billing bill) {
        bill.calculateBalance();
        Payment payment = new Payment(bill, amount, bill.getReservation().getGuest(), LocalDate.now(), type);

        double newBalance = bill.getBalance() - payment.getAmount();
        if(newBalance < 0.0){
            return false;
        }

        bill.addPayment(payment);
        bill.setBalance(newBalance);
        return true;
    }

    @Override
    public boolean applyDiscount(Billing b, double percent, Role user){
        if(user == Role.MANAGER && percent > managerDiscount){
            return false;
        }
        if(user == Role.ADMINISTRATOR && percent > adminDiscount){
            return false;
        }
        b.setDiscount(percent);
        checkUpdateBillBalance(b);
        return true;
    }

    @Override
    public double getDeposit(Billing b) {
        return (b.getTotalCharges() + b.getTax())*depositPercent/100;
    }
}
