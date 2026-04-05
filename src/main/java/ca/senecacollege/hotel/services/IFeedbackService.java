package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Feedback;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Sentiment;

import java.util.Optional;

public interface IFeedbackService {
    Feedback makeFeedback(Reservation reservation, int rating, String comments, Sentiment sentiment);
    Reservation findReservation(int resNum);
}
