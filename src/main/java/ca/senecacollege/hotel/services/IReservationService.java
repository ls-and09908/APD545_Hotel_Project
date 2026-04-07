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
    public List<Reservation> getAllReservations();

    /**
     * Checks if the email parameter has already been used for an existing guest.
     * @param email
     * @return true if the email is valid (doesn't already exist) and false otherwise
     */
    boolean checkGuestEmail(String email);
    boolean canCheckOut(Reservation r);

    boolean isCheckOutTime(Reservation res);

    boolean attemptCheckOut(Reservation res);

    boolean cancelReservation(Reservation r);

    /**
     * Checks if it is possible to extend the reservation without changing rooms
     */
    boolean extendReservation(Reservation res);

    void attemptCheckin(Reservation res);
}
