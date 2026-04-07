package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Waitlist;
import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class WaitlistRepository implements IWaitlistRepository {
    private final SessionFactory sessionFactory;

    @Inject
    WaitlistRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public List<Waitlist> getAllWaitlists() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Waitlist", Waitlist.class);
            return q.list();
        }
    }

    @Override
    public void saveWaitlist(Waitlist w) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            session.merge(w);
            tx.commit();
        }catch(RuntimeException e){
            if(tx!=null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Waitlist> getWaitlist(int waitlistNum) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Waitlist.class, waitlistNum));
        }
    }
    @Override
    public void removeWaitlist(Waitlist w){
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            session.remove(w);
            tx.commit();
        }catch (RuntimeException e){
            if(tx!=null) tx.rollback();
            throw e;
        }
    }

}
