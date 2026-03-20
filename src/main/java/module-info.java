module ca.senecacollege.application {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.senecacollege.hotel.application to javafx.fxml;
    exports ca.senecacollege.hotel.application;
    exports ca.senecacollege.hotel.controllers;
    opens ca.senecacollege.hotel.controllers to javafx.fxml;
}