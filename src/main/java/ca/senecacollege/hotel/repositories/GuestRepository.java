package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Guest;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GuestRepository implements IGuestRepository {
    /*//Just storing guests in memory for now. Will update
    private ObservableList<Guest> guestList = FXCollections.observableArrayList();

    public void addGuest(Guest g){
        guestList.add(g);
    }

    //This should theoretically return all the loyal guests of all the guests. Willing to hear another way of doing it with SQL
    public HashSet<Guest> getLoyalGuests(){
        HashSet<Guest> loyalGuests = new HashSet<>();
        for(Guest i : guestList){
            if(i.isLoyal()){
                loyalGuests.add(i);
            }
        }
        return loyalGuests;
    }*/
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

            em.persist(g);

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
        Guest result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Guest> cq = cb.createQuery(Guest.class);
            Root<Guest> guest = cq.from(Guest.class);
            cq.select(guest).where(cb.equal(guest.get("GUEST_ID"), guestID));

            result = em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
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
            return results;
        }
    }
}
