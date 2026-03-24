package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;
import ca.senecacollege.hotel.repositories.ReservationRepository;

import java.util.List;

public class ReservationService implements IReservationService {
    private ReservationRepository _resRepo;

    @Override
    public void saveReservation(Reservation reservation) {
//        _reservationRepository.add(reservation);
    }

    @Override
    public List<Room> findAvailableRoom(Reservation r, RoomType type) {
        return List.of();
    }

    @Override
    public void addRoom(Reservation r, Room rm) {

    }
}
