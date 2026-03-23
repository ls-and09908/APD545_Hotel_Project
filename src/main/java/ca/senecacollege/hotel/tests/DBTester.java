package ca.senecacollege.hotel.tests;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.services.RoomFactory;
import com.google.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;

public class DBTester {
    private EntityManagerFactory emf;

    @Inject
    public DBTester(EntityManagerFactory emf){
        this.emf = emf;
    }

    public void makeDB(){
        // DB testing, comment it out from start when you don't have mysql running
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

//        AdminUser test1 = new AdminUser();
//        test1.setUsername("Sttco");
//
//        em.persist(test1);
        LocalDate date = LocalDate.now();
        Guest guest = new Guest("Gab", "1800-HOTNFUN", "help@google.com");
        AddOn a1 = new AddOn("wifi", "..", 15.00, false);
        AddOn a2 = new AddOn("wifi2", "..", 152.00, false);
        AddOn a3 = new AddOn("wifi3", "..", 153.00, true);
        AddOn a4 = new AddOn("wifi4", "..", 154.00, false);

        Room r1 = RoomFactory.createRoom(420, RoomType.PENTHOUSE);
        Room r2 = RoomFactory.createRoom(200, RoomType.SINGLE);

        Reservation res = new Reservation(guest, 3, 2, date, date);
        Billing bill = res.getBilling();
        bill.setBalance(183.77);

        Charge test1 = new Charge(4.0, 1, 123.77, bill, a2);
        Payment tp1 = new Payment(bill, 60.0, guest, date);

        Reservation res2 = new Reservation(guest, 1, 4, date, date);
        Billing bill2 = res2.getBilling();
        bill2.setBalance(340.60);

        Charge test2 = new Charge(0.0, 1, 159.52, bill2, a1);
        Charge test3 = new Charge(0.0, 2, 81.08, bill2, a3);
        Payment tp2 = new Payment(bill2, 100.0, guest, date);

        res.addAddOn(a2);
        res2.addAddOn(a1);
        res2.addAddOn(a3);

        res.addRoom(r2);
        res2.addRoom(r1);

        em.persist(guest);
        em.persist(res);
        em.persist(bill);
        em.persist(test1);
        em.persist(tp1);

        em.persist(res2);
        em.persist(bill2);
        em.persist(test2);
        em.persist(test3);
        em.persist(tp2);

        em.persist(a1);
        em.persist(a2);
        em.persist(a3);
        em.persist(a4);

        em.persist(r1);
        em.persist(r2);

        em.getTransaction().commit();

        em.close();
    }
}
