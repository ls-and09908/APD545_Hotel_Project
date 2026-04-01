package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Sentiment;

import java.util.Optional;

public interface IFeedbackService {
    void makeFeedback(Reservation reservation, int rating, String comments, Sentiment sentiment);
    Reservation findReservation(int resNum);
    Optional<Reservation> getit(int resNum);
}
