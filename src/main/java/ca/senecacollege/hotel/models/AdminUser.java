package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

@Entity
public class AdminUser {
    @Id
    @GeneratedValue
    private int userID;

    private String username;
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_type")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public AdminUser(){}

    public void setUsername(String username) {
        this.username = username;
    }
}
