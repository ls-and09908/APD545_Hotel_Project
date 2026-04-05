package ca.senecacollege.hotel.utilities;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.services.BillingService;
import ca.senecacollege.hotel.services.RoomFactory;
import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBInitializer {
    private final SessionFactory sessionFactory;
    private BillingService _bs;


    @Inject
    public DBInitializer(SessionFactory sessionFactory,  BillingService bs){
        this.sessionFactory = sessionFactory;
        this._bs = bs;
    }

    public void makeDB(){
        Transaction tx=null;
        try(Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();

            LocalDate date = LocalDate.now();
            List<Guest> guests = List.of(
                    new Guest("Gab", "1800-8556021", "help@google.com", "Canada"),
                    new Guest("Lou", "4168971201", "loulou@yahoo.com", "Canada"),
                    new Guest("Bog Troll", "909823195", "bogtroll@hotmail.ca", "China", 2),
                    new Guest("Helga", "708175023", "helga@sheshelga.com", "Vietnam"),
                    new Guest("Lando", "412341558", "landoes@gmail.com", "Canada", 1),
                    new Guest("Not a Bee", "18008338122", "buzzbuzz@bees.biz", "United States of America")
            );

            List<AddOn> addons = List.of(
                    new AddOn("WIFI", ".....", 25.00, false),
                    new AddOn("BREAKFAST", "..", 50.00, false),
                    new AddOn("PARKING", "....", 5.00, true),
                    new AddOn("SPA", ".....", 80.00, false));

            List<Room> rooms = new ArrayList<>();
            int numRooms = 40;
            int floor = 0;
            for (int i = 0; i < numRooms; i++) {
                switch (i % 10) {
                    case 9:
                        rooms.add(RoomFactory.createRoom(floor + (i % 10 + 1) * 2, RoomType.DELUXE));
                        break;
                    case 7, 8:
                        rooms.add(RoomFactory.createRoom(floor + (i % 10 + 1) * 2, RoomType.DOUBLE));
                        break;
                    case 0:
                        floor += 100;
                    default:
                        rooms.add(RoomFactory.createRoom(floor + (i % 10 + 1) * 2, RoomType.SINGLE));
                        break;
                }
            }
            floor = 500;
            for (int i = 0; i < 4; i++) {
                rooms.add(RoomFactory.createRoom(floor + (i + 1) * 2, RoomType.PENTHOUSE));
            }

            Reservation res = new Reservation(guests.get(0), 3, 2, date, date.plusDays(2));
            res.addAddOn(addons.get(1));
            res.addAddOn(addons.get(3));
            res.addRoom(rooms.get(6));
            res.addRoom(rooms.get(5));
            res.addRoom(rooms.get(7));
            Billing bill = _bs.generateBill(res);
            bill.addPayment(new Payment(bill, 360.0, guests.get(0), date));
            _bs.checkUpdateBillBalance(bill);

            Reservation res2 = new Reservation(guests.get(1), 1, 4, date.minusDays(2), date.plusDays(5));
            res2.addAddOn(addons.get(3));
            res2.addAddOn(addons.get(2));
            res2.addAddOn(addons.get(0));
            res2.addRoom(rooms.get(18));
            Billing bill2 = _bs.generateBill(res);
            bill2.addPayment(new Payment(bill2, 100.60, guests.get(1), date));
            _bs.checkUpdateBillBalance(bill2);

            Reservation res3 = new Reservation(guests.get(0), 3, 2, date.plusDays(6), date.plusDays(17));
            res3.addRoom(rooms.get(43));
            Billing bill3 = _bs.generateBill(res3);
            bill3.addPayment(new Payment(bill3, 16.23, guests.get(0), date));
            bill3.addPayment(new Payment(bill3, 500.00, guests.get(0), date.minusDays(16)));
            _bs.checkUpdateBillBalance(bill3);

            Reservation res4 = new Reservation(guests.get(4), 1, 1, date.minusDays(8), date.minusDays(1));
            res4.addRoom(rooms.get(8));
            Billing bill4 = _bs.generateBill(res4);
            bill4.addPayment(new Payment(bill4, 1400.00, guests.get(4), date.minusDays(2)));
            res4.setStatus(ReservationStatus.CHECKEDOUT);
            _bs.checkUpdateBillBalance(bill4);

            for (Guest g : guests) {
                session.persist(g);
            }

            // persist rooms
            for (Room r : rooms) {
                session.persist(r);
            }
            // persist addons
            for (AddOn a : addons) {
                session.persist(a);
            }

            session.merge(res);
            session.merge(res2);
            session.merge(res3);
            session.merge(res4);

        //Scott's AdminUser table below. Please let me know if I did it incorrect:
            String salt = BCrypt.gensalt(12);
            String testAdminPw = BCrypt.hashpw("admin", salt);
            String testManagerPw = BCrypt.hashpw("manager", salt);

            List<AdminUser> adminUsers = List.of(
                new AdminUser("admin", testAdminPw, Role.ADMINISTRATOR),
                new AdminUser("manager", testManagerPw, Role.MANAGER)
            );

            for(AdminUser a: adminUsers){
                session.persist(a);
            }

            res2.setStatus(ReservationStatus.CHECKEDOUT);
            Feedback fb = new Feedback(guests.get(1), res2, 4, LocalDate.now(), "Yummers", Sentiment.CLEAN);
            session.merge(fb);
        //Scott section ends here

            tx.commit();
        }catch(RuntimeException e){
            if(tx!=null) tx.rollback();
            throw e;
        }
    }
}
