package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;
import ca.senecacollege.hotel.repositories.*;
import com.google.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IReservationService {
    private IReservationRepository _resRepo;
    private IRoomRepository _roomRepo;
    private IGuestRepository _guestRepo;

    @Inject
    public ReservationService(IReservationRepository resRepo, IRoomRepository roomRepo, IGuestRepository guestRepo){
        this._resRepo = resRepo;
        this._roomRepo = roomRepo;
        this._guestRepo = guestRepo;
    }

    @Override
    public void saveReservation(Reservation reservation) {
        _guestRepo.saveGuest(reservation.getGuest());
        _resRepo.saveRes(reservation);
    }

    @Override
    public Reservation getReservation(int reservationNumber){
        return _resRepo.getRes(reservationNumber);
    }

    // Queries the repo for rooms available during the reservation's desired dates
    @Override
    public List<Room> findAvailableRoom(LocalDate checkIn, LocalDate checkOut, RoomType type) {
        List<Room> availableRooms = _roomRepo.getAvailableRooms(checkIn, checkOut);
        List<Room> roomList = new ArrayList<>();
        if (availableRooms.isEmpty()){
            return null;
        }
        for (Room room : availableRooms){
            if(room.getType() == type){
                roomList.add(room);
            }
        }
        return roomList.isEmpty() ? null : roomList;
    }

    @Override
    public void addRoom(Reservation r, Room rm) {
        r.addRoom(rm);
    }
}
