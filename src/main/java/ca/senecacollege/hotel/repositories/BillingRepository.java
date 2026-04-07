package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Billing;
import com.google.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class BillingRepository implements IBillingRepository {
    private final SessionFactory sessionFactory;

    @Inject
    BillingRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Billing> getAllBillings() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Billing", Billing.class);
            return q.list();
        }
    }

    @Override
    public Optional<Billing> getBill(int billNum) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Billing.class, billNum));
        }
    }
}
