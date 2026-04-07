package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.ReservationStatus;
import ca.senecacollege.hotel.services.AuthService;
import ca.senecacollege.hotel.services.IAuthService;
import ca.senecacollege.hotel.services.IReservationService;
import ca.senecacollege.hotel.services.IWaitlistService;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class AdminController implements SceneManagerAware {
    private final IAuthService _authService;
    private final IReservationService _resService;
    private final IWaitlistService _waitService;
    private AdminUser adminuser;
    SceneManager sceneManager;

    @FXML
    Label errLbl;
    @FXML
    TextField usernameInput;
    @FXML
    PasswordField passwordInput;
    @FXML
    TableView adminTable;
    @FXML
    TableColumn<Reservation, String> nameCol;
    @FXML
    TableColumn<Reservation, String> phoneCol;
    @FXML
    TableColumn<Reservation, String> emailCol;
    @FXML
    TableColumn<Reservation, String> inCol;
    @FXML
    TableColumn<Reservation, String> outCol;
    @FXML
    TableColumn<Reservation, String> statusCol;
    @FXML
    TextField nameSearch;
    @FXML
    TextField phoneSearch;
    @FXML
    TextField statusSearch;
    @FXML
    TextField emailSearch;
    @FXML
    DatePicker fromSearch;
    @FXML
    DatePicker toSearch;

    @Inject
    public AdminController(IAuthService authService, IReservationService resService, IWaitlistService waitService){
        _authService = authService;
        _resService = resService;
        _waitService = waitService;
    }

    @Override
    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    public void initialize(){
        if(nameCol != null){
            setTableData(null);
            phoneSearch.textProperty().addListener((observable, oldValue, newValue) ->{
                if(!newValue.matches("\\d*")){
                    phoneSearch.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }
    }

    private void setTableData(ObservableList<Reservation> resList){
        ObservableList<Reservation> allRes;
        if(resList == null) {
            allRes = FXCollections.observableArrayList(_resService.getAllReservations());
        }
        else{
            allRes = resList;
        }
        adminTable.setItems(allRes);
        nameCol.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getGuest().getName()));
        phoneCol.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getGuest().getPhone()));
        emailCol.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getGuest().getEmail()));
        statusCol.setCellValueFactory(r -> new SimpleObjectProperty<>(String.valueOf(r.getValue().getStatus())));
        inCol.setCellValueFactory(r -> new SimpleObjectProperty<>(String.valueOf(r.getValue().getCheckIn())));
        outCol.setCellValueFactory(r -> new SimpleObjectProperty<>(String.valueOf(r.getValue().getCheckOut())));

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
    private void handleSearch(){
        ObservableList<Reservation> observableMasterData = FXCollections.observableArrayList(_resService.getAllReservations());
        FilteredList<Reservation> filteredRes = getReservations(observableMasterData);
        setTableData(filteredRes);
    }

    private FilteredList<Reservation> getReservations(ObservableList<Reservation> observableMasterData) {
        String searchedName = nameSearch.getText();
        String searchedPhone = phoneSearch.getText();
        String searchedEmail = emailSearch.getText();
        String searchedStatus = statusSearch.getText();
        LocalDate searchedIn = fromSearch.getValue();
        LocalDate searchedOut = toSearch.getValue();
        FilteredList<Reservation> filteredRes = new FilteredList<>(observableMasterData, r->{
            return r.getGuest().getName().equalsIgnoreCase(searchedName)
                    || r.getGuest().getEmail().equalsIgnoreCase(searchedEmail)
                    || r.getGuest().getPhone().equalsIgnoreCase(searchedPhone)
                    || String.valueOf(r.getStatus()).equals(searchedStatus)
                    || r.getCheckOut().equals(searchedOut)
                    || r.getCheckIn().equals(searchedIn);
        });
        return filteredRes;
    }


    @FXML
    private void handleCancelBooking(){
        Reservation res = (Reservation) adminTable.getSelectionModel().getSelectedItem();
        res.setStatus(ReservationStatus.CANCELLED);
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
    private void toEditBooking() throws IOException{
        Reservation res = (Reservation) adminTable.getSelectionModel().getSelectedItem();
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AddUpdateBooking.fxml", (AdminBookingController controller) -> {
            controller.setRes(res);
            if (res != null) controller.setGuest(res.getGuest());
            else controller.setGuest(null);
            controller.setUser(this.adminuser);
        });
    }

    @FXML
    private void toAddBooking() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AddUpdateBooking.fxml", (AdminBookingController controller) -> {
            controller.setGuest(null);
            controller.setUser(this.adminuser);
        });
    }

    @FXML
    private void onWaitlistPress() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecacollege/hotel/application/WaitlistDialog.fxml"));
        DialogPane dialogPane = loader.load();
        Dialog<String> dialog = new Dialog<>();
        WaitlistController wc = loader.getController();
        wc.setWaitService(_waitService);
        dialog.setTitle("Waitlist");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setDialogPane(dialogPane);
        dialog.show();
    }

    public void setAdminuser(AdminUser adminuser) {
        this.adminuser = adminuser;
    }
}
