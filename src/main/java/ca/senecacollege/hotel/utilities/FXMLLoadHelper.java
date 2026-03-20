package ca.senecacollege.hotel.utilities;

import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class FXMLLoadHelper {
    public FXMLLoadHelper(){}

    public static FXMLLoadResult loadWithSceneManagerController(String fxmlPath, Injector injector, SceneManager sceneManager) throws IOException, IOException {
        URL resource = FXMLLoadHelper.class.getResource(fxmlPath);
        if(resource == null){
            throw new IllegalArgumentException("Resource not found: " + fxmlPath);
        }
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(injector::getInstance);
        Parent root = loader.load();
        Object controller = loader.getController();
        if(controller != null && sceneManager != null && controller instanceof SceneManagerAware sma){
            sma.setSceneManager(sceneManager);
        }
        return new FXMLLoadResult(root, controller);

    }

}
