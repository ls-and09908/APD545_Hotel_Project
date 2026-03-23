package ca.senecacollege.hotel.utilities;

import ca.senecacollege.hotel.repositories.AdminUserRepository;
import ca.senecacollege.hotel.repositories.IAdminUserRepository;
import ca.senecacollege.hotel.services.*;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class AppModule extends AbstractModule {
    @Override
    protected void configure(){
        bind(ILoyaltyService.class).to(LoyaltyService.class).asEagerSingleton();
        bind(IAdminUserRepository.class).to(AdminUserRepository.class).asEagerSingleton();;
        bind(IReservationService.class).to(ReservationService.class).asEagerSingleton();

        bind(LoyaltyService.class).in(Singleton.class);
        bind(AuthService.class).in(Singleton.class);
        bind(ReservationService.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public EntityManagerFactory provideEMF(){
        return Persistence.createEntityManagerFactory("hotel-persistence-unit");
    }
}
