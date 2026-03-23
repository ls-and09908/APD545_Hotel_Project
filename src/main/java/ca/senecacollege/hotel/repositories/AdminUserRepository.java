package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AdminUser;

import java.util.List;

public class AdminUserRepository implements IAdminUserRepository {
    @Override
    public List<AdminUser> getAllAdminUsers() {
        return List.of();
    }

    @Override
    public void saveAdminUser(AdminUser r) {

    }

    @Override
    public AdminUser getAdminUser(String username) {
        return null;
    }
}
