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

    @Enumerated(EnumType.STRING)
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

    public int getUserID() { return userID; }

    public String getRoleLabel(){ return role.label(); }

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
