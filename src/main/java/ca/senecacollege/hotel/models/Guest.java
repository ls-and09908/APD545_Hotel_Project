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

    @Column(unique = true)
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

    public boolean isLoyal(){
        return loyaltyNum != null;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public Integer getLoyaltyNum() {
        return loyaltyNum;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLoyaltyNum(Integer loyaltyNum) {
        this.loyaltyNum = loyaltyNum;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}
