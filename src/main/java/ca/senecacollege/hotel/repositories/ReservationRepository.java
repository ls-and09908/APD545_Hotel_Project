package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Reservation;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class ReservationRepository implements IReservationRepository {
    private EntityManagerFactory emf;

    @Inject
    ReservationRepository(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
            Root<Reservation> reservation = cq.from(Reservation.class);
            cq.select(reservation);

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            return results;
        }
    }

    @Override
    public void saveRes(Reservation r) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(r);

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
    public Reservation getRes(int resNum) {
        Reservation result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
            Root<Reservation> reservation = cq.from(Reservation.class);
            cq.select(reservation).where(cb.equal(reservation.get("RES_NUM"), resNum));

            result = em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            return result;
        }
    }
}
