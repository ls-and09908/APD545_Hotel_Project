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
        try(Session session = sessionFactory.openSession()) {
            var q = session.createQuery("FROM Reservation", Reservation.class);
            return q.list();
        }
    }

    @Override
    public void saveRes(Reservation r, Integer guestID, List<Integer> rooms, List<Integer> addons, Integer billNum) {
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            if(guestID != null){
                Guest g = session.find(Guest.class, guestID);
                r.setGuest(g);
            } else {
                session.persist(r.getGuest());
            }
            if(billNum != null){
                Billing b = session.find(Billing.class, billNum);
                r.setBilling(b);
            }
            r.getRooms().clear();
            for(Integer id : rooms){
                r.addRoom(session.find(Room.class, id));
            }
            r.getAddOns().clear();
            for(Integer id : addons){
                r.addAddOn(session.find(AddOn.class, id));
            }
            if(r.getReservationNumber() != null){
                session.merge(r);
            } else session.persist(r);
            tx.commit();
        }catch(RuntimeException e){
            if(tx!=null) tx.rollback();
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
        try(Session session = sessionFactory.openSession()){
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
            return q.list();
        }
    }
}
