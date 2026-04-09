package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.ReservationStatus;
import ca.senecacollege.hotel.services.*;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import ca.senecacollege.hotel.utilities.UserContext;
import com.google.inject.Inject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class AdminController implements SceneManagerAware {
    private final IActivityLogService _logService;
    private final IAuthService _authService;
    private final IReservationService _resService;
    private final IWaitlistService _waitService;
    SceneManager sceneManager;
    private List<Reservation> allReservations;

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
    private ListView<ReservationStatus> statusList;
    @FXML
    TextField emailSearch;
    @FXML
    DatePicker fromSearch;
    @FXML
    DatePicker toSearch;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button checkinBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Pagination adminPage;

    @Inject
    public AdminController(IActivityLogService logService, IAuthService authService, IReservationService resService, IWaitlistService waitService){
        _logService = logService;
        _authService = authService;
        _resService = resService;
        _waitService = waitService;
        allReservations = _resService.getAllReservations();
    }

    @Override
    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    public void initialize(){
        if(nameCol != null){
            setTableData(FXCollections.observableArrayList(_resService.getAllReservations()));
            phoneSearch.textProperty().addListener((observable, oldValue, newValue) ->{
                if(!newValue.matches("\\d*")){
                    phoneSearch.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
            statusList.setItems(FXCollections.observableArrayList(ReservationStatus.values()));
            statusList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            fromSearch.setValue(LocalDate.now().withDayOfMonth(1));
            toSearch.setValue(YearMonth.now().atEndOfMonth());
        }
        if(cancelBtn != null) cancelBtn.disableProperty().bind(adminTable.getSelectionModel().selectedItemProperty().isNull());
        if(editBtn != null) editBtn.disableProperty().bind(adminTable.getSelectionModel().selectedItemProperty().isNull());
        if(checkinBtn!= null){
            adminTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
                checkinBtn.setDisable(true);
                if(newValue != null){
                    Reservation res = (Reservation) newValue;
                    if(res.getStatus() == ReservationStatus.BOOKED) checkinBtn.setDisable(false);
                }
            }));
        }
    }

    public void refreshTable(){
        allReservations = FXCollections.observableArrayList(_resService.getAllReservations());
        setTableData(null);
    }

    private void setTableData(ObservableList<Reservation> resList){
        ObservableList<Reservation> allRes = FXCollections.observableArrayList();
        if(resList == null) {
            allRes = FXCollections.observableArrayList(allReservations);
        }
        else{
            allRes = resList;
        }

        int maxItemPerPage = 30;
        int numOfData = allRes.size();
        int numPages = numOfData / maxItemPerPage;
        adminPage.setPageCount(numPages > 0 ? numPages : 1);
        ObservableList<Reservation> finalAllRes = allRes;
        adminPage.setPageFactory((Integer idx) ->{
            int from = idx * (Math.min(maxItemPerPage, finalAllRes.size()));
            int to = Math.min(from + maxItemPerPage, finalAllRes.size());
            adminTable.setItems(FXCollections.observableArrayList(finalAllRes.subList(from, to)));
            return adminTable;
        });

        adminTable.setMinHeight(500);
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
        if(_authService.authetnicateLogin(user, pass)) {
            sceneManager.switchScene("/ca/senecacollege/hotel/application/AdminDashboard.fxml", null);
        }
        else {
            errLbl.setText("User not found");
        }
    }

    @FXML
    private void handleSearch(){
        ObservableList<Reservation> observableMasterData = FXCollections.observableArrayList(allReservations);
        FilteredList<Reservation> filteredRes = getReservations(observableMasterData);
        setTableData(filteredRes);
    }

    private FilteredList<Reservation> getReservations(ObservableList<Reservation> observableMasterData) {
        String searchedName = nameSearch.getText();
        String searchedPhone = phoneSearch.getText();
        String searchedEmail = emailSearch.getText();
        ObservableList<ReservationStatus> searchedStatus = statusList.getSelectionModel().getSelectedItems();
        LocalDate searchedIn = fromSearch.getValue();
        LocalDate searchedOut = toSearch.getValue();
        FilteredList<Reservation> filteredRes = new FilteredList<>(observableMasterData, r->{
            return r.getGuest().getName().toLowerCase().contains(searchedName.toLowerCase())
                    && r.getGuest().getEmail().toLowerCase().contains(searchedEmail.toLowerCase())
                    && r.getGuest().getPhone().toLowerCase().contains(searchedPhone.toLowerCase())
                    && searchedStatus.isEmpty() || searchedStatus.contains(r.getStatus())
                    && !r.getCheckOut().isAfter(searchedOut)
                    && !r.getCheckIn().isBefore(searchedIn);
        });
        String statuses = "{ | ";
        for(ReservationStatus rs : searchedStatus){
            statuses += rs.name() + " | ";
        }
        statuses += " | }";
        _logService.search(searchedName, phoneSearch.getText(), emailSearch.getText(),statuses, searchedIn, searchedOut );
        return filteredRes;
    }

    @FXML
    private void handleCancelBooking(){
        Reservation res = (Reservation) adminTable.getSelectionModel().getSelectedItem();
        _resService.cancelReservation(res);
        adminTable.refresh();
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
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AdminDashboard.fxml", null);
    }
    @FXML
    private void toLoyalty() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/LoyaltyDashboard.fxml", null);
    }

    @FXML
    private void toEditBooking() throws IOException{
        Reservation res = (Reservation) adminTable.getSelectionModel().getSelectedItem();
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AddUpdateBooking.fxml", (AdminBookingController controller) -> {
            controller.setRes(res);
            if (res != null) controller.setGuest(res.getGuest());
            else controller.setGuest(null);
        });
    }

    @FXML
    private void toAddBooking() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AddUpdateBooking.fxml", (AdminBookingController controller) -> {
            controller.setGuest(null);
        });
    }

    @FXML
    private void onWaitlistPress() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecacollege/hotel/application/WaitlistDialog.fxml"));
        DialogPane dialogPane = loader.load();
        Dialog<String> dialog = new Dialog<>();
        WaitlistController wc = loader.getController();
        wc.setWaitService(_waitService, _resService);
        wc.setAdminControl(this);
        dialog.setTitle("Waitlist");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setDialogPane(dialogPane);
        dialog.show();
    }

    @FXML
    private void onCheckin(){
        Reservation res = (Reservation) adminTable.getSelectionModel().getSelectedItem();
        _resService.attemptCheckin(res);
        adminTable.refresh();
    }
}
