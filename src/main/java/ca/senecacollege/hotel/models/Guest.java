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

    private String country;

    @Column(unique = true, nullable = true)
    private Integer loyaltyNum = null;

    private int loyaltyPoints = 0;

    public Guest(){}

    public Guest(String name, String phone, String email, String country) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.country = country;
    }

    public Guest(String name, String phone, String email, String country, int loyaltyNum) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.country = country;
        this.loyaltyNum = loyaltyNum;
    }

    public void makeLoyaltyMember(int loyaltyNum){
        this.loyaltyNum = loyaltyNum;
        this.loyaltyPoints = 0;
    }

    @Override
    public String toString(){
        return name + email;
    }
}
