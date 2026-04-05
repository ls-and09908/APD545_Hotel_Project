package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.repositories.IAdminUserRepository;
import com.google.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        AdminUser comparedUser = adminRepo.getAdminUser(user);
        boolean authenticated = comparedUser != null && BCrypt.checkpw(password, comparedUser.getPasswordHash());
        _logService.loginAttempt(comparedUser, authenticated);
        return authenticated;
    }


}
