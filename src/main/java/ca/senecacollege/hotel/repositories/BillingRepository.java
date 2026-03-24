package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Billing;
import ca.senecacollege.hotel.models.Room;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class BillingRepository implements IBillingRepository {
    private EntityManagerFactory emf;

    @Inject
    BillingRepository(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public List<Billing> getAllBillings() {
        List<Billing> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Billing> cq = cb.createQuery(Billing.class);
            Root<Billing> bill = cq.from(Billing.class);
            cq.select(bill);

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return results;
    }

    @Override
    public void saveBill(Billing b) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(b);

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
    public Billing getBill(int billNum) {
        Billing result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Billing> cq = cb.createQuery(Billing.class);
            Root<Billing> bill = cq.from(Billing.class);
            cq.select(bill).where(cb.equal(bill.get("BILL_NUM"), billNum));

            result = em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }
}
