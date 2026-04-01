package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Feedback;

import java.util.List;

public interface IFeedbackRepository {
    List<Feedback> getAllFeedbacks();
    void saveFeedback(Feedback fb);
    Feedback getFeedback(int feedbackId);
    void save(Feedback fb);
}
