package ca.senecacollege.hotel.controllers;


import ca.senecacollege.hotel.models.Charge;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;
import ca.senecacollege.hotel.services.IBillingService;
import ca.senecacollege.hotel.services.IReservationService;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

public class AdminBookingController implements SceneManagerAware {
    private final IBillingService _billService;
    private final IReservationService _resService;
    private SceneManager sceneManager;
    private Reservation res;

    @Inject
    public AdminBookingController(IBillingService billService, IReservationService resService){
        _billService = billService;
        _resService = resService;
    }

    public void initialize(){
        setupCountryBox();
        setupGuestSpinners();


        saveBtn.disableProperty().bind(nameError.visibleProperty()
                .or(loyaltyError.visibleProperty())
                .or(phoneError.visibleProperty())
                .or(emailError.visibleProperty())
                .or(checkinError.visibleProperty())
                .or(checkoutError.visibleProperty())
        );

        if(res == null){
            // initialize form for a new reservation
            res = new Reservation();
            String requiredError = "* required";

            adminBookingHeader.setText("Create New Booking");
            nameError.setText(requiredError);
            nameError.setVisible(true);
            phoneError.setText(requiredError);
            phoneError.setVisible(true);
            emailError.setText(requiredError);
            emailError.setVisible(true);
            phoneError.setText(requiredError);
            phoneError.setVisible(true);

        } else {
            // initialize form for an existing reservation

        }

    }

    private void setupCountryBox(){
        ObservableList<String> countries = FXCollections.observableArrayList("United States of America", "Canada", "China", "Indonesia", "Vietnam", "Estonia", "Other");
        countryBox.setItems(countries);
    }

    private void setupGuestSpinners(){
        SpinnerValueFactory<Integer> guestValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1);
        adultSpinner.setValueFactory(guestValueFactory);
        childSpinner.setValueFactory(guestValueFactory);
    }

    public void setRes(Optional<Reservation> res) {
        this.res = res.orElse(null);
    }

    @FXML
    private void toDash() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AdminDashboard.fxml", null);
    }

    @Override
    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    @FXML
    private Label adminBookingHeader;
    @FXML
    private Label nameTxt;
    @FXML
    private Label loyaltyTxt;
    @FXML
    private Label phoneTxt;
    @FXML
    private Label emailTxt;
    @FXML
    private Label countryTxt;
    @FXML
    private Label checkinTxt;
    @FXML
    private Label checkoutTxt;
    @FXML
    private Label billRoom;
    @FXML
    private Label billAddon;
    @FXML
    private Label billSubtotal;
    @FXML
    private Label billTax;
    @FXML
    private Label billTotal;
    @FXML
    private Label billPayments;
    @FXML
    private Label billBalance;
    @FXML
    private Label billRoomChange;
    @FXML
    private Label billAddonChange;
    @FXML
    private Label billSubtotalChange;
    @FXML
    private Label billTaxChange;
    @FXML
    private Label billTotalChange;
    @FXML
    private Label billPaymentsChange;
    @FXML
    private Label billBalanceChange;
    @FXML
    private Label pointsTxt;
    @FXML
    private Label pointsLbl;

    @FXML
    private TextField nameInput;
    @FXML
    private TextField loyaltyInput;
    @FXML
    private TextField phoneInput;
    @FXML
    private TextField emailInput;
    @FXML
    private TextField discountInput;
    @FXML
    private TextField paymentInput;

    @FXML
    private DatePicker checkinInput;
    @FXML
    private DatePicker checkoutInput;

    @FXML
    private Label nameError;
    @FXML
    private Label loyaltyError;
    @FXML
    private Label phoneError;
    @FXML
    private Label emailError;
    @FXML
    private Label checkinError;
    @FXML
    private Label checkoutError;
    @FXML
    private Label bookedRoomErr;
    @FXML
    private Label availRoomErr;
    @FXML
    private Label discountErr;
    @FXML
    private Label paymentErr;

    @FXML
    private ListView<Room> resRooms;
    @FXML
    private ListView<Room> availRooms;

    @FXML
    private CheckBox signUp;
    @FXML
    private CheckBox breakfast;
    @FXML
    private CheckBox parking;
    @FXML
    private CheckBox wifi;
    @FXML
    private CheckBox spa;

    @FXML
    private ComboBox<String> countryBox;
    @FXML
    private ComboBox<RoomType> roomFilter;
    @FXML
    private ComboBox<Charge> chargeBox;
    @FXML
    private ComboBox<String> paymentTypeBox;

    @FXML
    private Spinner<Integer> adultSpinner;
    @FXML
    private Spinner<Integer> childSpinner;

    @FXML
    private Button updateName;
    @FXML
    private Button updateLoyalty;
    @FXML
    private Button updatePhone;
    @FXML
    private Button updateEmail;
    @FXML
    private Button updateCountry;
    @FXML
    private Button updateCheckin;
    @FXML
    private Button updateCheckout;
    @FXML
    private Button addRoom;
    @FXML
    private Button rmvRoom;
    @FXML
    private Button repRoom;
    @FXML
    private Button applyDiscount;
    @FXML
    private Button applyPayment;
    @FXML
    private Button saveBtn;
    @FXML
    private Button returnBtn;
    @FXML
    private Button cancellationBtn;
    @FXML
    private Button checkoutBtn;
    @FXML
    private Button updateGuests;
}
