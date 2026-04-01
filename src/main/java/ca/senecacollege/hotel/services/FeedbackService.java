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
    private IFeedbackRepository _fbRepo;
    private IReservationRepository _resRepo;

    @Inject
    public FeedbackService(IFeedbackRepository fbRepo, IReservationRepository resRepo) {
        this._fbRepo = fbRepo;
        this._resRepo = resRepo;
    }

    @Override
    public void makeFeedback(Reservation reservation, int rating, String comments, Sentiment sentiment){
        Feedback feedback = new Feedback(reservation.getGuest(), reservation, rating, LocalDate.now(), comments, sentiment);
        //_fbRepo.saveFeedback(feedback);
        _fbRepo.save(feedback);
    }

    @Override
    public Reservation findReservation(int resNum) {
        return _resRepo.getRes(resNum);
    }

    public Optional<Reservation> getit(int resNum){
        return _resRepo.findById(resNum);
    }
}
