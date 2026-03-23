package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.repositories.IBillingRepository;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BillingService implements IBillingService {
    private IBillingRepository _billRepo;
    private PricingModel _stdPrice;
    private PricingModel _wkndPrice;

    @Inject
    public BillingService(IBillingRepository billRepo, @Named("standard")PricingModel std, @Named("weekend")PricingModel wknd){
        this._billRepo = billRepo;
        this._stdPrice = std;
        this._wkndPrice = wknd;
    }

    @Override
    public Billing generateBill(Reservation r){
        /* Generates a new billing for a reservation:
           - creates charges with appropriate pricing models for rooms and addons on the reservation
           - sets the reservation's billing
           - returns the created billing
        */
        Billing bill = new Billing(r);

        int days = Math.toIntExact(ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut()));
        int weekendDays = getWeekendDays(r);


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
        r.setBilling(bill);
        return bill;
    }

    // returns the number of weekend nights in a reservation
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

    @Override
    public void checkUpdateBillBalance(Billing b) {
        Double balance = b.getTotalCharges() - b.getTotalPayments();
        b.setBalance(balance);
    }
}
