package ca.senecacollege.hotel.models;

import jakarta.persistence.*;


@Entity
public class AdminUser {
    @Id
    @GeneratedValue
    private int userID;

    private String username;

    public String getUsername() {
        return username;
    }

    public AdminUser(){}

    public void setUsername(String username) {
        this.username = username;
    }



}
