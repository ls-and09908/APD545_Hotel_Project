package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Billing {
    @Id
    @GeneratedValue
    @Column(name = "BILL_NUM")
    private int billNumber;

    @OneToOne
    private Reservation reservation;

    @OneToMany(mappedBy = "bill")
    private List<Payment> payments;

    @OneToMany(mappedBy = "bill")
    private List<Charge> charges;

    private Double balance;

    public Double getTotal(){
        return 0.0;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Billing(Reservation r){
        this.reservation = r;
    }
}
