package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.*;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {
    void saveReservation(Reservation r);
    List<Room> findAvailableRoom(LocalDate checkIn, LocalDate checkOut, RoomType type);
    Reservation getReservation(int reservationNumber);
    AddOn getAddOn(String name);
    List<RoomSet> getRoomSuggestion(int nAdults, int nChildren);
}
