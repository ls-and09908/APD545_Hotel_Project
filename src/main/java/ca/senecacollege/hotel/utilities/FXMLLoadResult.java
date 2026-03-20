package ca.senecacollege.hotel.utilities;

import javafx.scene.Parent;

public class FXMLLoadResult {
    public final Parent root;
    public final Object controller;

    public FXMLLoadResult(Parent root, Object controller){
        this.root = root;
        this.controller = controller;
    }
}
