package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Guest;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class GuestRepository implements IGuestRepository {
    private EntityManagerFactory emf;

    @Inject
    GuestRepository(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public List<Guest> getAllGuests() {
        List<Guest> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Guest> cq = cb.createQuery(Guest.class);
            Root<Guest> guest = cq.from(Guest.class);
            cq.select(guest);

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            return results;
        }
    }

    @Override
    public void saveGuest(Guest g) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.merge(g);

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
    public Guest getGuest(int guestID) {
        List<Guest> result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Guest> cq = cb.createQuery(Guest.class);
            Root<Guest> guest = cq.from(Guest.class);
            cq.select(guest).where(cb.equal(guest.get("GUEST_ID"), guestID));

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

    @Override
    public Guest getLoyaltyMember(int loyaltyNum) {
        List<Guest> result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Guest> cq = cb.createQuery(Guest.class);
            Root<Guest> guest = cq.from(Guest.class);
            cq.select(guest).where(cb.equal(guest.get("loyaltyNum"), loyaltyNum));

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

    @Override
    public List<Guest> getLoyalGuests() {
        List<Guest> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Guest> cq = cb.createQuery(Guest.class);
            Root<Guest> guest = cq.from(Guest.class);
            cq.select(guest).where(cb.notEqual(guest.get("loyaltyNum"), -1));

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return results;
    }

    @Override
    public int getNewLoyaltyNumber() {
        Integer result = 0;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Guest> root = cq.from(Guest.class);
            cq.select(cb.max(root.get("loyaltyNum")));

            result = em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        if (result == null){
            result = 0;
        }
        return result + 1;
    }
}
