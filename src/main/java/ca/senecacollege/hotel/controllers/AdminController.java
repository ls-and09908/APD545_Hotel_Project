package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.services.AuthService;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.io.IOException;

public class AdminController implements SceneManagerAware {

    @FXML
    Label subtitleLbl;
    @FXML
    Label errLbl;
    @FXML
    TextField usernameInput;
    @FXML
    TextField passwordInput;

    private AuthService _authService;
    SceneManager sceneManager;

    @Inject
    public AdminController(AuthService authService){
        _authService = authService;
    }

    @Override
    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    @FXML
    public void loginPress() throws IOException {
        String user = usernameInput.getText();
        String pass = passwordInput.getText();
        if(_authService.authetnicateLogin(user, pass)) {
            sceneManager.switchScene("/ca/senecacollege/hotel/application/AdminDashboard.fxml", null);
        }
        else {
            errLbl.setText("User not found");
        }
    }

    @FXML
    public void reportsPress() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/Reports.fxml", null);
    }

    @FXML
    public void revenuePress(){
        subtitleLbl.setText("Revenue");
    }
    @FXML
    public void occupancyPress(){
        subtitleLbl.setText("Occupancy");
    }
    @FXML
    public void activityPress(){
        subtitleLbl.setText("Activity");
    }
    @FXML
    public void feedbackPress(){
        subtitleLbl.setText("Feedback");
    }

    @FXML
    private void toHome() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/Welcome.fxml", null);
    }
    @FXML
    private void toDash() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AdminDashboard.fxml", null);
    }

    @FXML
    private void toAddEditBooking() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AddUpdateBooking.fxml", null);
    }
}
