package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.repositories.IBillingRepository;
import ca.senecacollege.hotel.repositories.IReservationRepository;
import ca.senecacollege.hotel.utilities.UserContext;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BillingService implements IBillingService {
    private final IReservationService _resService;
    private final IBillingRepository _billRepo;
    private final IActivityLogService _logService;
    private final ILoyaltyService _loyaltyService;
    private final PricingModel _stdPrice;
    private final PricingModel _wkndPrice;
    private final double adminDiscount;
    private final double managerDiscount;
    private final double depositPercent;

    @Inject
    public BillingService(IReservationService resService, IBillingRepository billRepo, IActivityLogService logService, ILoyaltyService loyaltyService, @Named("standard")PricingModel std, @Named("weekend")PricingModel wknd, @Named("adminDiscount")double adminDiscount, @Named("managerDiscount")double managerDiscount, @Named("depositPercent")double depositPercent){
        _resService = resService;
        this._billRepo = billRepo;
        this._logService = logService;
        this._loyaltyService = loyaltyService;
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
        r.setBilling(bill);
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
        if(amount != 0.0) {
            Reservation r = bill.getReservation();
            Guest guest = r.getGuest();
            bill.calculateBalance();
            Payment payment = new Payment(bill, amount, guest, LocalDate.now(), type);
            if (type == PaymentMethod.REFUND && amount >= 0.0) {
                _logService.processRefund(bill, amount, type, false);
                return false;
            }

            double newBalance = bill.getBalance() - payment.getAmount();
            if (Math.round(newBalance * 100.0) / 100.0 < 0.0) {
                _logService.processPayment(bill, amount, type, false);
                return false;
            }

            bill.addPayment(payment);
            bill.calculateBalance();
//            bill.setBalance(newBalance);
            r.setBilling(bill);
            if (type == PaymentMethod.DEPOSIT) {
                r.setStatus(ReservationStatus.BOOKED);
                _resService.saveNewReservation(r);
            }
            if (type == PaymentMethod.REFUND) {_logService.processRefund(bill, amount, type, true);}
            _logService.processPayment(bill, amount, type, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean applyDiscount(Billing b, double percent){
        if(UserContext.getUser().getRole() == Role.MANAGER && percent > managerDiscount){
            _logService.processDiscount(b, false, percent);
            return false;
        }
        if(UserContext.getUser().getRole() == Role.ADMINISTRATOR && percent > adminDiscount){
            _logService.processDiscount(b, false, percent);
            return false;
        }
        Reservation r = b.getReservation();
        b.setDiscount(percent);
        checkUpdateBillBalance(b);

        r.setBilling(b);
        _resService.saveReservation(r);
        _logService.processDiscount(b, true, percent);
        return true;
    }

    @Override
    public double getDeposit(Billing b) {
        return b.getTotalCharges()*depositPercent/100;
    }

    @Override
    public double getRefundableAmount(Billing b, boolean includeDeposit) {
        double total = 0.0;
        List<Payment> billPayments = b.getPayments();
        for (Payment p: billPayments){
            switch(p.getMethod()){
                case DEPOSIT:
                    if(includeDeposit) total += p.getAmount();
                    break;
                case LOYALTY:
                    break;
                default:
                    total += p.getAmount();
            }
        }
        return total;
    }

    @Override
    public double refundCancellation(Billing b, boolean refundDeposit) {
        double refund = getRefundableAmount(b, refundDeposit);
        if (addPaymentToBill(-refund, PaymentMethod.REFUND, b)) return refund;
        return 0.0;
    }

    @Override
    public double earlyCheckoutRefund(){
        return 0;
    }
}
