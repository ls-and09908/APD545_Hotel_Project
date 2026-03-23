package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Reservation;

import java.util.List;

public interface IReservationRepository {
    List<Reservation> getAllReservations();
    void saveRes(Reservation r);
    Reservation getRes(int resNum);
}
