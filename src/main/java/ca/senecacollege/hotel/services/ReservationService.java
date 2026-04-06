package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.repositories.*;
import com.google.inject.Inject;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IReservationService {
    private IReservationRepository _resRepo;
    private IRoomRepository _roomRepo;
    private IGuestRepository _guestRepo;
    private IAddonRepository _addonRepo;

    @Inject
    public ReservationService(IReservationRepository resRepo, IRoomRepository roomRepo, IGuestRepository guestRepo, IAddonRepository addonRepo){
        this._resRepo = resRepo;
        this._roomRepo = roomRepo;
        this._guestRepo = guestRepo;
        this._addonRepo = addonRepo;
    }

    @Override
    public void saveReservation(Reservation reservation) {
        //_guestRepo.saveGuest(reservation.getGuest());
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

    /**
     * Checks if the email parameter has already been used for an existing guest.
     * @param email
     * @return true if the email is valid (doesn't already exist) and false otherwise
     */
    @Override
    public boolean checkGuestEmail(String email) {
        return _guestRepo.findGuestEmail(email).isEmpty();
    }
}
