package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Billing;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.repositories.IBillingRepository;
import com.google.inject.Inject;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class BillingService {
    private IBillingRepository _billRepo;

    @Inject
    public BillingService(IBillingRepository billRepo){
        this._billRepo = billRepo;
    }

    public Billing generateBill(Reservation r){
        // generates a new billing for a reservation, creating charges with appropriate pricing models

        return null;
    }

    public int getWeekendPricing(LocalDate checkin, LocalDate checkout){
        int count = 0;
        LocalDate reservationDay = checkin;
        while(!reservationDay.isEqual(checkout)){
            DayOfWeek day = reservationDay.getDayOfWeek();
            if(day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY){
                count += 1;
            }
            reservationDay.plusDays(1);
        }
        return count;
    }


}
