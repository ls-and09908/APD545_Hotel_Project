package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Waitlist;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class WaitlistRepository implements IWaitlistRepository {
    private EntityManagerFactory emf;

    @Inject
    WaitlistRepository(EntityManagerFactory emf){
        this.emf = emf;
    }
    @Override
    public List<Waitlist> getAllWaitlists() {
        List<Waitlist> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Waitlist> cq = cb.createQuery(Waitlist.class);
            Root<Waitlist> waitlist = cq.from(Waitlist.class);
            cq.select(waitlist);

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            return results;
        }
    }

    @Override
    public void saveWaitlist(Waitlist w) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.merge(w);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public Waitlist getWaitlist(int waitlistNum) {
        Waitlist result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Waitlist> cq = cb.createQuery(Waitlist.class);
            Root<Waitlist> waitlist = cq.from(Waitlist.class);
            cq.select(waitlist).where(cb.equal(waitlist.get("WAITLIST_NUM"), waitlistNum));

            result = em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            return result;
        }
    }
}
