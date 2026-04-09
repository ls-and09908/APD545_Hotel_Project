package ca.senecacollege.hotel.controllers;

import ca.senecacollege.hotel.models.LoyaltyTransaction;
import ca.senecacollege.hotel.services.ILoyaltyService;
import ca.senecacollege.hotel.utilities.SceneManager;
import ca.senecacollege.hotel.utilities.SceneManagerAware;
import com.google.inject.Inject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class LoyaltyController implements SceneManagerAware {
    private final ILoyaltyService _loyaltyService;
    private final DateTimeFormatter dateFormat;
    private ObservableList<LoyaltyTransaction> allTransactions = FXCollections.observableArrayList();
    private FilteredList<LoyaltyTransaction> filteredData;
    SceneManager sceneManager;

    @Inject
    public LoyaltyController(ILoyaltyService loyaltyService) {
        this._loyaltyService = loyaltyService;
        dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    @FXML
    public void initialize(){
        allTransactions.setAll(_loyaltyService.getLoyaltyTransactions());
        filteredData = new FilteredList<>(allTransactions, p -> true);
        SortedList<LoyaltyTransaction> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(loyaltyTable.comparatorProperty());
        loyaltyTable.setItems(sortedData);
        dateFrom.setValue(LocalDate.now().withDayOfMonth(1));
        dateTo.setValue(YearMonth.now().atEndOfMonth());

        dateTo.valueProperty().addListener(((observable, oldValue, newValue) -> {
            checkFilter();
        }));
        dateFrom.valueProperty().addListener(((observable, oldValue, newValue) -> {
            checkFilter();
        }));

        loyaltyField.textProperty().addListener(((observable, oldValue, newValue) -> {
            checkFilter();
        }));

        paymentCheck.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            checkFilter();
        }));

        refundCheck.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            checkFilter();
        }));

        redeemCheck.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            checkFilter();
        }));

        dateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTime().format(dateFormat)));
        guestCol.setCellValueFactory(cellData->
                new SimpleIntegerProperty(cellData.getValue().getGuest().getLoyaltyNum()).asObject());
        typeCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTransactionType()));
        pointCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getTransactionPoints()).asObject());
        balanceCol.setCellValueFactory(cellData->
                new SimpleIntegerProperty(cellData.getValue().getNewBalance()).asObject());


    }

    @FXML
    private void toDash() throws IOException {
        sceneManager.switchScene("/ca/senecacollege/hotel/application/AdminDashboard.fxml", null);
    }

    private void checkFilter(){
        LocalDate start = dateFrom.getValue();
        LocalDate end = dateTo.getValue();
        String searchTxt = loyaltyField.getText();
        boolean payment = paymentCheck.isSelected();
        boolean refund = refundCheck.isSelected();
        boolean redeem = redeemCheck.isSelected();

        filteredData.setPredicate(lt -> {
            LocalDateTime time = lt.getTime();
            boolean date = (!time.isBefore(start.atStartOfDay())) && !time.isAfter(end.atStartOfDay());

            boolean loyaltyNum = true;
            if (searchTxt != null && !searchTxt.isEmpty()) {
                loyaltyNum = String.valueOf(lt.getGuest().getLoyaltyNum())
                        .contains(searchTxt);
            }

            boolean type = true;
            if ((!payment && !refund && !redeem)){
                type = true;
            } else {
                type = false;

                if(payment && lt.getTransactionType() == "earn"){
                    type = true;
                }
                if(refund && lt.getTransactionType() == "refund"){
                    type = true;
                }
                if(redeem && lt.getTransactionType() == "redeem"){
                    type = true;
                }
            }
            return date && type && loyaltyNum;
        });
    }

    @FXML
    private TextField loyaltyField;
    @FXML
    private CheckBox paymentCheck;
    @FXML
    private CheckBox refundCheck;
    @FXML
    private CheckBox redeemCheck;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML private
    TableView<LoyaltyTransaction> loyaltyTable;
    @FXML private
        TableColumn<LoyaltyTransaction, String> dateCol;
    @FXML private
        TableColumn<LoyaltyTransaction, Integer> guestCol;
    @FXML private
        TableColumn<LoyaltyTransaction, String> typeCol;
    @FXML private
        TableColumn<LoyaltyTransaction, Integer> pointCol;
    @FXML private
        TableColumn<LoyaltyTransaction, Integer> balanceCol;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
