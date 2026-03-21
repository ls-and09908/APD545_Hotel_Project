package ca.senecacollege.hotel.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;

//@Entity
public class Billing {
//    @Id
//    @GeneratedValue
    private int billNumber;

    private Reservation reservation;
    private List<Payment> payments;
    private List<Charge> charges;
    private Double balance;

    public Double getTotal(){
        return 0.0;
    }

}
