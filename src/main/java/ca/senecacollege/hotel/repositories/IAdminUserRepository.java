package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AdminUser;

import java.util.List;
import java.util.Optional;

public interface IAdminUserRepository {
    List<AdminUser> getAllAdminUsers();
    void saveAdminUser(AdminUser r);
    Optional<AdminUser> getAdminUser(String username);
}
