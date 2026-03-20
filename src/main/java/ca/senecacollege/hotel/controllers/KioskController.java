package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

//TODO finish linking navigation between pages
//TODO implement proper repositories
public class KioskController implements SceneManagerAware {
    SceneManager sceneManager;

    //Currently unsure of what repositories this will be requiring
    @Inject
    public KioskController(){

    }

    //Set's the SceneManager object (Called on instantiation)
    @Override
    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }


    //<editor-fold desc="pageNavigationControls">
    @FXML
    private void onToDatesPress() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskDateSelect.fxml", null);
    }

    @FXML
    private void onToRoomPress() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskRoomSelect.fxml", null);
    }

    @FXML
    private void backToCountPress() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskGuestCount.fxml", null);
    }
    //</editor-fold>

}
