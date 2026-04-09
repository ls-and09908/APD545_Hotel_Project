package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.Feedback;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.ReservationStatus;
import ca.senecacollege.hotel.models.Sentiment;
import ca.senecacollege.hotel.services.IFeedbackService;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.io.IOException;
import java.util.List;

public class FeedbackController implements SceneManagerAware {
    private IFeedbackService _fbService;
    private SceneManager sceneManager;
    private List<SVGPath> stars;
    private IntegerProperty starRating = new SimpleIntegerProperty();
    private Reservation reservation;

    @Inject
    public FeedbackController(IFeedbackService fbService){
        this._fbService = fbService;
    }

    @Override
    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    @FXML
    private void toHome() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/Welcome.fxml", null);
    }

    public void initialize(){
        submitBtn.disableProperty().bind(starRating.isEqualTo(0)
                .or(resNumField.textProperty().isEmpty())
                .or(resNumError.visibleProperty())
                .or(sentimentBox.getSelectionModel().selectedItemProperty().isNull())
                .or(commentsText.textProperty().length().greaterThan(2000)));

        ObservableList<Sentiment> sentiments = FXCollections.observableArrayList(Sentiment.values());
        sentimentBox.setItems(sentiments);

        stars = List.of(onestar, twostar, threestar, fourstar, fivestar);

        onestar.setOnMouseClicked(event -> {
            setStarRating(1);
        });

        twostar.setOnMouseClicked(event -> {
            setStarRating(2);
        });

        threestar.setOnMouseClicked(event -> {
            setStarRating(3);
        });

        fourstar.setOnMouseClicked(event -> {
            setStarRating(4);
        });

        fivestar.setOnMouseClicked(event -> {
            setStarRating(5);
        });

        resNumField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("^[0-9]{0,12}$")){
                resNumError.setText("Reservation number should contain only numbers.");
                resNumError.setVisible(true);
            } else {
                resNumError.setVisible(false);
            }
        });

        resNumField.focusedProperty().addListener((observable, oldValue, newValue) ->{
            if(!newValue && !resNumError.isVisible()){
                try {
                    int resNum = Integer.parseInt(resNumField.getText());
                    reservation = _fbService.findReservation(resNum);
                    if (reservation == null) {
                        resNumError.setText("Reservation not found.");
                        resNumError.setVisible(true);
                    } else if (reservation.getStatus() != ReservationStatus.CHECKEDOUT) {
                        resNumError.setText("Reservation has not checked out.");
                        resNumError.setVisible(true);
                    }
                } catch (NumberFormatException e){
                    resNumError.setText("Reservation number is required.");
                    resNumError.setVisible(true);
                }
            }
        });

        commentsText.textProperty().addListener((observable, oldValue, newValue) -> {
            int length = newValue.length();
            commentCharCount.setText("Characters: " + length + "/2000");
            if(length > 2000){
                commentCharCount.setStyle("-fx-text-fill: red;");
            } else {
                commentCharCount.setStyle("-fx-text-fill: black;");
            }
        });
    }

    private void clearStars(){
        for(SVGPath star : stars){
            clearStar(star);
        }
    }

    private void clearStar(SVGPath star){
        star.setFill(Color.TRANSPARENT);
        star.getStyleClass().clear();
    }

    private void setStarRating(int rating){
        clearStars();
        for (int i = 0; i < rating; i++){
            stars.get(i).setFill(Color.YELLOW);
        }
        starRating.setValue(rating);
    }

    @FXML
    private void submitFeedback() throws IOException {
        Feedback fb = _fbService.makeFeedback(reservation, starRating.get(), commentsText.getText(), sentimentBox.getSelectionModel().getSelectedItem());
        sceneManager.switchScene("/ca/senecacollege/hotel/application/FeedbackConfirm.fxml", (FeedbackConfirmController controller) -> {
            controller.setFeedback(fb);
        });
    }

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

    @FXML
    private Button submitBtn;

    @FXML
    private TextArea commentsText;

    @FXML
    private ChoiceBox<Sentiment> sentimentBox;

    @FXML
    private TextField resNumField;

    @FXML
    private Label resNumError;

    @FXML
    private Label commentCharCount;
}
