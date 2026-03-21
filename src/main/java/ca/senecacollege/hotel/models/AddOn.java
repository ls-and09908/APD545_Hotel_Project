package ca.senecacollege.hotel.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

//@Entity
public class AddOn implements ChargeSource {
   // @Id
   // @GeneratedValue
    private int id;

    private String name;
    private String desc;
    private Double price;
    private Boolean chargedNightly;

    public AddOn(){

    }

    @Override
    public Double getBasePrice() {
        return price;
    }

    @Override
    public Boolean isPaidNightly() {
        return chargedNightly;
    }
}
