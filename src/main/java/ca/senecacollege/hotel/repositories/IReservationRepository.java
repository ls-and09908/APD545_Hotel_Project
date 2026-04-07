package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IReservationRepository {
    List<Reservation> getAllReservations();
    void saveRes(Reservation r, Integer guestID, List<Integer> rooms, List<Integer> addons, Integer billNum);

    Optional<Reservation> getRes(int resNum);

    List<Integer> checkReserved(Room r, int resNum, LocalDate checkIn, LocalDate checkOut);
}
