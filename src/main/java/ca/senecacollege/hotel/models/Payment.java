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
    @JoinColumn(name = "GUEST_ID", nullable = true)
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

    public LocalDate getDate(){
        return date;
    }

    public Payment(){}

    public Payment(Billing bill, Double amount, Guest payee, LocalDate date, PaymentMethod method) {
        this.bill = bill;
        this.amount = amount;
        this.payee = payee;
        this.date = date;
        this.method = method;
    }
}
