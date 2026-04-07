package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;
import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Locale;
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
    public Optional<AddOn> getAddOn(int addOnID) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(AddOn.class, addOnID));
        }
    }

    @Override
    public Optional<AddOn> getAddOn(String name) {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM AddOn a WHERE a.name = :name", AddOn.class);
            q.setParameter("name", name.toUpperCase());
            return q.uniqueResultOptional();
        }
    }
}
