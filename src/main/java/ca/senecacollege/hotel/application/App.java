package ca.senecacollege.hotel.application;

import ca.senecacollege.hotel.tests.DBTester;
import ca.senecacollege.hotel.utilities.AppModule;
import ca.senecacollege.hotel.utilities.FXMLLoadHelper;
import ca.senecacollege.hotel.utilities.FXMLLoadResult;
import ca.senecacollege.hotel.utilities.SceneManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.persistence.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;



//TODO Set the initial screen to the actual initial screen (Currently stating on kiosk for testing)
public class App extends Application {
    private static Injector injector;

    @Override
    public void start(Stage stage) throws Exception {
        injector = Guice.createInjector(new AppModule());

//        onInit();

        SceneManager sceneManager = new SceneManager(stage, injector);
        FXMLLoadResult result = FXMLLoadHelper.loadWithSceneManagerController("/ca/senecacollege/hotel/application/Welcome.fxml", injector, sceneManager);
        stage.setScene(new Scene(result.root));
        stage.setTitle("Hello!");
        stage.show();

    }

    @Override
    public void stop() throws Exception {
        EntityManagerFactory emf = injector.getInstance(EntityManagerFactory.class);
        if(emf.isOpen()){
            emf.close();
        }
    }

    private void onInit(){
        DBTester tester = injector.getInstance(DBTester.class);
        tester.makeDB();
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}

