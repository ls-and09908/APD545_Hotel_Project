package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Billing;
import com.google.inject.Inject;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class BillingRepository implements IBillingRepository {
    private EntityManagerFactory emf;

    @Inject
    BillingRepository(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public List<Billing> getAllBillings() {
        return List.of();
    }

    @Override
    public void saveBill(Billing b) {

    }

    @Override
    public Billing getBill(int billNum) {
        return null;
    }
}
