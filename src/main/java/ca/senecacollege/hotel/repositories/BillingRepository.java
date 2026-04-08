package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Billing;
import ca.senecacollege.hotel.models.Reservation;
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
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            var q = session.createQuery("FROM Billing", Billing.class);
            tx.commit();
            return q.list();
        }
    }

    @Override
    public void saveBill(Billing b) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();

            Billing bill = session.merge(b);
            Reservation r = session.find(Reservation.class, b.getReservation().getReservationNumber());
            if(r != null){
                r.setBilling(bill);
                bill.setReservation(r);
            }
            tx.commit();
        }catch(RuntimeException e){
            if(tx!=null) tx.rollback();
            throw e;
        }
    }


}
