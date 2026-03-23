package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Billing {
    @Id
    @GeneratedValue
    @Column(name = "BILL_NUM")
    private int billNumber;

    @OneToOne
    private Reservation reservation;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<Charge> charges = new ArrayList<>();

    private Double balance;

    public Billing(Reservation r){
        this.reservation = r;
    }

    public Double getTotalPayments(){
        Double total = 0.0;
        for (Payment p: payments){
            total += p.getAmount();
        }

        return total;
    }

    public Double getTotalCharges(){
        Double total = 0.0;
        for (Charge c: charges){
            total += c.getTotal();
        }

        return total;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void addCharge(Charge c){
        this.charges.add(c);
    }

    public void addPayment(Payment p){
        this.payments.add(p);
    }
}
