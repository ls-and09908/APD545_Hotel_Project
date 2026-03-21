package ca.senecacollege.hotel.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class AddOn implements ChargeSource {
    @Id
    @GeneratedValue
    @Column(name = "ADDON_ID")
    private int id;

    private String name;
    private String desc;
    private Double price;
    private Boolean chargedNightly;

    public AddOn(String name, String desc, Double price, Boolean chargedNightly) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.chargedNightly = chargedNightly;
    }

    @Override
    public Double getBasePrice() {
        return price;
    }

    @Override
    public Boolean isPaidNightly() {
        return chargedNightly;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Object obj){
        if (obj == null){ return false; }
        if (!this.getClass().equals(obj.getClass())){ return false; }

        AddOn tempObj = (AddOn) obj;
        return this.id == tempObj.getId();
    }

    public int hashCode(){
        // TODO write hashCode func
        return 0;
    }
}
