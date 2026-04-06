package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.services.AuthService;
import ca.senecacollege.hotel.services.IAuthService;
import ca.senecacollege.hotel.services.IReservationService;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Optional;

public class AdminController implements SceneManagerAware {
    private final IAuthService _authService;
    private final IReservationService _resService;
    private AdminUser adminuser;
    SceneManager sceneManager;


    @FXML
    Label errLbl;
    @FXML
    TextField usernameInput;
    @FXML
    PasswordField passwordInput;


    @Inject
    public AdminController(IAuthService authService, IReservationService resService){
        _authService = authService;
        _resService = resService;
    }

    @Override
    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    @FXML
    public void loginPress() throws IOException {
        String user = usernameInput.getText();
        String pass = passwordInput.getText();
        this.adminuser = _authService.authenticateLogin(user, pass);
        if(this.adminuser != null) {
            System.out.println(adminuser.getUsername() + " loginPress");
            sceneManager.switchScene("/ca/senecacollege/hotel/application/AdminDashboard.fxml", (AdminController controller) -> controller.setAdminuser(this.adminuser));
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
    private void toHome() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/Welcome.fxml", null);
    }
    @FXML
    private void toDash() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AdminDashboard.fxml", (AdminController controller) -> controller.setAdminuser(this.adminuser));
    }

    @FXML
    private void toAddEditBooking() throws IOException{
        System.out.println(adminuser.getUsername() + "click on add booking");

        Reservation res = null;
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AddUpdateBooking.fxml", (AdminBookingController controller) -> {
            controller.setRes(res);
            if (res != null) controller.setGuest(res.getGuest());
            else controller.setGuest(null);
            controller.setUser(this.adminuser);
        });
    }

    public void setAdminuser(AdminUser adminuser) {
        this.adminuser = adminuser;
    }
}
