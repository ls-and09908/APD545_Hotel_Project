package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.models.Reservation;
import ca.senecacollege.hotel.models.Waitlist;
import ca.senecacollege.hotel.services.IReservationService;
import ca.senecacollege.hotel.services.IWaitlistService;
import com.google.inject.Inject;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.time.LocalDate;

public class WaitlistController {
    private IWaitlistService waitService;
    private IReservationService resService;
    private AdminController ac;

    public void setWaitService(IWaitlistService waitService, IReservationService resService){
        this.waitService = waitService;
        this.resService = resService;
        setCombo();
        setTables();
    }


    private void setCombo(){
        ObservableList<String> countries = FXCollections.observableArrayList("Canada", "USA", "China", "Indonesia", "Vietnam", "Other");
        countryCombo.setItems(countries);
    }

    private void setTables(){
        ObservableList<Waitlist> allWaitlist = waitService.getAllWaitlist();
        guestCol.setCellValueFactory(w -> new SimpleObjectProperty<>(w.getValue().getGuest().getName()));
        adultCol.setCellValueFactory(w -> new SimpleObjectProperty<>(w.getValue().getAdults()));
        childCol.setCellValueFactory(w -> new SimpleObjectProperty<>(w.getValue().getChildren()));
        inCOl.setCellValueFactory(w -> new SimpleObjectProperty<>(w.getValue().getCheckIn()));
        outCol.setCellValueFactory(w -> new SimpleObjectProperty<>(w.getValue().getCheckOut()));
        waitlistTable.setItems(allWaitlist);
    }

    @FXML
    public void closeDialog(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveNewWaitlist(){
        String name = nameText.getText();
        String phone = phoneText.getText();
        String email = emailText.getText();
        String country = countryCombo.getValue();
        int adult = -1;
        int child = -1;
        try{
            adult = Integer.parseInt(adultText.getText());
        } catch (Exception e){
            errLbl.setText("Adult must be number");
            errLbl.setVisible(true);
            return;
        }
        try {
            child = Integer.parseInt(adultText.getText());
        }catch (Exception e){
            errLbl.setText("Child must be number");
            errLbl.setVisible(true);
            return;
        }
        LocalDate in = null;
        if(inDate.getValue() != null){
            in = inDate.getValue();
        }
        LocalDate out = null;
        if(outDate.getValue() != null) {
            out = outDate.getValue();
        }
        if(name.isBlank()){
            errLbl.setText("Name not set");
            errLbl.setVisible(true);
            return;
        }
        else if(phone.isBlank() || ! phone.matches("\\d*")){
            errLbl.setText("Phone must be numeric value");
            errLbl.setVisible(true);
            return;
        }
        else if(email.isBlank()){
            errLbl.setText("Must have email");
            errLbl.setVisible(true);
            return;
        }
        else if(country == null){
            errLbl.setText("Country must be set");
            errLbl.setVisible(true);
            return;
        }
        else if(adult <= 0){
            errLbl.setText("Must have at least 1 adult");
            errLbl.setVisible(true);
            return;
        }
        else if(child < 0){
            errLbl.setText("Cannot have negative people");
            errLbl.setVisible(true);
            return;
        }
        else if(in == null || in.isBefore(LocalDate.now())){
            errLbl.setText("Must book after today");
            errLbl.setVisible(true);
            return;
        }
        else if(out == null || out.isBefore(in)){
            errLbl.setText("Must check out after in");
            errLbl.setVisible(true);
            return;
        }
        else{
            Waitlist newWaitlist = new Waitlist(new Guest(name,phone,email,country), adult, child, in, out);
            waitService.saveWaitlistRes(newWaitlist);
        }
        setTables();
    }

    @FXML
    private void makeResFromWaitlist(){
        Waitlist selectedWaitlist = (Waitlist) waitlistTable.getSelectionModel().getSelectedItem();
        Guest waitingGuest = selectedWaitlist.getGuest();
        Reservation newRes = new Reservation(waitingGuest, selectedWaitlist.getAdults(), selectedWaitlist.getChildren(), selectedWaitlist.getCheckIn(), selectedWaitlist.getCheckOut());
        resService.saveReservation(newRes);
        waitService.removeWaitlist(selectedWaitlist);
        setTables();
        ac.refreshTable();
    }

    @FXML
    private void removeFromWaitlist(){
        Waitlist selectedWaitlist = (Waitlist) waitlistTable.getSelectionModel().getSelectedItem();
        waitService.removeWaitlist(selectedWaitlist);
        setTables();
    }

    public void setAdminControl(AdminController ac){
        this.ac = ac;
    }

    @FXML
    TextField nameText;
    @FXML
    TextField emailText;
    @FXML
    TextField phoneText;
    @FXML
    ComboBox<String> countryCombo;
    @FXML
    TextField adultText;
    @FXML
    TextField childText;
    @FXML
    DatePicker outDate;
    @FXML
    DatePicker inDate;
    @FXML
    TableView waitlistTable;
    @FXML
    TableColumn<Waitlist, String> guestCol;
    @FXML
    TableColumn<Waitlist, Integer> adultCol;
    @FXML
    TableColumn<Waitlist, Integer> childCol;
    @FXML
    TableColumn<Waitlist, LocalDate> inCOl;
    @FXML
    TableColumn<Waitlist, LocalDate> outCol;
    @FXML
    Button submitBtn;
    @FXML
    Button removeBtn;
    @FXML
    Label errLbl;

}
