package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.repositories.ReservationRepository;

public class ReservationService implements IReservationService {
    private ReservationRepository _reservationRepository;


    @Override
    public void saveReservation(Reservation reservation) {
        _reservationRepository.saveRes(reservation);
    }

    @Override
    public Reservation getReservation(int reservationNumber){
        return _reservationRepository.getRes(reservationNumber);
    }
}
