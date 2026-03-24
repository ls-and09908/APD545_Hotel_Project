package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.application.App;
import ca.senecacollege.hotel.models.*;
import ca.senecacollege.hotel.services.BillingService;
import ca.senecacollege.hotel.services.LoyaltyService;
import ca.senecacollege.hotel.services.ReservationService;
import ca.senecacollege.hotel.services.RoomFactory;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

//TODO Connect kiosk elements to functions
//TODO Create kiosk functionality
//TODO implement proper repositories
public class KioskController implements SceneManagerAware {
    private SceneManager sceneManager;
    private Reservation tempReservation;
    private Guest tempGuest;
    private static double finalCost;
    private static int numAdult = 0;
    private static int numChildren = 0;
    private static LocalDate checkInDate;
    private static LocalDate checkOutDate;
    private static String name;
    private static String phone;
    private static String email;
    private static String country;
    private static ArrayList<String> addonNames = new ArrayList<>();
    private static double displayAddons = 0.00;
    private static ArrayList<Room> roomList = new ArrayList<>();
    private BillingService _billingService;
    private LoyaltyService _loyaltyService;
    private ReservationService _reservationService;

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
    Button roomSelectNextBtn;
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
    private Text billBaseCost;
    @FXML
    private Text billAddonCost;
    @FXML
    private Text billSeasonalCost;
    @FXML
    private Text billDiscountCost;
    @FXML
    private Text billTotalCost;
    @FXML
    private Text billCustomerName;
    @FXML
    private Text billCardNumber;
    @FXML
    private Text billRemainingBalance;
    @FXML
    private Text billDepositCost;
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
    private void initialize(){
        disableNextButtons();
        if(billBaseCost != null){
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
    }

    private void setBillingData(){
        tempReservation = new Reservation(tempGuest, numAdult, numChildren, checkInDate, checkOutDate);
        for(Room i: roomList){
            tempReservation.addRoom(i);
        }
        String desc;
        double addCost = 0.00;
        boolean nightly;
        for(String i: addonNames){
            if(i.equals("Wifi")) {
                desc = "Guest has access to hotel wifi during their stay";
                addCost = 25;
                displayAddons += addCost;
                nightly = false;
            }
            else if(i.equals("Breakfast")) {
                desc = "Guest gets breakfast";
                addCost = 50;
                displayAddons += (addCost * ChronoUnit.DAYS.between(checkInDate, checkOutDate));
                nightly = true;
            }
            else if(i.equals("Spa")) {
                desc = "Guest gets access to spa";
                addCost = 80;
                displayAddons += addCost;
                nightly = false;
            }
            else{
                desc = "Guest gets daily parking";
                addCost = 5;
                displayAddons += (addCost * ChronoUnit.DAYS.between(checkInDate, checkOutDate));
                nightly = true;
            }
            tempReservation.addAddOn(new AddOn(i, desc, addCost, nightly));
        }
        _billingService.generateBill(tempReservation);
    }

    private void setBillingDisplay(){
        finalCost = tempReservation.getBilling().getTotalCharges();
        double deposit = 500.00 - tempReservation.getBilling().getTotalPayments();
        if(deposit < 0.00){
            deposit = 0.00;
        }
        billBaseCost.setText(" $" + finalCost);
        billDepositCost.setText(" $" + deposit);
        billRemainingBalance.setText(" $" + (tempReservation.getBilling().getTotalCharges() - tempReservation.getBilling().getTotalPayments()));
        billDiscountCost.setText(" N/A");
        billSeasonalCost.setText(" N/A");
        billCardNumber.setText(" N/A");
        billTotalCost.setText(" $" + tempReservation.getBilling().getTotalCharges());
        billCustomerName.setText(" " + name);
        billAddonCost.setText(" $" + displayAddons);
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
        singleSpinner.setValueFactory(svalueFactory);
        doubleSpinner.setValueFactory(dvalueFactory);
        pentSpinner.setValueFactory(pvalueFactory);
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
            roomSelectNextBtn.disableProperty().bind(singleSpinner.valueProperty().isEqualTo(0).and(doubleSpinner.valueProperty().isEqualTo(0)).and(pentSpinner.valueProperty().isEqualTo(0)));
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
        if(! emailTxt.getText().isEmpty()){
            emailLbl.setText(emailTxt.getText());
            emailLbl.setVisible(true);
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
        if(! loyaltyTxt.getText().isEmpty()){
            loyaltyLbl.setText(loyaltyTxt.getText());
            loyaltyLbl.setVisible(true);
        }
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
            dateScreenErr.setText("Error: In after out");
            dateScreenErr.setVisible(true);
            return false;
        }
        else if(inDate.getValue().isBefore(LocalDate.now()) || outDate.getValue().isBefore(LocalDate.now())){
            dateScreenErr.setText("Error: Must check IN on or after today");
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
        if(singleSpinner != null){
            int numSingle = singleSpinner.getValue();
            int numDouble = doubleSpinner.getValue();
            int numPen = pentSpinner.getValue();
            for(int i = 0; i < numSingle; i++){
                roomList.add(RoomFactory.createRoom(01, RoomType.SINGLE));
            }
            for(int i = 0; i < numDouble; i++){
                roomList.add(RoomFactory.createRoom(01, RoomType.DOUBLE));
            }
            for(int i = 0; i < numPen; i++){
                roomList.add(RoomFactory.createRoom(01, RoomType.PENTHOUSE));
            }
        }
        sceneManager.switchScene("/ca/senecacollege/hotel/application/KioskGuestDetails.fxml", null);
    }

    @FXML
    private void toAddonsPress() throws IOException {
        if(nameLbl != null){
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
        if(loyaltyLbl.getText().isBlank()){ //If there's no loyalty value we construct the guest without it
            return new Guest(nameLbl.getText(), phoneLbl.getText(), emailLbl.getText());
        }
        try{
            loyaltyNumber = Integer.parseInt(loyaltyLbl.getText());
        }catch (NumberFormatException e){
            loyaltyErr.setText("Not a number");
            loyaltyErr.setVisible(true);
            return null;
        }
        Guest temporaryGuest = new Guest(name, phone, email, loyaltyNumber);
        if(_loyaltyService.findLoyalGuest(temporaryGuest)){
            return temporaryGuest; //The guest has a loyalty number and it has been confirmed
        }
        loyaltyErr.setText("Loyalty not found");
        loyaltyErr.setVisible(true);

        return null;
    }

    @FXML
    private void toBillingPress() throws IOException {
        if(wifiBox != null){
            if(wifiBox.isSelected()){
               addonNames.add("Wifi");
            }
            if(breakfastBox.isSelected()){
                addonNames.add("Breakfast");
            }
            if(spaBox.isSelected()){
                addonNames.add("Spa");
            }
            if(parkingBox.isSelected()){
                addonNames.add("Parking");
            }
        }
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
        _reservationService.saveReservation(tempReservation);
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

}
