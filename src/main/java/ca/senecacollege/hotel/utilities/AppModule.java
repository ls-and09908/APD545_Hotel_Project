package ca.senecacollege.hotel.utilities;

import ca.senecacollege.hotel.services.ILoyaltyService;
import ca.senecacollege.hotel.services.LoyaltyService;
import ca.senecacollege.hotel.tests.DBTester;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class AppModule extends AbstractModule {
    @Override
    protected void configure(){
        bind(ILoyaltyService.class).to(LoyaltyService.class).in(Singleton.class);

        bind(DBTester.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public EntityManagerFactory provideEMF(){
        return Persistence.createEntityManagerFactory("hotel-persistence-unit");
    }
}
