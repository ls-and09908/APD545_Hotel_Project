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

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Charge> charges = new ArrayList<>();

    private Double balance;

    private Double discount = 0.0;

    public Billing(){}

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

    public Double getAddOnCharges(){
        Double total = 0.0;
        for (Charge c: charges){
            if (c.getSource().getClass() == AddOn.class){
                total += c.getTotal();
            }
        }
        return total;
    }

    public Double getRoomCharges(){
        Double total = 0.0;
        for (Charge c: charges){
            if (c.getSource().getClass() == Room.class){
                total += c.getTotal();
            }
        }
        return total;
    }

    public Reservation getReservation(){
        return reservation;
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

    public Double getBalance() {
        return balance;
    }

    public List<Charge> getCharges(){ return this.charges; }

    public List<Payment> getPayments(){ return this.payments; }

    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    public void setDiscount(double discount){
        this.discount = discount;
    }

    public Double getDiscount() {
        return discount;
    }

    public Double getTax(){
        return this.getTotalCharges()*0.13;
    }

    // only stage of Billing calculations that actually calculates tax
    public void calculateBalance(){
        double balance = this.getTotalCharges();
        balance = balance + getTax() - (this.discount/100)*balance - this.getTotalPayments();
        setBalance(balance);
    }
}
