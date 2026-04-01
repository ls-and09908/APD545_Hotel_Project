package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.repositories.IAdminUserRepository;
import com.google.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService implements IAuthService{
    private IAdminUserRepository adminRepo;

    @Inject
    public AuthService(IAdminUserRepository adminRepository){
        this.adminRepo = adminRepository;
    }

    @Override
    public boolean authetnicateLogin(String user, String password){
        AdminUser comparedUser = adminRepo.getAdminUser(user);
        return comparedUser != null && BCrypt.checkpw(password, comparedUser.getPasswordHash());
    }


}
