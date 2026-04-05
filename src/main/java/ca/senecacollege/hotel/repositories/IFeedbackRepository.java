package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Feedback;

import java.util.List;
import java.util.Optional;

public interface IFeedbackRepository {
    List<Feedback> getAllFeedbacks();
    void saveFeedback(Feedback fb);
    Optional<Feedback> getFeedback(int feedbackId);
}
