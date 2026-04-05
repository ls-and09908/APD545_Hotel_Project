package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AuditLog;
import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class AuditLogRepository implements IAuditLogRepository {
    private final SessionFactory sessionFactory;

    @Inject
    public AuditLogRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {
        try(Session session = sessionFactory.openSession()) {
            var q =  session.createQuery("FROM AuditLog", AuditLog.class);
            return q.list();
        }
    }

    @Override
    public void saveAuditLog(AuditLog a) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            session.merge(a);
            tx.commit();
        }catch(RuntimeException e){
            if(tx!=null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<AuditLog> getAuditLog(int id) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(AuditLog.class, id));
        }
    }
}
