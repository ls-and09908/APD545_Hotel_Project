package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.repositories.*;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IReservationService {
    private IReservationRepository _resRepo;
    private IRoomRepository _roomRepo;
    private IGuestRepository _guestRepo;
    private IAddonRepository _addonRepo;
    private final IBillingService _billService;
    private final ILoyaltyService _loyaltyService;
    private final int _cancellationDays;

    @Inject
    public ReservationService(IReservationRepository resRepo, IRoomRepository roomRepo, IGuestRepository guestRepo, IAddonRepository addonRepo, IBillingService billService, ILoyaltyService loyaltyService, @Named("cancellationDaysPolicy")int cancellationDays){
        this._resRepo = resRepo;
        this._roomRepo = roomRepo;
        this._guestRepo = guestRepo;
        this._addonRepo = addonRepo;
        _billService = billService;
        _loyaltyService = loyaltyService;
        _cancellationDays = cancellationDays;
    }

    @Override
    public void saveReservation(Reservation reservation) {
        _guestRepo.saveGuest(reservation.getGuest());
        _resRepo.saveRes(reservation);
    }

    @Override
    public Reservation getReservation(int reservationNumber){
        return _resRepo.getRes(reservationNumber).orElse(null);
    }

    /**
     * Queries the repo for rooms available during the reservation's desired dates.
     * <p>Sending in a type of null will return all available rooms</p>
     * @return A list of all rooms of the selected type that are available
     */
    @Override
    public List<Room> findAvailableRoom(LocalDate checkIn, LocalDate checkOut, RoomType type) {
        List<Room> availableRooms = _roomRepo.getAvailableRooms(checkIn, checkOut);
        List<Room> roomList = new ArrayList<>();
        if (availableRooms.isEmpty()){
            return null;
        }
        if (type == null) return availableRooms;
        for (Room room : availableRooms){
            if(room.getType() == type){
                roomList.add(room);
            }
        }
        return roomList.isEmpty() ? null : roomList;
    }

    @Override
    public List<Reservation> getAllReservations(){
        return _resRepo.getAllReservations();
    }

    @Override
    public AddOn getAddOn(String name) {
        return _addonRepo.getAddOn(name).orElse(null);
    }

    @Override
    public List<RoomSet> getRoomSuggestion(int nAdults, int nChildren) {
        List<RoomSet> rooms = new ArrayList<>();
        int totalGuests = nAdults + nChildren;
        int doubleRooms = 0;
        if(totalGuests > 2){
            doubleRooms = totalGuests/4;
            if (totalGuests%4 > 2){
                doubleRooms += 1;
            } else {
                rooms.add(new RoomSet(RoomType.SINGLE, 1));
            }
            rooms.add(new RoomSet(RoomType.DOUBLE, doubleRooms));
        } else {
            rooms.add(new RoomSet(RoomType.SINGLE, 1));
        }
            return rooms;
        }

    @Override
    public boolean checkGuestEmail(String email) {
        return _guestRepo.findGuestEmail(email).isEmpty();
    }

    @Override
    public boolean canCheckOut(Reservation res){
        return res.getBilling().getBalance() == 0.0;
    }

    @Override
    public boolean isCheckOutTime(Reservation res){
        return res.getCheckOut().isEqual(LocalDate.now());
    }

    @Override
    public boolean attemptCheckOut(Reservation res){
        if(canCheckOut(res)){
            res.setStatus(ReservationStatus.CHECKEDOUT);
        } else return false;
        return true;
    }

    /**
     * Attempts to cancel a reservation, freeing up rooms and refunding the customer.
     * <p>Reservations that are already cancelled, checked out, or checked in cannot be cancelled and will return false.</p>
     * @param res
     * @return true if the cancellation was successful
     */
    @Override
    public boolean cancelReservation(Reservation res){
        switch(res.getStatus()){
            case CANCELLED, CHECKEDOUT, CHECKEDIN:
                // TODO: Log invalid cancellation attempt
                return false;
            case BOOKED:
                boolean returnDeposit = LocalDate.now().plusDays(_cancellationDays).isBefore(res.getCheckIn());
                double refund = _billService.refundCancellation(res.getBilling(), returnDeposit);
                if (refund > 0.0){
                    // refunds loyalty points, loyalty service has safety check for non-loyal guests
                    _loyaltyService.removePoints(refund, res.getGuest());
                }
            default:
                res.getRooms().clear();
        }
        res.setStatus(ReservationStatus.CANCELLED);
        _resRepo.saveRes(res);
        return true;
        // TODO: trigger notifying waitlists?
    }

    /**
     * query the DB for any res_num(s) except the current reservation in the room_res table during the specified dates
     * if the returned value is nothing, the room is available
     */
    @Override
    public boolean extendReservation(Reservation res){
        LocalDate checkIn = res.getCheckIn();
        LocalDate checkOut = res.getCheckOut();
        List<Room> roomsList = res.getRooms();
        List<Integer> results;
        for(Room room : roomsList){
            results = _resRepo.checkReserved(room, res.getReservationNumber(), checkIn, checkOut);
            System.out.println("DB Found these: " + results);
            if (!results.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void attemptCheckin(Reservation res){
        res.setStatus(ReservationStatus.CHECKEDIN);
    }

}
