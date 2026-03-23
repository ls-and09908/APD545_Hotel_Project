package ca.senecacollege.hotel.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Guest {
    @Id
    @GeneratedValue
    @Column(name = "GUEST_ID")
    private int id;

    private String name;

    @Column(length = 12)
    private String phone;

    private String email;

    private transient int loyaltyNum = -1;

    public Guest(){}

    public Guest(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Guest(String name, String phone, String email, int loyaltyNum) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.loyaltyNum = loyaltyNum;
    }

    public void setLoyalty(int loyaltyNum){
        this.loyaltyNum = loyaltyNum;
    }

    public boolean isLoyal(){
        return loyaltyNum != -1;
    }
}
