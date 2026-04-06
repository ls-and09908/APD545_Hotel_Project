package ca.senecacollege.hotel.tests;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.repositories.IAddonRepository;
import ca.senecacollege.hotel.repositories.IRoomRepository;
import ca.senecacollege.hotel.services.BillingService;
import ca.senecacollege.hotel.services.RoomFactory;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;


import java.time.LocalDate;

public class DBTester {
    private EntityManagerFactory emf;
    private BillingService _bs;
    private PricingModel _stdPrice;
    private PricingModel _wkndPrice;
    private IRoomRepository _rmRepo;
    private IAddonRepository _aoRepo;

    @Inject
    public DBTester(EntityManagerFactory emf, BillingService bs, @Named("standard")PricingModel std, @Named("weekend")PricingModel wknd, IRoomRepository rmRepo, IAddonRepository aoRepo){
        this.emf = emf;
        this._bs = bs;
        this._stdPrice = std;
        this._wkndPrice = wknd;
        this._rmRepo = rmRepo;
        this._aoRepo = aoRepo;
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
        Guest guest = new Guest("Gab", "1800-8556021", "help@google.com", "Canada");
        Guest guest2 = new Guest("Lou", "4168971201", "loulou@yahoo.com", "Canada");

        AddOn a1 = new AddOn("wifi", ".....", 15.00, false);
        AddOn a2 = new AddOn("colour", "..", 243.77, false);
        AddOn a3 = new AddOn("magic", "....", 81.08, true);
        AddOn a4 = new AddOn("scott", "( '~')7", 78.44, false);

        Room r1 = RoomFactory.createRoom(420, RoomType.PENTHOUSE);
        Room r2 = RoomFactory.createRoom(200, RoomType.SINGLE);
        Room r3 = RoomFactory.createRoom(216, RoomType.DOUBLE);
        Room r4 = RoomFactory.createRoom(218, RoomType.DOUBLE);
        Room r5 = RoomFactory.createRoom(310, RoomType.DOUBLE);
        Room r6 = RoomFactory.createRoom(206, RoomType.SINGLE);
        Room r7 = RoomFactory.createRoom(208, RoomType.SINGLE);
        Room r8 = RoomFactory.createRoom(410, RoomType.PENTHOUSE);

        Reservation res = new Reservation(guest, 3, 2, date, date.plusDays(2));
        Billing bill = res.getBilling();

        bill.addCharge(new Charge(a2, bill, 1, _stdPrice));
//        bill.addPayment(new Payment(bill, 60.0, guest, date));
        _bs.checkUpdateBillBalance(bill);

        Reservation res2 = new Reservation(guest2, 1, 4, date, date.plusDays(2));
        Billing bill2 = res2.getBilling();

        bill2.addCharge(new Charge(a4, bill2, 1, _stdPrice));
        bill2.addCharge(new Charge(a3, bill2, 2, _stdPrice));
//        bill2.addPayment(new Payment(bill2, 100.0, guest2, date));
        _bs.checkUpdateBillBalance(bill2);

        // Billing service small tests
        Reservation res3 = new Reservation(guest, 3, 2, date.plusDays(6), date.plusDays(12));
        res3.addRoom(r2);
        Billing bill3 = _bs.generateBill(res3);
//        Payment tp3 = new Payment(bill3, 16.23, guest, date);
//        bill3.addPayment(tp3);
        res3.setBilling(bill3);
        _bs.checkUpdateBillBalance(bill3);

        res.addAddOn(a2);
        res2.addAddOn(a1);
        res2.addAddOn(a3);

        res.addRoom(r2);
        res2.addRoom(r1);

        em.persist(guest);
        em.persist(res);
        em.persist(bill);

        em.persist(res2);
        em.persist(bill2);

        em.persist(a1);
        em.persist(a2);
        em.persist(a3);
        em.persist(a4);

        em.persist(r1);
        em.persist(r2);

        em.persist(res3);
        em.persist(bill3);

        em.getTransaction().commit();

        em.close();
    }
}
