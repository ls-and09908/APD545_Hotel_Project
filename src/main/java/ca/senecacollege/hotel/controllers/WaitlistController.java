package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.Guest;
import ca.senecacollege.hotel.models.Waitlist;
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
import javafx.stage.Stage;

import java.awt.*;
import java.time.LocalDate;

public class WaitlistController {
    private IWaitlistService waitService;

    public void setWaitService(IWaitlistService waitService){
        this.waitService = waitService;
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
        int adult = Integer.parseInt(adultText.getText());
        int child = Integer.parseInt(childText.getText());
        LocalDate in = inDate.getValue();
        LocalDate out = outDate.getValue();
        if(! name.isBlank() && ! phone.isBlank() && ! email.isBlank() && ! country.isBlank() && adult>0 && child>=0 && in.isAfter(LocalDate.now()) && out.isAfter(in)){
            Waitlist newWaitlist = new Waitlist(new Guest(name,phone,email,country), adult, child, in, out);
            waitService.saveWaitlistRes(newWaitlist);
        }
        setTables();
    }

    @FXML private void removeFromWaitlist(){
        Waitlist selectedWaitlist = (Waitlist) waitlistTable.getSelectionModel().getSelectedItem();
        waitService.removeWaitlist(selectedWaitlist);
        setTables();
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

}
