package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RoomRepository implements IRoomRepository {
    private final SessionFactory sessionFactory;

    @Inject
    public RoomRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Room> getAllRooms() {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Room", Room.class);
            return q.list();
        }
    }

    @Override
    public void saveRoom(Room r) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            session.merge(r);
            tx.commit();
        }catch(RuntimeException e){
            if(tx!=null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Room> getRoom(int roomNum) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Room.class, roomNum));
        }
    }

    @Override
    public List<Room> getRoomsByType(RoomType type) {
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Room r WHERE r.roomType = :type", Room.class);
            q.setParameter("roomType", type);
            return q.list();
        }
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        try(Session session = sessionFactory.openSession()){
            var q = session.createQuery("""
                FROM Room rm
                WHERE NOT EXISTS (
                    SELECT 1 FROM Reservation res
                    JOIN res.rooms rr
                    WHERE rr = rm
                    AND res.checkIn < :checkOut
                    AND res.checkOut > :checkIn
                )""", Room.class);
            q.setParameter("checkOut", checkOut);
            q.setParameter("checkIn", checkIn);
            return q.list();
        }
    }
}
