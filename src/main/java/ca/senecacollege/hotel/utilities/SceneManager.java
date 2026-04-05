package ca.senecacollege.hotel.utilities;

import com.google.inject.Injector;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class SceneManager {
    public final Stage stage;
    public final Injector injector;


    public SceneManager(Stage stage, Injector injector){
        this.stage = stage;
        this.injector = injector;
    }

    public <T> void switchScene(String fxmlPath, Consumer<T> controllerConsumer) throws IOException {
        FXMLLoadResult result = FXMLLoadHelper.loadWithSceneManagerController(fxmlPath, injector, this);
        if(controllerConsumer!=null)
            controllerConsumer.accept((T) result.controller);
        stage.setScene(new Scene(result.root));
        stage.show();
    }

}