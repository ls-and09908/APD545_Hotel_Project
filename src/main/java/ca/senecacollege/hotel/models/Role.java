package ca.senecacollege.hotel.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
class Role {
    @Id
    @GeneratedValue
    private int roleID;

    private String type;
    private String discountMax;
}
