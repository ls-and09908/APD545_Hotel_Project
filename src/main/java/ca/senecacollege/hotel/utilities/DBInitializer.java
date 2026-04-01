package ca.senecacollege.hotel.utilities;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.repositories.IAddonRepository;
import ca.senecacollege.hotel.repositories.IAdminUserRepository;
import ca.senecacollege.hotel.repositories.IRoomRepository;
import ca.senecacollege.hotel.services.BillingService;
import ca.senecacollege.hotel.services.RoomFactory;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBInitializer {
    private EntityManagerFactory emf;
    private BillingService _bs;
    private PricingModel _stdPrice;
    private PricingModel _wkndPrice;
    private IRoomRepository _rmRepo;
    private IAddonRepository _aoRepo;
    private IAdminUserRepository _adRepo;

    @Inject
    public DBInitializer(EntityManagerFactory emf, BillingService bs, @Named("standard")PricingModel std, @Named("weekend")PricingModel wknd, IRoomRepository rmRepo, IAddonRepository aoRepo, IAdminUserRepository adRepo){
        this.emf = emf;
        this._bs = bs;
        this._stdPrice = std;
        this._wkndPrice = wknd;
        this._rmRepo = rmRepo;
        this._aoRepo = aoRepo;
        this._adRepo = adRepo;
    }

    public void makeDB(){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

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
                if (i % 10 == 0) {
                    floor += 100;
                }
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

            // persist guests
            for (Guest g : guests) {
                em.persist(g);
            }

            // persist rooms
            for (Room r : rooms) {
                em.persist(r);
            }
            // persist addons
            for (AddOn a : addons) {
                em.persist(a);
            }

            em.merge(res);
            em.merge(res2);
            em.merge(res3);
    //Scott's AdminUser table below. Please let me know if I did it incorrect:
            String salt = BCrypt.gensalt(12);
            String testAdminPw = BCrypt.hashpw("admin", salt);
            String testManagerPw = BCrypt.hashpw("manager", salt);
            Role manager = new Role("manager", "30");
            Role admin = new Role("admin", "15");

            List<AdminUser> adminUsers = List.of(
                new AdminUser("admin", testAdminPw, admin),
                new AdminUser("manager", testManagerPw, manager)
            );

            for(AdminUser a: adminUsers){
                em.persist(a);
            }
        //Scott section ends here
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }

    }
}
