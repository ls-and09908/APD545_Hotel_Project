package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.RoomSet;
import ca.senecacollege.hotel.models.Waitlist;
import ca.senecacollege.hotel.repositories.IWaitlistRepository;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class WaitlistService implements IWaitlistService {

    private IWaitlistRepository waitRepo;
    @Inject
    public WaitlistService(IWaitlistRepository waitRepo){
        this.waitRepo = waitRepo;
    }

    @Override
    public void saveWaitlistRes(Waitlist w) {
        waitRepo.saveWaitlist(w);
    }

    @Override
    public void removeWaitlist(Waitlist w){
        waitRepo.removeWaitlist(w);
    }

    @Override
    public Waitlist getWaitlist(int waitlistNum) {
        return null;
    }

    @Override
    public ObservableList<Waitlist> getAllWaitlist(){
        return FXCollections.observableArrayList(waitRepo.getAllWaitlists());
    }
}
