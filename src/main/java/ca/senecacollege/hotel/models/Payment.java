package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    @Column(name = "PAYMENT_NUM")
    private int paymentNumber;

    @ManyToOne
    @JoinColumn(name = "BILL_NUM")
    private Billing bill;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "GUEST_ID")
    private Guest payee;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private LocalDate date;

    public Guest getPayee() {
        return payee;
    }

    public void setPayee(Guest payee) {
        this.payee = payee;
    }

    public Billing getBill() {
        return bill;
    }

    public void setBill(Billing bill) {
        this.bill = bill;
    }

    public Double getAmount() {
        return amount;
    }

    public Payment(Billing bill, Double amount, Guest payee, LocalDate date) {
        this.bill = bill;
        this.amount = amount;
        this.payee = payee;
        this.date = date;
    }

    public Payment(){}

    public int calculatePoints(){
        return 0;
    }
}
