package ca.senecacollege.hotel.application;

import ca.senecacollege.hotel.utilities.*;
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
import java.io.InputStream;
import java.util.Properties;


public class App extends Application {
    private static Injector injector;

    @Override
    public void start(Stage stage) throws Exception {
        injector = Guice.createInjector(new AppModule());

        initDatabase();

        SceneManager sceneManager = new SceneManager(stage, injector);
        FXMLLoadResult result = FXMLLoadHelper.loadWithSceneManagerController("/ca/senecacollege/hotel/application/Welcome.fxml", injector, sceneManager);
        stage.setScene(new Scene(result.root));
        stage.setTitle("APD545 Hotel Reservation Service");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        EntityManagerFactory emf = injector.getInstance(EntityManagerFactory.class);
        if(emf.isOpen()){
            emf.close();
        }
    }

    /**
     * Initializes the database with test data if the schema generation mode is set to create in application.properties config file.
     * Makes startup slow, so only use create mode when you need fresh data for testing Or if you accidentally corrupted the DB.
     */
    private void initDatabase(){
        DBInitializer initializer = injector.getInstance(DBInitializer.class);
        Properties config = new Properties();
        try(InputStream in = DBInitializer.class
                .getClassLoader()
                .getResourceAsStream("application.properties")){
            if(in!=null){
                config.load(in);
                String schemaGenMode = config.getProperty("hibernate.hbm2ddl.auto");
                if(schemaGenMode.equals("create")){
                    initializer.makeDB();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}

