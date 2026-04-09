package ca.senecacollege.hotel.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class LoyaltyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer transactionId;

    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "GUEST_ID")
    private Guest guest;

    private int transactionPoints;
    private int newBalance;
    private String transactionType;

    public LoyaltyTransaction() {
    }

    public LoyaltyTransaction(LocalDateTime time, Guest guest, int transactionPoints, int newBalance, String transactionType) {
        this.time = time;
        this.guest = guest;
        this.transactionPoints = transactionPoints;
        this.newBalance = newBalance;
        this.transactionType = transactionType;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public int getTransactionPoints() {
        return transactionPoints;
    }

    public void setTransactionPoints(int transactionPoints) {
        this.transactionPoints = transactionPoints;
    }

    public int getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(int newBalance) {
        this.newBalance = newBalance;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getTransactionId() {
        return transactionId;
    }
}
