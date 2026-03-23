package ca.senecacollege.hotel.utilities;

import ca.senecacollege.hotel.services.ILoyaltyService;
import ca.senecacollege.hotel.services.LoyaltyService;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class AppModule extends AbstractModule {
    @Override
    protected void configure(){
        bind(ILoyaltyService.class).to(LoyaltyService.class).asEagerSingleton();
        bind(LoyaltyService.class).in(Singleton.class);
    }
}
