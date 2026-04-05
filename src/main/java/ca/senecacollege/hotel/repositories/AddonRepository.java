package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;
import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class AddonRepository implements IAddonRepository {
    private final SessionFactory sessionFactory;

    @Inject
    AddonRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<AddOn> getAllAddOns() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM AddOn", AddOn.class);
            return q.list();
        }
    }

    @Override
    public void saveAddOn(AddOn a) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            session.merge(a);
            tx.commit();
        }catch(RuntimeException e){
            if(tx!=null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<AddOn> getAddOn(int addOnID) {
//        List<AddOn> result = null;
//        EntityManager em = emf.createEntityManager();
//        try {
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<AddOn> cq = cb.createQuery(AddOn.class);
//            Root<AddOn> addOn = cq.from(AddOn.class);
//            cq.select(addOn).where(cb.equal(addOn.get("id"), addOnID));
//
//            result = em.createQuery(cq).getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            em.close();
//        }
//        if(result.isEmpty()){
//            return null;
//        } else {
//            return result.get(0);
//        }
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(AddOn.class, addOnID));
        }
    }

    @Override
    public Optional<AddOn> getAddOn(String name) {
//        List<AddOn> result = null;
//        try (EntityManager em = emf.createEntityManager()) {
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<AddOn> cq = cb.createQuery(AddOn.class);
//            Root<AddOn> addOn = cq.from(AddOn.class);
//            cq.select(addOn).where(cb.like(addOn.get("name"), name.toUpperCase()));
//
//            result = em.createQuery(cq).getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(result.isEmpty()){
//            return null;
//        } else {
//            return result.get(0);
//        }
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM AddOn a WHERE a.name = :name", AddOn.class);
            q.setParameter("name", name);
            return q.uniqueResultOptional();
        }
    }
}
