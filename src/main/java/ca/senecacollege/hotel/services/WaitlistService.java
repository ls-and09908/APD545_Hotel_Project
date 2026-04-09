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
    private IReservationService resService;
    @Inject
    public WaitlistService(IWaitlistRepository waitRepo, IReservationService resService){
        this.waitRepo = waitRepo;
        this.resService = resService;
    }

    @Override
    public void saveWaitlistRes(Waitlist w) {
//        w.setRooms(resService.getRoomSuggestion(w.getAdults(), w.getChildren())); //This should work, however I don't know if we should set these rooms to waitlisted here
        waitRepo.saveWaitlist(w, w.getGuest().getId(), w.getRooms());
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

    @Override
    public void alert(List<RoomSet> freedRooms){
        List<Waitlist> allWaitlists = getAllWaitlist();
        for(Waitlist w: allWaitlists){
            if(w.getRooms().equals(freedRooms)){
                w.alert();
            }
        }
    }
}
