package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.models.Reservation;

import java.util.List;

public interface IAdminUserRepository {
    List<AdminUser> getAllAdminUsers();
    void saveAdminUser(AdminUser r);
    AdminUser getAdminUser(String username);
}
