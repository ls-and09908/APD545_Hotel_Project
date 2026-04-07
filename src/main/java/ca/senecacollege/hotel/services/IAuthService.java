package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.AdminUser;

public interface IAuthService {

    public boolean authetnicateLogin(String user, String password);
}
