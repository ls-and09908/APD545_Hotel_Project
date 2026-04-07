package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.repositories.IAdminUserRepository;
import ca.senecacollege.hotel.utilities.UserContext;
import com.google.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AuthService implements IAuthService{
    private IAdminUserRepository adminRepo;
    private IActivityLogService _logService;

    @Inject
    public AuthService(IAdminUserRepository adminRepository, IActivityLogService logService){
        this.adminRepo = adminRepository;
        this._logService = logService;
    }

    @Override
    public boolean authetnicateLogin(String user, String password){
        Optional<AdminUser> comparedUser = adminRepo.getAdminUser(user);
        boolean authenticated = comparedUser.isPresent() && BCrypt.checkpw(password, comparedUser.get().getPasswordHash());

        // if the user was not found, no logging occurs
        comparedUser.ifPresent(adminUser -> {
            UserContext.setUser(adminUser);
            _logService.loginAttempt(authenticated);
        });
        return authenticated ;
    }
}
