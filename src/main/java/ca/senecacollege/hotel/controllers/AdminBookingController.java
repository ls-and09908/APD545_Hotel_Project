package ca.senecacollege.hotel.controllers;


import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;
import ca.senecacollege.hotel.repositories.IGuestRepository;
import ca.senecacollege.hotel.services.IBillingService;
import ca.senecacollege.hotel.services.ILoyaltyService;
import ca.senecacollege.hotel.services.IReservationService;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class AdminBookingController implements SceneManagerAware {
    private final IBillingService _billService;
    private final IReservationService _resService;
    private final ILoyaltyService _loyaltyService;
    private ObjectProperty<AdminUser> user = new SimpleObjectProperty<>();
    private BooleanProperty showMemberMenus = new SimpleBooleanProperty(false);
    private SceneManager sceneManager;
    private ObjectProperty<Reservation> res = new SimpleObjectProperty<>();
    private ObjectProperty<Guest> guest = new SimpleObjectProperty<>();
    private String requiredError = "* required";
    private ObservableList<Room> reservationRooms = FXCollections.observableArrayList();
    private ObservableList<Room> availableRooms = FXCollections.observableArrayList();
    private ArrayList<CheckBox> addOnCheckboxes = new ArrayList<>();
    private DoubleProperty roomCharge = new SimpleDoubleProperty();
    private DoubleProperty addonCharge = new SimpleDoubleProperty();
    private DoubleBinding subtotal;
    private DoubleBinding tax;
    private DoubleProperty discount = new SimpleDoubleProperty();
    private DoubleBinding total;
    private DoubleProperty paymentsMade = new SimpleDoubleProperty();
    private DoubleProperty remainingBalance = new SimpleDoubleProperty();

    @Inject
    public AdminBookingController(IBillingService billService, IReservationService resService, ILoyaltyService loyaltyService){
        _billService = billService;
        _resService = resService;
        _loyaltyService = loyaltyService;
    }

    public void initialize(){
        addOnCheckboxes.addAll(List.of(breakfast, parking, wifi, spa));
        setupBlankGuest();
        setupNewBooking();

        breakfast.setUserData(_resService.getAddOn("breakfast"));
        spa.setUserData(_resService.getAddOn("spa"));
        wifi.setUserData(_resService.getAddOn("wifi"));
        parking.setUserData(_resService.getAddOn("parking"));

        setupCountryBox();
        setupGuestSpinners();
        setupPaymentTypes();
        setupRoomFilter();

        saveBtn.disableProperty().bind(nameErr.visibleProperty()
                .or(loyaltyErr.visibleProperty())
                .or(phoneErr.visibleProperty())
                .or(emailErr.visibleProperty())
                .or(dateErr.visibleProperty())
        );

        setupRoomLists();
        setupBillBindings();

        showMemberMenus.bind(loyaltyTxt.visibleProperty());
        loyaltyInput.disableProperty().bind(loyaltyTxt.visibleProperty());
        updateLoyalty.disableProperty().bind(loyaltyInput.textProperty().isEmpty().or(loyaltyErr.visibleProperty()));
        updateEmail.disableProperty().bind(emailInput.textProperty().isEmpty().or(emailErr.visibleProperty()));
        updateName.disableProperty().bind(nameInput.textProperty().isEmpty().or(nameErr.visibleProperty()));
        updatePhone.disableProperty().bind(phoneInput.textProperty().isEmpty().or(phoneErr.visibleProperty()));
        updateGuests.disableProperty().bind(adultSpinner.valueProperty().isEqualTo(0).and(childSpinner.valueProperty().isEqualTo(0)));
        roomFilter.disableProperty().bind(dateErr.visibleProperty().or(updateDate.disableProperty().not()));
        applyDiscount.disableProperty().bind(discountErr.visibleProperty().or(discountInput.textProperty().isEmpty()).or(user.isNull()));
        applyPayment.disableProperty().bind(paymentErr.visibleProperty().or(paymentInput.textProperty().isEmpty()).or(user.isNull()));
        guest.addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setupGuest();
                if (newValue.isLoyal()) {
                    setupLoyalGuest();
                }
            }
        }));
        res.addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setupReservation();
            }
        }));

        registerListeners();
    }

    private void registerListeners(){
        showMemberMenus.addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                paymentTypeBox.getItems().remove(PaymentMethod.LOYALTY);
                pointsTxt.setVisible(false);
                pointsLbl.setVisible(false);
                pointsEarned.setVisible(false);
                billPoints.setVisible(false);
            } else {
                paymentTypeBox.getItems().add(PaymentMethod.LOYALTY);
                pointsTxt.setText(guest.get() != null ? String.format("%d Pts",guest.get().getLoyaltyPoints()) : "");
                pointsTxt.setVisible(true);
                pointsLbl.setVisible(true);
                pointsEarned.setVisible(true);
                billPoints.setVisible(true);
                billPoints.setText(String.valueOf(guest.get().getLoyaltyPoints()));
            }
        }));

        updateLoyalty.setOnAction((event)->{
            String text = loyaltyInput.getText();
            if(text.isBlank()) loyaltyErr.setVisible(false);
            try {
                Integer num = Integer.parseInt(text);
                Guest g = retrieveMembership(num);
                if (g == null){
                    loyaltyErr.setText("Loyalty not found");
                    loyaltyErr.setVisible(true);
                } else guest.set(g);
            } catch (NumberFormatException e) {
                loyaltyErr.setText("Not a number");
                loyaltyErr.setVisible(true);
                // TODO: log this
            }
        });

        emailInput.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isBlank()){
                emailErr.setText(requiredError);
                emailErr.setVisible(true);
            } else { emailErr.setVisible(false); }
        }));

        nameInput.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isBlank()){
                nameErr.setText(requiredError);
                nameErr.setVisible(true);
            } else { nameErr.setVisible(false); }
        }));

        phoneInput.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isBlank()){
                phoneErr.setText(requiredError);
                phoneErr.setVisible(true);
            } else if (!newValue.matches("^[0-9]{1,12}$")) {
                phoneErr.setText("Must be numerical");
                phoneErr.setVisible(true);
                // TODO: log this
            } else { phoneErr.setVisible(false); }
        }));

        countryBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null)updateCountry.setDisable(false);
        }));

        checkinInput.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (res.get() != null && newValue != null) updateDate.setDisable(!confirmDates(newValue, checkoutInput.getValue()));
        }));

        checkoutInput.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (res.get() != null && newValue != null) updateDate.setDisable(!confirmDates(checkinInput.getValue(), newValue));
        }));

        resRooms.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            toggleRoomButtons();
        }));
        availRooms.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            toggleRoomButtons();
        }));

        roomFilter.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            refreshAvailableRooms(newValue);
            toggleRoomButtons();
        }));

        breakfast.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            handleAddOnChecks(breakfast, newValue);
        }));

        parking.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            handleAddOnChecks(parking, newValue);
        }));

        wifi.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            handleAddOnChecks(wifi, newValue);
        }));

        spa.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            handleAddOnChecks(spa, newValue);
        }));

        discountInput.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d{1,2}(\\.\\d{1,2})?")) {
                discountErr.setText("Discount must be numerical");
                discountErr.setVisible(true);
            } else discountErr.setVisible(false);
        }));

        paymentInput.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d+(\\.\\d{1,2})?")) {
                paymentErr.setText("Payment must be numerical");
                paymentErr.setVisible(true);
            } else paymentErr.setVisible(false);
        }));

        reservationRooms.addListener((ListChangeListener<Room>) change -> {
            res.get().setBilling(_billService.updateBillCharges(res.get()));
            roomCharge.setValue(res.get().getBilling().getRoomCharges());
        });

        paymentTypeBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null){
                switch (newValue){
                    case DEPOSIT -> {
                        paymentInput.setDisable(true);
                        paymentInput.setText(String.valueOf(_billService.getDeposit(res.get().getBilling())));
                    }
                    case LOYALTY -> {
                        paymentInput.setDisable(true);
                        paymentInput.setText(String.format("%.2f",_loyaltyService.pointsToDollars(guest.get().getLoyaltyPoints())));
                    }
                    default -> paymentInput.setDisable(false);
                }
            }
        }));
    }

    @FXML
    private Guest retrieveMembership(int loyaltyNum){
        return _loyaltyService.getLoyalGuest(loyaltyNum);
    }

    private void handleAddOnChecks(CheckBox cb, boolean checked){
        if (res.get() != null) {
            if (checked) res.get().addAddOn((AddOn) cb.getUserData());
            else res.get().removeAddOn((AddOn) cb.getUserData());
            _billService.updateBillCharges(res.get());
            addonCharge.set(res.get().getBilling().getAddOnCharges());
        }
    }

    private void setupBillBindings(){
        subtotal = roomCharge.add(addonCharge);
        tax = subtotal.multiply(0.13);
        total = subtotal.add(tax).subtract(discount);
        remainingBalance.bind(total.subtract(paymentsMade));

        billRoom.textProperty().bind(roomCharge.asString("$%.2f"));
        billAddon.textProperty().bind(addonCharge.asString("$%.2f"));
        billSubtotal.textProperty().bind(subtotal.asString("$%.2f"));
        billTax.textProperty().bind(tax.asString("$%.2f"));
        billDiscount.textProperty().bind(discount.asString("$%.2f"));
        billTotal.textProperty().bind(total.asString("$%.2f"));
        billPayments.textProperty().bind(paymentsMade.asString("$%.2f"));
        billBalance.textProperty().bind(remainingBalance.asString("$%.2f"));
    }

    private void setupBlankGuest(){
        guest.set(new Guest());

        nameErr.setText(requiredError);
        nameErr.setVisible(true);
        phoneErr.setText(requiredError);
        phoneErr.setVisible(true);
        emailErr.setText(requiredError);
        emailErr.setVisible(true);
        loyaltyTxt.setVisible(false);
        signUp.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                loyaltyTxt.setText("");
                loyaltyTxt.setVisible(false);
            } else {
                loyaltyTxt.setText("Joining Membership");
                loyaltyTxt.setVisible(true);
                guest.get().makeLoyaltyMember(_loyaltyService.getNewLoyaltyNum());
                loyaltyInput.setText("");
            }
        }));
    }

    /**
     * Populates the guest form with the information of the guest passed to the controller
     */
    private void setupGuest(){
        nameErr.setVisible(false);
        phoneErr.setVisible(false);
        emailErr.setVisible(false);

        nameTxt.setText(guest.get().getName());
        phoneTxt.setText(guest.get().getPhone());
        emailTxt.setText(guest.get().getEmail());
        countryBox.getSelectionModel().select(guest.get().getCountry());
    }

    /**
     * Populates the guest form with the loyalty information of the guest passed to the controller
     */
    private void setupLoyalGuest(){
        loyaltyTxt.setText(String.valueOf(guest.get().getLoyaltyNum()));
        loyaltyErr.setVisible(false);

        signUp.setDisable(true);
        signUp.setVisible(false);
    }

    /**
     * Initializes the form for a new reservation
     */
    private void setupNewBooking(){
        res.set(new Reservation());
        res.get().setBilling(new Billing(res.get()));

        adminBookingHeader.setText("Create New Booking");
        checkinInput.valueProperty().setValue(LocalDate.now());
        checkoutInput.valueProperty().setValue(LocalDate.now());
        dateErr.setText(requiredError);
        dateErr.setVisible(true);

        reservationRooms.addAll(res.get().getRooms());
        for(CheckBox cb : addOnCheckboxes) {
            cb.disableProperty().bind(dateErr.visibleProperty().or(updateDate.disableProperty().not()));
        }
    }

    /**
     * Initializes the form to update an existing reservation
     */
    private void setupReservation(){
        adminBookingHeader.setText("Update Booking");
        saveBtn.setText("Save Changes");
        cancellationBtn.setVisible(true);
        checkoutBtn.setVisible(true);
        checkoutBtn.disableProperty().bind(billBalance.textProperty().isNotEqualTo("0.00"));

        checkinInput.setValue(res.get().getCheckIn());
        checkoutInput.setValue(res.get().getCheckOut());
        nAdults.setText(String.valueOf(res.get().getAdults()));
        nChildren.setText(String.valueOf(res.get().getChildren()));

        Set<AddOn> addOns = res.get().getAddOns();
        for(CheckBox cb : addOnCheckboxes){
            cb.disableProperty().unbind();
            if (addOns.contains((AddOn) cb.getUserData())){
                cb.selectedProperty().setValue(true);
            }
        }
        if(res.get().getStatus() != ReservationStatus.BOOKING) {
            paymentTypeBox.getItems().remove(PaymentMethod.DEPOSIT);
        }

        clearRooms();
        reservationRooms.addAll(res.get().getRooms());
        refreshAvailableRooms(null);
    }

    private void setupRoomFilter(){
        ObservableList<RoomType> roomTypes = FXCollections.observableArrayList(RoomType.values());
        roomTypes.add(null);
        roomFilter.setItems(roomTypes);
    }

    private void setupPaymentTypes(){
        ObservableList<PaymentMethod> paymentTypes = FXCollections.observableArrayList(PaymentMethod.values());
        paymentTypes.remove(PaymentMethod.LOYALTY);
        paymentTypeBox.setItems(paymentTypes);
    }

    private void setupCountryBox(){
        ObservableList<String> countries = FXCollections.observableArrayList("United States of America", "Canada", "China", "Indonesia", "Vietnam", "Estonia", "Other");
        countryBox.setItems(countries);
    }

    private void setupGuestSpinners(){
        SpinnerValueFactory<Integer> aValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1);
        SpinnerValueFactory<Integer> cValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1);
        adultSpinner.setValueFactory(aValueFactory);
        childSpinner.setValueFactory(cValueFactory);
    }

    private boolean confirmDates(LocalDate in, LocalDate out){
        if(in.isAfter(out)) {
            dateErr.setText("Check-in date must be before check-out");
            dateErr.setVisible(true);
            return false;
        }
        else if(in.isBefore(LocalDate.now())){
            dateErr.setText("Must be on or after the current date");
            dateErr.setVisible(true);
            return false;
        }
        dateErr.setVisible(false);
        return true;
        // TODO: log validation failure
    }

    private void setupRoomLists(){
        resRooms.setItems(reservationRooms);
        availRooms.setItems(availableRooms);

        toggleRoomButtons();
    }

    private void toggleRoomButtons(){
        boolean rmvToggle = reservationRooms.isEmpty() || resRooms.getSelectionModel().getSelectedItem() == null;
        rmvRoom.setDisable(rmvToggle);
        boolean availToggle = availableRooms.isEmpty() || availRooms.getSelectionModel().getSelectedItem() == null;
        addRoom.setDisable(availToggle);
        repRoom.setDisable(rmvToggle || availToggle);
    }

    private void clearRooms(){
        reservationRooms.clear();
    }

    private void refreshRoomsList(){
        clearRooms();
        refreshAvailableRooms(null);
        toggleRoomButtons();
    }

    private void refreshAvailableRooms(RoomType type){
        if(res.get().getCheckIn() != null && res.get().getCheckOut() != null){
            availableRooms.clear();
            List<Room> refreshedRooms = _resService.findAvailableRoom(res.get().getCheckIn(), res.get().getCheckOut(), type);
            if (!refreshedRooms.isEmpty()) {
                availableRooms.addAll(refreshedRooms);
            }
        }
    }

    @FXML
    private void onAddRoom(){
        Room rm = availRooms.getSelectionModel().getSelectedItem();
        res.get().addRoom(rm);
        reservationRooms.add(rm);
        availableRooms.remove(rm);
    }

    @FXML
    private void onRemoveRoom(){
        Room rm = resRooms.getSelectionModel().getSelectedItem();
        availableRooms.add(rm);
        reservationRooms.remove(rm);
        res.get().removeRoom(rm);
    }

    @FXML
    private void onReplaceRoom(){
        onRemoveRoom();
        onAddRoom();
    }

    @FXML
    private void onUpdateName(){
        String text = nameInput.getText();
        nameTxt.setText(text);
        nameTxt.setVisible(true);
        nameErr.setVisible(false);
        guest.get().setName(text);
    }

    @FXML
    private void onUpdatePhone(){
        String text = phoneInput.getText();
        phoneTxt.setText(text);
        phoneTxt.setVisible(true);
        phoneErr.setVisible(false);
        guest.get().setPhone(text);
    }

    @FXML
    private void onUpdateEmail(){
        String text = emailInput.getText();
        if(_resService.checkGuestEmail(text)){
            emailTxt.setText(text);
            emailTxt.setVisible(true);
            emailErr.setVisible(false);
            guest.get().setEmail(text);
        } else {
            emailErr.setText("Email already in use.");
            emailErr.setVisible(true);
            // TODO: log validation failure
        }
    }

    @FXML
    private void onUpdateCountry(){
        String text = countryBox.getSelectionModel().getSelectedItem();
        countryTxt.setText(text);
        updateCountry.setDisable(true);
        guest.get().setCountry(text);
    }

    @FXML
    private void onUpdateDate(){
        res.get().setCheckIn(checkinInput.getValue());
        res.get().setCheckOut(checkoutInput.getValue());
        refreshRoomsList();
        updateDate.setDisable(true);
    }

    @FXML
    private void onUpdateGuests(){
        int num = adultSpinner.getValue();
        res.get().setAdults(num);
        nAdults.setText(String.valueOf(num));
        num = childSpinner.getValue();
        res.get().setChildren(num);
        nChildren.setText(String.valueOf(num));
    }

    @FXML
    private void onApplyDiscount(){
        try{
            Double percent = Double.parseDouble(discountInput.getText());
            if(!_billService.applyDiscount(res.get().getBilling(), percent, user.get().getRole())){
                discountErr.setText("Discount value is above maximum allowed.");
                discountErr.setVisible(true);
            } else {
                discountErr.setVisible(false);
                discount.setValue(subtotal.multiply(percent/100).getValue());
                billDiscount.textProperty().bind(discount.asString("$%.2f"));
            }
        } catch (NumberFormatException e) {
            // TODO: log validation failure
            discountErr.setText("Invalid value");
            discountErr.setVisible(true);
        }
    }

    @FXML
    private void onApplyPayment(){
        PaymentMethod paymentType = paymentTypeBox.getSelectionModel().getSelectedItem();
        try {
            Double amt = Double.parseDouble(paymentInput.getText());
            if(paymentType == PaymentMethod.REFUND) amt = -amt;
            if(_billService.addPaymentToBill(amt, paymentType, res.get().getBilling())){
                if(paymentType == PaymentMethod.DEPOSIT){
                    res.get().setStatus(ReservationStatus.BOOKED);
                    paymentTypeBox.getItems().remove(PaymentMethod.DEPOSIT);
                }
                paymentErr.setVisible(false);
                paymentsMade.setValue(res.get().getBilling().getTotalPayments());
                int numPoints;
                int ptsEarned;
                if(paymentType != PaymentMethod.LOYALTY) {
                    numPoints = _loyaltyService.getPointsFromPayment(amt);

                    ptsEarned = Integer.parseInt(billPoints.getText());
                    billPoints.setText(String.valueOf(ptsEarned+numPoints));
                    _loyaltyService.earnPoints(numPoints, guest.get());
                    pointsTxt.setText(String.format("%d Pts", guest.get().getLoyaltyPoints()));
                } else {
                    numPoints = _loyaltyService.spendPoints(amt, guest.get());
                    ptsEarned = Integer.parseInt(billPoints.getText());
                    billPoints.setText(String.valueOf(ptsEarned-numPoints));
                    pointsTxt.setText(String.format("%d Pts", guest.get().getLoyaltyPoints()));
                }
            } else {
                paymentErr.setText("Payment cannot be applied.");
                paymentErr.setVisible(true);
            }

        } catch (NumberFormatException e){
            // TODO: log validation failure
            paymentErr.setText("Invalid value");
            paymentErr.setVisible(true);
        }
    }

    @FXML
    private void onSave() throws IOException {
        if(res.get().getStatus() == ReservationStatus.BOOKING){
            paymentErr.setText("Must pay deposit to confirm.");
            paymentErr.setVisible(true);
            return;
        } else {
            res.get().setGuest(guest.get());
            _resService.saveReservation(res.get());
            toDash();
        }
    }

    @FXML
    private void toDash() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AdminDashboard.fxml", (AdminController controller) -> controller.setAdminuser(this.user.get()));
    }

    public void setRes(Reservation res) { if(res != null) this.res.setValue(res); }
    public void setGuest(Guest guest){ if(guest != null) this.guest.setValue(guest); }
    public void setUser(AdminUser user) { if(user != null) this.user.setValue(user); }

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
    private Label billDiscount;
    @FXML
    private Label pointsTxt;
    @FXML
    private Label pointsLbl;
    @FXML
    private Label pointsEarned;
    @FXML
    private Label billPoints;
    @FXML
    private Label nAdults;
    @FXML
    private Label nChildren;

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
    private Label nameErr;
    @FXML
    private Label loyaltyErr;
    @FXML
    private Label phoneErr;
    @FXML
    private Label emailErr;
    @FXML
    private Label dateErr;
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
    private ComboBox<PaymentMethod> paymentTypeBox;

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
    private Button updateDate;
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
