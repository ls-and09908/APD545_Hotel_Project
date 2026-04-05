package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Billing;
import ca.senecacollege.hotel.models.Payment;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class PaymentRepository implements IPaymentRepository {
    private EntityManagerFactory emf;

    @Inject
    PaymentRepository(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
            Root<Payment> pay = cq.from(Payment.class);
            cq.select(pay);

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return results;
    }

    @Override
    public void savePayment(Payment p) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.merge(p);

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
}
