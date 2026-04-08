package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Feedback;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Sentiment;
import ca.senecacollege.hotel.repositories.IFeedbackRepository;
import ca.senecacollege.hotel.repositories.IReservationRepository;
import com.google.inject.Inject;

import java.time.LocalDate;
import java.util.Optional;

public class FeedbackService implements IFeedbackService {
    private final IFeedbackRepository _fbRepo;
    private final IReservationRepository _resRepo;
    private final IActivityLogService _logService;

    @Inject
    public FeedbackService(IFeedbackRepository fbRepo, IReservationRepository resRepo, IActivityLogService logService) {
        this._fbRepo = fbRepo;
        this._resRepo = resRepo;
        _logService = logService;
    }

    @Override
    public Feedback makeFeedback(Reservation reservation, int rating, String comments, Sentiment sentiment){
        Feedback feedback = new Feedback(reservation.getGuest(), reservation, rating, LocalDate.now(), comments, sentiment);
        _fbRepo.saveFeedback(feedback);
        _logService.receiveFeedback(feedback);
        return feedback;
    }

    @Override
    public Reservation findReservation(int resNum) {
        return _resRepo.getRes(resNum).orElse(null);
    }
}
