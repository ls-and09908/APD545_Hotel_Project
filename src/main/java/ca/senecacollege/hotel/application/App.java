package ca.senecacollege.hotel.application;

import ca.senecacollege.hotel.models.AdminUser;
import ca.senecacollege.hotel.utilities.AppModule;
import ca.senecacollege.hotel.utilities.FXMLLoadHelper;
import ca.senecacollege.hotel.utilities.FXMLLoadResult;
import ca.senecacollege.hotel.utilities.SceneManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.persistence.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


//TODO Set the initial scren to the actual initial screen (Currently stating on kiosk for testing)
public class App extends Application {
    private static Injector injector;

    @Override
    public void start(Stage stage) throws Exception {
        //onInit();

        injector = Guice.createInjector(new AppModule()); //Might need stage
        SceneManager sceneManager = new SceneManager(stage, injector);
        FXMLLoadResult result = FXMLLoadHelper.loadWithSceneManagerController("/ca/senecacollege/hotel/application/KioskGuestCount.fxml", injector, sceneManager);
        stage.setScene(new Scene(result.root));
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

