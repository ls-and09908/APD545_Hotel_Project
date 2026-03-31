package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue
    private int roleID;

    private String type;
    private String discountMax;

    public Role(String type, String discountMax){
        this.type = type;
        this.discountMax = discountMax;
    }
}
