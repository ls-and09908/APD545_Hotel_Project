package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.*;
import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.*;

public class ReservationRepository implements IReservationRepository {
    private final SessionFactory sessionFactory;

    @Inject
    ReservationRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Reservation> getAllReservations() {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            var q = session.createQuery("FROM Reservation", Reservation.class);
            tx.commit();
            return q.list();
        }
    }

    @Override
    public void saveRes(Reservation r, Integer guestID, List<Integer> rooms, List<Integer> addons, Integer billNum) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Reservation res;
            if (r.getReservationNumber() == null) {
                res = r;
                if (guestID != null) res.setGuest(session.find(Guest.class, guestID));
                else session.persist(res.getGuest());
                if (res.getBilling() != null) session.persist(res.getBilling());

                res.getRooms().clear();
                res.getAddOns().clear();
                for (Integer id : rooms) {
                    res.getRooms().add(session.find(Room.class, id));
                }
                for (Integer id : addons) {
                    res.getAddOns().add(session.find(AddOn.class, id));
                }
                session.persist(res);
            } else {
                res = session.find(Reservation.class, r.getReservationNumber());
                if (guestID != null) res.setGuest(session.find(Guest.class, guestID));
                updateResFromDB(r, res);

                if (r.getBilling() != null) {
                    Billing bill = session.merge(r.getBilling());
                    res.setBilling(bill);
                    bill.setReservation(res);
                } else res.setBilling(null);

                res.getRooms().clear();
                session.flush();
                for (Integer id : rooms) {
                    res.getRooms().add(session.find(Room.class, id));
                }

                res.getAddOns().clear();
                session.flush();
                for (Integer id : addons) {
                    res.getAddOns().add(session.find(AddOn.class, id));
                }
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Reservation> getRes(int resNum) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Reservation.class, resNum));
        }
    }

    @Override
    public List<Integer> checkReserved(Room r, int resNum, LocalDate checkIn, LocalDate checkOut) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            var q = session.createQuery("""
                SELECT res.reservationNumber
                FROM Reservation res
                JOIN res.rooms rm
                WHERE rm.roomNumber = :roomNum
                AND res.reservationNumber != :resNum
                AND res.checkIn < :checkOut
                AND res.checkIn > :checkIn
                """, Integer.class);
            q.setParameter("resNum", resNum);
            q.setParameter("checkIn", checkIn);
            q.setParameter("checkOut", checkOut);
            q.setParameter("roomNum", r.getRoomNumber());
            tx.commit();
            return q.list();
        }
    }

    @Override
    public void updateResFromDB(Reservation src, Reservation dest) {
        dest.setCheckIn(src.getCheckIn());
        dest.setCheckOut(src.getCheckOut());
        dest.setAdults(src.getAdults());
        dest.setChildren(src.getChildren());
        dest.setStatus(src.getStatus());
    }
}
