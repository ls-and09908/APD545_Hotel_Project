package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import java.io.IOException;

public class FeedbackController implements SceneManagerAware {
    SceneManager sceneManager;

    @Inject
    public FeedbackController(){}

    @Override
    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    @FXML
    private void toHome() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/Welcome.fxml", null);
    }
}
