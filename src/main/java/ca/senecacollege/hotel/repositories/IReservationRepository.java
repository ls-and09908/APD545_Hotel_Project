package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Reservation;

import java.util.List;
import java.util.Optional;

public interface IReservationRepository {
    List<Reservation> getAllReservations();
    void saveRes(Reservation r);
    Optional<Reservation> getRes(int resNum);
}
