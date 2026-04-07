package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Room;
import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public void saveRes(Reservation r) {
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
