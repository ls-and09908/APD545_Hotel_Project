package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;
import ca.senecacollege.hotel.models.Billing;
import ca.senecacollege.hotel.models.Payment;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class PaymentRepository implements IPaymentRepository {
    private final SessionFactory sessionFactory;

    @Inject
    PaymentRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Payment> getAllPayments() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Payment", Payment.class);
            return q.list();
        }
    }
}
