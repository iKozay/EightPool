package io.pool.controller;

import io.pool.view.MainMenuView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    private MainMenuView mmv;
    private Stage stage;

    public MainMenuController(MainMenuView mmv,Stage stage) {
        this.mmv = mmv;
        this.stage = stage;
    }



    public void soloAction(Scene scene){
        mmv.getP1().setOnMouseClicked(event -> {
            stage.setScene(scene);
        });
    }

    public void pvpAction(Scene scene){
        mmv.getP2().setOnMouseClicked(event -> {
            System.out.println("Clicked");
            scene.getRoot().getChildrenUnmodifiable().remove(mmv.getP2());
        });
    }

    public void pvAIAction(Scene scene){
        mmv.getP3().setOnMouseClicked(event -> {
            stage.setScene(scene);
        });
    }



}
