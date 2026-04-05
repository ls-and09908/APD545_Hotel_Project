package ca.senecacollege.hotel.repositories;

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

public class FeedbackRepository implements IFeedbackRepository {
    private EntityManagerFactory emf;
    private final SessionFactory sessionFactory;

    @Inject
    FeedbackRepository(EntityManagerFactory emf, SessionFactory sessionFactory){
        this.emf = emf;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        List<Feedback> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Feedback> cq = cb.createQuery(Feedback.class);
            Root<Feedback> feedback = cq.from(Feedback.class);
            cq.select(feedback);

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            return results;
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
    public Feedback getFeedback(int feedbackId) {
        List<Feedback> result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Feedback> cq = cb.createQuery(Feedback.class);
            Root<Feedback> feedback = cq.from(Feedback.class);
            cq.select(feedback).where(cb.equal(feedback.get("FEEDBACK_ID"), feedbackId));

            result = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        if(result.isEmpty()){
            return null;
        } else {
            return result.get(0);
        }
    }
}
