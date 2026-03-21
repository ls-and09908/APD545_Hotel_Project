package ca.senecacollege.hotel.models;

import java.time.LocalDate;

public class Payment {
    private Billing bill;
    private Double amount;
    private Guest payee;
    private PaymentMethod method;
    private LocalDate date;

    public int calculatePoints(){
        return 0;
    }
}
