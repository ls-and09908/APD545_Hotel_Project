package ca.senecacollege.hotel.utilities;

import ca.senecacollege.hotel.models.PricingModel;
import ca.senecacollege.hotel.models.StandardPricingModel;
import ca.senecacollege.hotel.models.WeekendPricingModel;
import ca.senecacollege.hotel.repositories.*;
import ca.senecacollege.hotel.services.*;
import ca.senecacollege.hotel.tests.DBTester;
import ca.senecacollege.hotel.services.*;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
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

        bind(IRoomRepository.class).to(RoomRepository.class).in(Singleton.class);
        bind(IBillingRepository.class).to(BillingRepository.class).in(Singleton.class);
        bind(IBillingService.class).to(BillingService.class).in(Singleton.class);
        bind(PricingModel.class).annotatedWith(Names.named("standard")).to(StandardPricingModel.class).in(Singleton.class);
        bind(PricingModel.class).annotatedWith(Names.named("weekend")).to(WeekendPricingModel.class).in(Singleton.class);
        bind(DBTester.class).asEagerSingleton();
        bind(ReservationService.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public EntityManagerFactory provideEMF(){
        return Persistence.createEntityManagerFactory("hotel-persistence-unit");
    }
}
