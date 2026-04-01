package ca.senecacollege.hotel.models;

import jakarta.persistence.*;
import ca.senecacollege.hotel.models.Role;

@Entity
public class AdminUser {
    @Id
    @GeneratedValue
    private int userID;

    @Column(name = "username")
    private String username;
    @Column(name = "PASSWORDHASH")
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_type")
    @Transient
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

    public AdminUser(String user, String passHash, Role role){
        username = user;
        passwordHash = passHash;
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPasswordHash(){return passwordHash;}
}
