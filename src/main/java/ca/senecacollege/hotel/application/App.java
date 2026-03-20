package ca.senecacollege.hotel.application;

import ca.senecacollege.hotel.models.AdminUser;
import jakarta.persistence.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //onInit();

        FXMLLoader loader = new FXMLLoader(App.class.getResource("Other.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Hello!");
        stage.show();
    }

    private void onInit(){
        // DB testing, uncomment it out from start when you don't have mysql running
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hotel-persistence-unit");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        AdminUser test1 = new AdminUser();
        test1.setUsername("Sttco");

        em.persist(test1);
        em.getTransaction().commit();

        em.close();
        emf.close();
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}

