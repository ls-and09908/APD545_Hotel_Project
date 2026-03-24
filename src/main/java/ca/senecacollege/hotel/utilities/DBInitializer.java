package ca.senecacollege.hotel.utilities;

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

public class DBInitializer {
    private EntityManagerFactory emf;
    private BillingService _bs;
    private PricingModel _stdPrice;
    private PricingModel _wkndPrice;
    private IRoomRepository _rmRepo;
    private IAddonRepository _aoRepo;

    @Inject
    public DBInitializer(EntityManagerFactory emf, BillingService bs, @Named("standard")PricingModel std, @Named("weekend")PricingModel wknd, IRoomRepository rmRepo, IAddonRepository aoRepo){
        this.emf = emf;
        this._bs = bs;
        this._stdPrice = std;
        this._wkndPrice = wknd;
        this._rmRepo = rmRepo;
        this._aoRepo = aoRepo;
    }

    public void makeDB(){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        LocalDate date = LocalDate.now();
        Guest guest = new Guest("Gab", "1800-8556021", "help@google.com");
        Guest guest2 = new Guest("Lou", "4168971201", "loulou@yahoo.com");

        AddOn a1 = new AddOn("wifi", ".....", 25.00, false);
        AddOn a2 = new AddOn("breakfast", "..", 50.00, false);
        AddOn a3 = new AddOn("parking", "....", 5.00, true);
        AddOn a4 = new AddOn("spa", ".....", 80.00, false);

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
        bill.addPayment(new Payment(bill, 60.0, guest, date));
        res.addAddOn(a2);
        res.addRoom(r2);
        _bs.checkUpdateBillBalance(bill);

        Reservation res2 = new Reservation(guest2, 1, 4, date.minusDays(2), date.plusDays(5));
        Billing bill2 = res2.getBilling();
        bill2.addCharge(new Charge(a4, bill2, 1, _stdPrice));
        bill2.addCharge(new Charge(a3, bill2, 2, _stdPrice));
        bill2.addPayment(new Payment(bill2, 100.0, guest2, date));
        res2.addAddOn(a1);
        res2.addAddOn(a3);
        res2.addRoom(r1);
        _bs.checkUpdateBillBalance(bill2);

        Reservation res3 = new Reservation(guest, 3, 2, date.plusDays(6), date.plusDays(12));
        res3.addRoom(r2);
        Billing bill3 = _bs.generateBill(res3);
        Payment tp3 = new Payment(bill3, 16.23, guest, date);
        bill3.addPayment(tp3);
        res3.setBilling(bill3);
        _bs.checkUpdateBillBalance(bill3);

        em.persist(guest);
        em.persist(guest2);
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
        em.persist(r3);
        em.persist(r4);
        em.persist(r5);
        em.persist(r6);
        em.persist(r7);
        em.persist(r8);

        em.persist(res3);
        em.persist(bill3);

        em.getTransaction().commit();

        em.close();

    }
}
