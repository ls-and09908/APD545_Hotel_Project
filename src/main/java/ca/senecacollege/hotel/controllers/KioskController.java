package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class KioskController implements SceneManagerAware {
    @FXML
    Button toDatesBtn;

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

    @FXML
    private void onToDatesPress() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskDateSelect.fxml", null);
    }
}
