package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;

import java.util.List;

public interface IReservationService {
    void saveReservation(Reservation r);
    List<Room> findAvailableRoom(Reservation r, RoomType type);
    void addRoom(Reservation r, Room rm);
}
