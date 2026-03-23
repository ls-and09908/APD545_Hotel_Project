package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.services.AuthService;
import com.google.inject.Inject;

public class AdminController {

    private AuthService _authService;

    @Inject
    public AdminController(AuthService authService){
        _authService = authService;
    }

}
