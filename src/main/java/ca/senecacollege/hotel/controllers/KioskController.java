package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.services.BillingService;
import ca.senecacollege.hotel.services.LoyaltyService;
import ca.senecacollege.hotel.services.ReservationService;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class KioskController implements SceneManagerAware {
    private SceneManager sceneManager;
    private static Reservation tempReservation = new Reservation();
    private static Guest tempGuest;
    private static double finalCost;
    private static int numAdult = 0;
    private static int numChildren = 0;
    private static LocalDate checkInDate;
    private static LocalDate checkOutDate;
    private static String name;
    private static String phone;
    private static String email;
    private static String country;
    private BillingService _billingService;
    private LoyaltyService _loyaltyService;
    private ReservationService _reservationService;

    private List<Image> imageList = new ArrayList<>();
    private int imageIndex = 0;
    private ArrayList<String> imageText;
    @FXML
    private ImageView diagramImage;
    @FXML
    private Label diagramLbl;

    //<editor-fold desc="FXMLElements">
    //<editor-fold desc="WelcomeFXML">
    //</editor-fold>
    //<editor-fold desc="Screen1FXML">
    @FXML
    Spinner<Integer> adultSpinner;
    @FXML
    Spinner<Integer> childSpinner;
    @FXML
    Button guestCountNextBtn;
    //</editor-fold>

    //<editor-fold desc="Screen2FXML">
    @FXML
    DatePicker inDate;
    @FXML
    DatePicker outDate;
    @FXML
    Button rentalDatesBtn;
    @FXML
    Label dateScreenErr;
    //</editor-fold>

    //<editor-fold desc="Screen3FXML">
    @FXML
    Spinner<Integer> singleSpinner;
    @FXML
    Spinner<Integer> doubleSpinner;
    @FXML
    Spinner<Integer> pentSpinner;
    @FXML
    Spinner<Integer> deluxeSpinner;
    @FXML
    Button roomSelectNextBtn;
    @FXML
    Label suggestionSuccessLbl;
    //</editor-fold>

    //<editor-fold desc="Screen4FXML">
    @FXML
    Spinner<String> countrySpinner;
    @FXML
    TextField nameTxt;
    @FXML
    TextField loyaltyTxt;
    @FXML
    TextField phoneTxt;
    @FXML
    TextField emailTxt;
    @FXML
    Text nameLbl;
    @FXML
    Text loyaltyLbl;
    @FXML
    Text phoneLbl;
    @FXML
    Text countryLbl;
    @FXML
    Text emailLbl;
    @FXML
    Button clientDetailsNextBtn;
    @FXML
    Button updateEmail;
    @FXML
    Button updateName;
    @FXML
    Button updatePhone;
    @FXML
    Button updateCountry;
    @FXML
    Button updateLoyalty;
    @FXML
    private Text loyaltyErr;
    @FXML
    private Text emailErr;
    @FXML
    Button signupBtn;
    //</editor-fold>

    //<editor-fold desc="Screen5FXML">
    @FXML
    CheckBox wifiBox;
    @FXML
    CheckBox breakfastBox;
    @FXML
    CheckBox spaBox;
    @FXML
    CheckBox parkingBox;
    @FXML
    Button addonsNextBtn;
    //</editor-fold>

    //<editor-fold desc="Screen6FXML">
    @FXML
    private Text billRoomCost;
    @FXML
    private Text billAddonCost;
    @FXML
    private Text billSubtotalCost;
    @FXML
    private Text billTaxCost;
    @FXML
    private Text billTotalCost;
    @FXML
    private Text billBalance;
    @FXML
    private Text billDepositCost;
    @FXML
    private ListView<Charge> roomCharges;
    @FXML
    private ListView<Charge>addonCharges;
    //</editor-fold>

    //<editor-fold desc="Screen7FXML">
    @FXML
    Text conNameTxt;
    @FXML
    Text conPhoneTxt;
    @FXML
    Text conEmailTxt;
    @FXML
    Text conCostTxt;
    @FXML
    Text addonTxt1;
    @FXML
    Text addonTxt2;
    @FXML
    Text addonTxt3;
    @FXML
    Text addonTxt4;
    //</editor-fold>

    //</editor-fold>

    @Inject
    public KioskController(LoyaltyService loyaltyService, BillingService billingService, ReservationService reservationService){
        _loyaltyService = loyaltyService;
        _billingService = billingService;
        _reservationService = reservationService;
    }

    //Sets the SceneManager object (Called on instantiation)
    //Also doubles as acting for initialize unless later determined to not work
    @Override
    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    @FXML
    private void initialize() throws IOException {
        disableNextButtons();
        if(diagramImage != null){
            imageText = new ArrayList<>();
            imageList.add(new Image(getClass().getResourceAsStream("/ca/senecacollege/hotel/application/Kiosk1.png")));
            imageList.add(new Image(getClass().getResourceAsStream("/ca/senecacollege/hotel/application/Kiosk2.png")));
            imageList.add(new Image(getClass().getResourceAsStream("/ca/senecacollege/hotel/application/Kiosk3.png")));
            imageList.add(new Image(getClass().getResourceAsStream("/ca/senecacollege/hotel/application/Kiosk4.png")));
            imageList.add(new Image(getClass().getResourceAsStream("/ca/senecacollege/hotel/application/Kiosk5.png")));
            imageList.add(new Image(getClass().getResourceAsStream("/ca/senecacollege/hotel/application/Kiosk6.png")));
            imageList.add(new Image(getClass().getResourceAsStream("/ca/senecacollege/hotel/application/Kiosk7.png")));
            imageList.add(new Image(getClass().getResourceAsStream("/ca/senecacollege/hotel/application/Kiosk8.png")));
            imageList.add(new Image(getClass().getResourceAsStream("/ca/senecacollege/hotel/application/Kiosk9.png")));
            imageList.add(new Image(getClass().getResourceAsStream("/ca/senecacollege/hotel/application/Kiosk10.png")));
            imageText.add("Begin by selecting the number of guests.");
            imageText.add("Select your check in and check out dates.");
            imageText.add("Ensure you are checking out after checking in and that your check in is before today.");
            imageText.add("Select which room styles you would like.");
            imageText.add("Alternative our system can suggest rooms for you.");
            imageText.add("Fill out your personal information.");
            imageText.add("Double check that everything is correct.");
            imageText.add("Select any additional addons.");
            imageText.add("Review your booking.");
            imageText.add("Confirm one final time.");
            diagramImage.setImage(imageList.get(0));
        }
        if(billRoomCost != null){
            setBillingData();
            setBillingDisplay();
        }
        if(conCostTxt != null){
            conCostTxt.setText("$" + String.valueOf(finalCost));
            conEmailTxt.setText(email);
            conNameTxt.setText(name);
            conPhoneTxt.setText(phone);
            addonTxt1.setVisible(false);
            addonTxt2.setVisible(false);
            addonTxt3.setVisible(false);
            addonTxt4.setVisible(false);
        }
        if(wifiBox != null){
            wifiBox.setUserData(_reservationService.getAddOn("wifi"));
            breakfastBox.setUserData(_reservationService.getAddOn("breakfast"));
            spaBox.setUserData(_reservationService.getAddOn("spa"));
            parkingBox.setUserData(_reservationService.getAddOn("parking"));

            wifiBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if(newValue) tempReservation.addAddOn((AddOn)wifiBox.getUserData());
                else tempReservation.removeAddOn((AddOn)wifiBox.getUserData());
            }));
            breakfastBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if(newValue) tempReservation.addAddOn((AddOn)breakfastBox.getUserData());
                else tempReservation.removeAddOn((AddOn)breakfastBox.getUserData());
            }));
            spaBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if(newValue) tempReservation.addAddOn((AddOn)spaBox.getUserData());
                else tempReservation.removeAddOn((AddOn)spaBox.getUserData());
            }));
            parkingBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if(newValue) tempReservation.addAddOn((AddOn)parkingBox.getUserData());
                else tempReservation.removeAddOn((AddOn)parkingBox.getUserData());
            }));
        }
        if(emailTxt!= null){
            emailTxt.textProperty().addListener(((observable) -> {
                emailErr.setVisible(false);
            }));
        }
    }

    private void setBillingData(){
        tempReservation.setGuest(tempGuest);
        tempReservation.setAdults(numAdult);
        tempReservation.setChildren(numChildren);
        tempReservation.setCheckIn(checkInDate);
        tempReservation.setCheckOut(checkOutDate);
        Billing bill = _billingService.generateBill(tempReservation);
        List<Charge> allCharges = bill.getCharges();

        ObservableList<Charge> rmCharges = FXCollections.observableArrayList(allCharges.stream()
                .filter((c)-> c.getSource().getClass() == Room.class).toList());
        ObservableList<Charge> aoCharges = FXCollections.observableArrayList(allCharges.stream()
                .filter((c)-> c.getSource().getClass() == AddOn.class).toList());
        roomCharges.setItems(rmCharges);
        addonCharges.setItems(aoCharges);
    }

    private void setBillingDisplay(){
        Billing bill = tempReservation.getBilling();
        double deposit = _billingService.getDeposit(bill);
        double roomCost = bill.getRoomCharges();
        double addonCost = bill.getAddOnCharges();
        double tax = bill.getTax();
        finalCost = roomCost+addonCost+tax;
        billRoomCost.setText(String.format("$%.2f", roomCost));
        billAddonCost.setText(String.format("$%.2f", addonCost));
        billSubtotalCost.setText(String.format("$%.2f", roomCost+addonCost));
        billTaxCost.setText(String.format("$%.2f", tax));
        billTotalCost.setText(String.format("$%.2f", finalCost));
        billDepositCost.setText(String.format("$%.2f", deposit));
        billBalance.setText(String.format("$%.2f", finalCost - deposit));
    }

    private void setGuestCountSpinners(){
        SpinnerValueFactory<Integer> avalueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1);
        SpinnerValueFactory<Integer> cvalueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1);
        adultSpinner.setValueFactory(avalueFactory);
        childSpinner.setValueFactory(cvalueFactory);
    }

    private void setRoomCountSpinners(){
        SpinnerValueFactory<Integer> svalueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1);
        SpinnerValueFactory<Integer> dvalueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1);
        SpinnerValueFactory<Integer> pvalueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1);
        SpinnerValueFactory<Integer> devalueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1);
        singleSpinner.setValueFactory(svalueFactory);
        doubleSpinner.setValueFactory(dvalueFactory);
        pentSpinner.setValueFactory(pvalueFactory);
        deluxeSpinner.setValueFactory(devalueFactory);
    }

    private void setCountrySpinner(){
        ObservableList<String> countries = FXCollections.observableArrayList("United States of America", "Canada", "China", "Indonesia", "Vietnam", "Estonia", "Other");
        SpinnerValueFactory<String> cvalueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(countries);
        countrySpinner.setValueFactory(cvalueFactory);
    }

    //This function considers which screen the user is currently on and disables the "next" button if the user has not input data
    private void disableNextButtons(){
        //This is necessary to stop the controller from attempting to call these function when not on the first kiosk scene
        if(adultSpinner != null) {
            setGuestCountSpinners();
            guestCountNextBtn.disableProperty().bind(adultSpinner.valueProperty().isEqualTo(0).and(childSpinner.valueProperty().isEqualTo(0)));
        }
        else if(inDate != null){
            rentalDatesBtn.disableProperty().bind(inDate.valueProperty().isNull().or(outDate.valueProperty().isNull()));
            dateScreenErr.setVisible(false);
        }
        else if(singleSpinner != null){
            setRoomCountSpinners();
            roomSelectNextBtn.disableProperty().bind(singleSpinner.valueProperty().isEqualTo(0).and(doubleSpinner.valueProperty().isEqualTo(0)).and(pentSpinner.valueProperty().isEqualTo(0).and(deluxeSpinner.valueProperty().isEqualTo(0))));
        }
        else if(countrySpinner != null){
            nameLbl.setVisible(false);
            loyaltyLbl.setVisible(false);
            phoneLbl.setVisible(false);
            emailLbl.setVisible(false);
            countryLbl.setVisible(false);
            nameLbl.setText("");
            emailLbl.setText("");
            phoneLbl.setText("");
            countryLbl.setText("");
            loyaltyLbl.setText("");
            loyaltyErr.setVisible(false);
            setCountrySpinner();
            clientDetailsNextBtn.disableProperty().bind(nameLbl.textProperty().isEmpty().or(phoneLbl.textProperty().isEmpty().or(emailLbl.textProperty().isEmpty().or(countryLbl.textProperty().isEmpty()))));
        }
    }

    //<editor-fold desc="specificallyClientDetailControls">

    @FXML
    private void onUpdateName(){
        if(! nameTxt.getText().isEmpty()){
            nameLbl.setText(nameTxt.getText());
            nameLbl.setVisible(true);
        }
    }
    @FXML
    private void onUpdateEmail(){
        String text = emailTxt.getText();
        if(!text.isBlank()){
            if(_reservationService.checkGuestEmail(text)) {
                emailLbl.setText(emailTxt.getText());
                emailLbl.setVisible(true);
            } else {
                emailErr.setText("Email already in use");
                emailErr.setVisible(true);
            }
        }
    }
    @FXML
    private void onUpdatePhone(){
        if(! phoneTxt.getText().isEmpty()){
            phoneLbl.setText(phoneTxt.getText());
            phoneLbl.setVisible(true);
        }
    }
    @FXML
    private void onUpdateCountry(){
        if(! countrySpinner.getValue().isEmpty()){
            countryLbl.setText(countrySpinner.getValue());
            countryLbl.setVisible(true);
        }
    }
    @FXML
    private void onUpdateLoyalty(){
        String text = loyaltyTxt.getText();
        if(text.isBlank()) loyaltyErr.setVisible(false);
        try {
            int num = Integer.parseInt(text);
            Guest g = _loyaltyService.getLoyalGuest(num);
            if (g == null){
                loyaltyErr.setText("Loyalty number not found");
                loyaltyErr.setVisible(true);
            } else {
                loyaltyLbl.setText(text);
                loyaltyLbl.setVisible(true);
            }
        } catch (NumberFormatException e) {
            loyaltyErr.setText("Not a number");
            loyaltyErr.setVisible(true);
            // TODO: log this
        }
    }

    @FXML
    private void onRetrieve(){
        String text = emailTxt.getText();
        if(!text.isBlank()){
            Guest g = _reservationService.getGuestByEmail(text);
            if(g != null) {
                setGuestFormData(g);
                emailLbl.setText(emailTxt.getText());
                emailLbl.setVisible(true);
            } else {
                emailErr.setText("Couldn't find the guest with that email.");
                emailErr.setVisible(true);
            }
        }
    }

    private void setGuestFormData(Guest g){
        tempGuest = g;
        nameLbl.setText(g.getName());
        nameLbl.setVisible(true);
        if(g.isLoyal()){
            loyaltyLbl.setText(String.valueOf(g.getLoyaltyNum()));
            loyaltyLbl.setVisible(true);
        }
        phoneLbl.setText(g.getPhone());
        phoneLbl.setVisible(true);
        emailLbl.setText(g.getEmail());
        emailLbl.setVisible(true);
        countryLbl.setText(g.getCountry());
        countryLbl.setVisible(true);
    }
    //</editor-fold>

    //<editor-fold desc="pageNavigationControls">
    @FXML
    private void onToDatesPress() throws IOException {
        if(adultSpinner != null){
            numAdult = adultSpinner.getValue();
            numChildren = childSpinner.getValue();
        }
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskDateSelect.fxml", null);
    }

    @FXML
    private void onAdminLoginPress() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AdminLogin.fxml", null);
    }
    @FXML
    private void onFeedbackPress() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/Feedback.fxml", null);
    }

    @FXML
    private void onToRoomPress() throws IOException{
        if(inDate != null){
            if(confirmDates()){
                checkInDate = inDate.getValue();
                checkOutDate = outDate.getValue();
            }
            else{
                return;
            }
        }
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskRoomSelect.fxml", null);
    }

    private boolean confirmDates(){
        if(inDate.getValue().isAfter(outDate.getValue())){
            dateScreenErr.setText("Error: Check-in date must be before check-out");
            dateScreenErr.setVisible(true);
            return false;
        }
        else if(inDate.getValue().isBefore(LocalDate.now()) || outDate.getValue().isBefore(LocalDate.now())){
            dateScreenErr.setText("Error: Check-in date must be on or after the current date");
            dateScreenErr.setVisible(true);
            return false;
        }
        return true;
    }

    @FXML
    private void backToCountPress() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskGuestCount.fxml", null);
    }

    @FXML
    private void toGuestDetailsPress() throws IOException {
        // TODO: handle adding to waitlist if the requested rooms are not available (might need a new screen/error messages)
        if(singleSpinner != null){
            tempReservation.getRooms().clear();
            int numSingle = singleSpinner.getValue();
            int numDouble = doubleSpinner.getValue();
            int numPen = pentSpinner.getValue();
            int numDeluxe = deluxeSpinner.getValue();

            List<Room> openRooms;
            if(numSingle != 0){
                openRooms = _reservationService.findAvailableRoom(checkInDate, checkOutDate, RoomType.SINGLE);
                if(openRooms != null && openRooms.size() >= numSingle){
                    for(int i = 0; i < numSingle; i++){
                        tempReservation.addRoom(openRooms.get(i));
                    }
                }
            }
            if(numDouble != 0) {
                openRooms = _reservationService.findAvailableRoom(checkInDate, checkOutDate, RoomType.DOUBLE);
                if (openRooms != null && openRooms.size() >= numDouble) {
                    for (int i = 0; i < numDouble; i++) {
                        tempReservation.addRoom(openRooms.get(i));
                    }
                }
            }
            if(numPen != 0) {
                openRooms = _reservationService.findAvailableRoom(checkInDate, checkOutDate, RoomType.PENTHOUSE);
                if (openRooms != null && openRooms.size() >= numPen) {
                    for (int i = 0; i < numPen; i++) {
                        tempReservation.addRoom(openRooms.get(i));
                    }
                }
            }
            if(numDeluxe != 0) {
                openRooms = _reservationService.findAvailableRoom(checkInDate, checkOutDate, RoomType.DELUXE);
                if (openRooms != null && openRooms.size() >= numDeluxe) {
                    for (int i = 0; i < numDeluxe; i++) {
                        tempReservation.addRoom(openRooms.get(i));
                    }
                }
            }
            suggestionSuccessLbl.setVisible(false);
        }
        tempGuest = null;
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskGuestDetails.fxml", null);
    }

    @FXML
    private void toAddonsPress() throws IOException {
        if(nameLbl != null && tempGuest == null){
            tempGuest = confirmLoyalty();
            if(tempGuest == null){
                return;
            }
        }
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskAddonSelect.fxml", null);
    }

    private Guest confirmLoyalty(){
        //Check that loyalty value is an integer value
        int loyaltyNumber = -1;
        name = nameLbl.getText();
        phone = phoneLbl.getText();
        email = emailLbl.getText();
        country = countryLbl.getText(); // added missing guest country info

        Guest temporaryGuest = new Guest(name, phone, email, country);
        if(loyaltyLbl.getText().isBlank()){ //If there's no loyalty value
            return temporaryGuest;
        }
        try{
            loyaltyNumber = Integer.parseInt(loyaltyLbl.getText());
        }catch (NumberFormatException e){
            loyaltyErr.setText("Not a number");
            loyaltyErr.setVisible(true);
            return null;
        }
        if(loyaltyTxt.isDisabled()){ // they clicked sign up, so make the guest a new loyalty member
            temporaryGuest.makeLoyaltyMember(loyaltyNumber);
            return temporaryGuest;
        }
        return null;
    }

    @FXML
    private void nextSlidePress(){
        imageIndex = (imageIndex + 1) % 10;
        diagramImage.setImage(imageList.get(imageIndex));
        diagramLbl.setText(imageText.get(imageIndex));
    }

    @FXML
    private void prevSlidePress(){
        imageIndex -= 1;
        if(imageIndex < 0){
            imageIndex = 8;
        }
        diagramImage.setImage(imageList.get(imageIndex));
        diagramLbl.setText(imageText.get(imageIndex));
    }

    @FXML
    private void toBillingPress() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskBillEstimate.fxml", null);
    }

    @FXML
    private void toHome() throws IOException{
        sceneManager.switchScene("/ca/senecacollege/hotel/application/Welcome.fxml", null);
    }

    @FXML
    private void toConfirmationPress() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskConfirm.fxml", null);
    }

    @FXML
    private void toFinalConfirmationPress() throws IOException {
        _reservationService.saveNewReservation(tempReservation);
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskFinalConfirm.fxml", null);
    }

    @FXML
    private void toKiosk() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskGuestCount.fxml", null);
    }

    @FXML
    public void rulesAndRegDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecacollege/hotel/application/RulesAndReg.fxml"));
        DialogPane dialogPane = loader.load();
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Rules and Regulations");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setDialogPane(dialogPane);
        dialog.show();
    }

    //</editor-fold>

    @FXML
    private void suggestRooms(){
        List<RoomSet> suggestion = _reservationService.getRoomSuggestion(numAdult, numChildren);
        deluxeSpinner.getValueFactory().setValue(0);
        pentSpinner.getValueFactory().setValue(0);
        for (RoomSet rm : suggestion){
            if (rm.getType() == RoomType.DOUBLE){
                doubleSpinner.getValueFactory().setValue(rm.getQuantity());
            } else {
                singleSpinner.getValueFactory().setValue(rm.getQuantity());
            }
        }
        suggestionSuccessLbl.setVisible(true);
    }

    @FXML
    private void onSignupClick(){
        if (!loyaltyTxt.isDisabled()) { // if they haven't clicked signup already, disables the textbox and gets a loyalty number for them
            int loyaltyNum = _loyaltyService.getNewLoyaltyNum();
            loyaltyLbl.setText(String.valueOf(loyaltyNum));
            loyaltyTxt.setText(String.valueOf(loyaltyNum));
            loyaltyTxt.setDisable(true);
            signupBtn.setText("Cancel Sign up");
        } else {
            signupBtn.setText("Click to Sign up");
            loyaltyTxt.setDisable(false);
            loyaltyTxt.setText("");
            loyaltyLbl.setText("");
        }
    }

}
