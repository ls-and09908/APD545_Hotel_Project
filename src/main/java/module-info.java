module ca.senecacollege.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires com.google.guice;
    requires java.sql;
    requires mysql.connector.j;
    requires org.hibernate.orm.core;


    opens ca.senecacollege.hotel.application to javafx.fxml;
    exports ca.senecacollege.hotel.application;
    exports ca.senecacollege.hotel.controllers;
    exports ca.senecacollege.hotel.services to com.google.guice;
    opens ca.senecacollege.hotel.controllers to javafx.fxml;
    opens ca.senecacollege.hotel.models to org.hibernate.orm.core, net.bytebuddy, jakarta.persistence;
}