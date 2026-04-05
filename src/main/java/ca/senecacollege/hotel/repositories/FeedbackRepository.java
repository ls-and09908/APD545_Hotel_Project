package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;
import ca.senecacollege.hotel.models.Feedback;
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

public class FeedbackRepository implements IFeedbackRepository {
    private final SessionFactory sessionFactory;

    @Inject
    FeedbackRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Feedback", Feedback.class);
            return q.list();
        }
    }

    @Override
    public void saveFeedback(Feedback fb) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            session.merge(fb);
            tx.commit();
        }catch(RuntimeException e){
            if(tx!=null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Feedback> getFeedback(int feedbackId) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Feedback.class, feedbackId));
        }
    }
}
