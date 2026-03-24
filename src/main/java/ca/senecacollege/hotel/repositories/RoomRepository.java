package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.util.List;

public class RoomRepository implements IRoomRepository {
    private EntityManagerFactory emf;

    @Inject
    public RoomRepository(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Room> cq = cb.createQuery(Room.class);
            Root<Room> room = cq.from(Room.class);
            cq.select(room);

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return results;
    }

    @Override
    public void saveRoom(Room r) {
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
    public Room getRoom(int roomNum) {
        Room result = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Room> cq = cb.createQuery(Room.class);
            Root<Room> room = cq.from(Room.class);
            cq.select(room).where(cb.equal(room.get("ROOM_NUM"), roomNum));

            result = em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public List<Room> getRoomsByType(RoomType type) {
        List<Room> results = null;
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Room> cq = cb.createQuery(Room.class);
            Root<Room> room = cq.from(Room.class);
            cq.select(room).where(cb.equal(room.get("ROOM_TYPE"), type));

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return results;
    }

    @Override
    public List<Room> getAvailableRooms(){
        List<Room> results = null;

        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Room> cq = cb.createQuery(Room.class);
            Root<Room> room = cq.from(Room.class);

            Subquery<Room> sq = cq.subquery(Room.class);
            Root<Reservation> reservation = sq.from(Reservation.class);
            Join<Reservation,Room> resRoom = reservation.join("rooms");

            // get rooms that have no reservation AND rooms that don't have any future reservations
            sq.select(resRoom).where(cb.and(
                    cb.equal(resRoom, room),
                    cb.greaterThanOrEqualTo(reservation.get("checkOut"), cb.currentDate())
            ));
            cq.select(room).where(cb.not(cb.exists(sq)));

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return results;
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<Room> results = null;

        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Room> cq = cb.createQuery(Room.class);
            Root<Room> room = cq.from(Room.class);

            Subquery<Room> sq = cq.subquery(Room.class);
            Root<Reservation> reservation = sq.from(Reservation.class);
            Join<Reservation,Room> resRoom = reservation.join("rooms");

            // get rooms whose current reservations don't overlap with requested checkin/checkout
            sq.select(resRoom).where(cb.and(
                    cb.equal(resRoom, room),
                    cb.lessThanOrEqualTo(reservation.get("checkIn"), checkOut),
                    cb.greaterThanOrEqualTo(reservation.get("checkOut"), checkIn)
            ));
            cq.select(room).where(cb.not(cb.exists(sq)));

            results = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return results;
    }
}
