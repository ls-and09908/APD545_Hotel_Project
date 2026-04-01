package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Reservation;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class ReservationRepository implements IReservationRepository {
    private EntityManagerFactory emf;
    private final SessionFactory sessionFactory;

    @Inject
    ReservationRepository(EntityManagerFactory emf,SessionFactory sessionFactory){
        this.emf = emf;
        this.sessionFactory = sessionFactory;
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

            em.merge(r);

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
            cq.select(reservation).where(cb.equal(reservation.get("reservationNumber"), resNum));

            result = em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            return result;
        }
    }

    @Override
    public Optional<Reservation> findById(int id) {
        try(Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.get(Reservation.class, id));
        }
    }
}
