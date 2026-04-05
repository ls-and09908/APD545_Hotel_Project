package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.Feedback;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.io.IOException;
import java.util.List;

public class FeedbackConfirmController implements SceneManagerAware {
    private SceneManager sceneManager;
    private ObjectProperty<Feedback> fb = new SimpleObjectProperty<>();
    private List<SVGPath> stars;

    @Override
    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    @FXML
    private void toHome() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/Welcome.fxml", null);
    }

    public void setFeedback(Feedback fb){
        this.fb.set(fb);
    }

    private void setStarRating(int rating){
        for (int i = 0; i < rating; i++){
            stars.get(i).setFill(Color.YELLOW);
        }
    }

    public void initialize(){
        stars = List.of(onestar, twostar, threestar, fourstar, fivestar);

        fb.addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                fbReservation.setText(String.valueOf(fb.get().getReservation().getReservationNumber()));
                fbDate.setText(String.valueOf(fb.get().getDate()));
                fbSentiment.setText(fb.get().getSentiment().name());
                fbComments.setText(fb.get().getComments());
                setStarRating(fb.get().getRating());
            }
        });
    }

    @FXML
    private Label fbReservation;

    @FXML
    private Label fbDate;

    @FXML
    private Label fbSentiment;

    @FXML
    private Label fbComments;

    @FXML
    private SVGPath onestar;

    @FXML
    private SVGPath twostar;

    @FXML
    private SVGPath threestar;

    @FXML
    private SVGPath fourstar;

    @FXML
    private SVGPath fivestar;
}
