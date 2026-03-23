module ca.senecacollege.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires com.google.guice;
    requires java.sql;
    requires mysql.connector.j;
    requires org.hibernate.orm.core;
    requires org.checkerframework.checker.qual;

    opens ca.senecacollege.hotel.application to javafx.fxml;
    exports ca.senecacollege.hotel.application;
    exports ca.senecacollege.hotel.controllers;
    opens ca.senecacollege.hotel.controllers to javafx.fxml;
    opens ca.senecacollege.hotel.models;
    opens ca.senecacollege.hotel.services to com.google.guice;
    exports ca.senecacollege.hotel.services;
    exports ca.senecacollege.hotel.utilities;
    exports ca.senecacollege.hotel.tests;
}