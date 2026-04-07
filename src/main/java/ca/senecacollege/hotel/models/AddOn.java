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
    private String description;
    private Double price;
    private Boolean chargedNightly;

    public AddOn(){}

    public AddOn(String name, String desc, Double price, Boolean chargedNightly) {
        this.name = name;
        this.description = desc;
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

    @Override
    public String chargeName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddOn)) return false;
        AddOn a = (AddOn) o;
        return id != 0 && id == a.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
