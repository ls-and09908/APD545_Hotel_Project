package ca.senecacollege.hotel.utilities;

import ca.senecacollege.hotel.repositories.AdminUserRepository;
import ca.senecacollege.hotel.repositories.IAdminUserRepository;
import ca.senecacollege.hotel.services.AuthService;
import ca.senecacollege.hotel.services.ILoyaltyService;
import ca.senecacollege.hotel.services.LoyaltyService;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class AppModule extends AbstractModule {
    @Override
    protected void configure(){
        bind(ILoyaltyService.class).to(LoyaltyService.class).asEagerSingleton();
        bind(IAdminUserRepository.class).to(AdminUserRepository.class).asEagerSingleton();;

        bind(LoyaltyService.class).in(Singleton.class);
        bind(AuthService.class).in(Singleton.class);
    }
}
