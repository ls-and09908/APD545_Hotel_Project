package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;
import ca.senecacollege.hotel.models.Reservation;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class AddonRepository implements IAddonRepository {
    private EntityManagerFactory emf;

    @Inject
    AddonRepository(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public List<AddOn> getAllAddOns() {
        List<AddOn> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<AddOn> cq = cb.createQuery(AddOn.class);
            Root<AddOn> addOn = cq.from(AddOn.class);
            cq.select(addOn);

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            return results;
        }
    }

    @Override
    public void saveAddOn(AddOn a) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.merge(a);

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
    public AddOn getAddOn(int addOnID) {
        List<AddOn> result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<AddOn> cq = cb.createQuery(AddOn.class);
            Root<AddOn> addOn = cq.from(AddOn.class);
            cq.select(addOn).where(cb.equal(addOn.get("ADDON_ID"), addOnID));

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
    public AddOn getAddOn(String name) {
        List<AddOn> result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<AddOn> cq = cb.createQuery(AddOn.class);
            Root<AddOn> addOn = cq.from(AddOn.class);
            cq.select(addOn).where(cb.like(addOn.get("name"), name.toUpperCase()));

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
