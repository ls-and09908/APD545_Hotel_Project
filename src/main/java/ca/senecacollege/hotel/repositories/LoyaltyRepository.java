package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;
import ca.senecacollege.hotel.models.LoyaltyTransaction;
import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class LoyaltyRepository implements ILoyaltyRepository {
    private final SessionFactory sessionFactory;

    @Inject
    LoyaltyRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<LoyaltyTransaction> getAllLoyaltyTransactions() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM LoyaltyTransaction", LoyaltyTransaction.class);
            return q.list();
        }
    }

    @Override
    public void saveLoyaltyTransaction(LoyaltyTransaction lt) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            session.persist(lt);
            tx.commit();
        } catch(RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
