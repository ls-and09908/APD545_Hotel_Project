package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Guest;
import com.google.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class GuestRepository implements IGuestRepository {
    private final SessionFactory sessionFactory;

    @Inject
    GuestRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Guest> getAllGuests() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Guest", Guest.class);
            return q.list();
        }
    }

    @Override
    public void saveGuest(Guest g) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            session.merge(g);
            tx.commit();
        }catch(RuntimeException e){
            if(tx!=null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Guest> getGuest(int guestID) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Guest.class, guestID));
        }
    }

    @Override
    public Optional<Guest> getLoyaltyMember(int loyaltyNum) {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Guest g WHERE g.loyaltyNum = :num", Guest.class);
            q.setParameter("num", loyaltyNum);
            return q.uniqueResultOptional();
        }
    }

    @Override
    public List<Guest> getLoyalGuests() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Guest g WHERE g.loyaltyNum IS NOT NULL", Guest.class);
            return q.list();
        }
    }

    @Override
    public Optional<Integer> getNewLoyaltyNumber() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("SELECT MAX(loyaltyNum) FROM Guest", Integer.class);
            return q.uniqueResultOptional();
        }
    }

    @Override
    public Optional<Guest> findGuestEmail(String email) {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Guest g WHERE g.email = :email", Guest.class);
            q.setParameter("email", email);
            return q.uniqueResultOptional();
        }
    }
}
