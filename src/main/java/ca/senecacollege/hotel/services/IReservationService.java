package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {
    void saveReservation(Reservation r);
    List<Room> findAvailableRoom(LocalDate checkIn, LocalDate checkOut, RoomType type);
    void addRoom(Reservation r, Room rm);
    public Reservation getReservation(int reservationNumber);
}
