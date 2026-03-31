package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.Waitlist;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class AdminUserRepository implements IAdminUserRepository {
    private EntityManagerFactory emf;

    @Inject
    public AdminUserRepository(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public List<AdminUser> getAllAdminUsers() {
        List<AdminUser> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<AdminUser> cq = cb.createQuery(AdminUser.class);
            Root<AdminUser> adminUser = cq.from(AdminUser.class);
            cq.select(adminUser);

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            return results;
        }
    }

    @Override
    public void saveAdminUser(AdminUser r) {
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
    public AdminUser getAdminUser(String username) {
        AdminUser result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<AdminUser> cq = cb.createQuery(AdminUser.class);
            Root<AdminUser> adminUser = cq.from(AdminUser.class);
            cq.select(adminUser).where(cb.equal(adminUser.get("username"), username));

            result = em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }
}
