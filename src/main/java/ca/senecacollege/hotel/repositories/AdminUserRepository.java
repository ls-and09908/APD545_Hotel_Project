package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;
import ca.senecacollege.hotel.models.AdminUser;
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
import java.util.Optional;

public class AdminUserRepository implements IAdminUserRepository {
    private final SessionFactory sessionFactory;
    @Inject
    public AdminUserRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<AdminUser> getAllAdminUsers() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM AdminUser", AdminUser.class);
            return q.list();
        }
    }

    @Override
    public void saveAdminUser(AdminUser r) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            session.merge(r);
            tx.commit();
        } catch(RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<AdminUser> getAdminUser(String username) {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM AdminUser a WHERE a.username = :username", AdminUser.class);
            q.setParameter("username", username);
            return q.uniqueResultOptional();
        }
    }
}
