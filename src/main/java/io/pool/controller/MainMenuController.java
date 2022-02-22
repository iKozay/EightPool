package io.pool.controller;

import io.pool.view.MainMenuView;
import io.pool.view.SettingsView;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainMenuController {

    private static MainMenuView mmv;
    private static Stage stage;
    private SettingsView settingsView;
    private Pane gameView;
    private static PoolCueController pcc;

    public MainMenuController(MainMenuView mmv,Stage stage) {
        this.mmv = mmv;
        this.stage = stage;
        mmv.setController(this);
    }

    public static void gotoMainMenu(){
        stage.getScene().setRoot(mmv);
        pcc.resetEventHandler(stage.getScene());
    }
    public void gotoSettings(){
        stage.getScene().setRoot(settingsView.getNewLoadedPane());
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


    public void setSettingsView(SettingsView settingsView) {
        this.settingsView = settingsView;
    }

    public void setGameView(Pane root, PoolCueController pcc) {
        this.gameView = root;
        this.pcc = pcc;
        mmv.getP1().setOnMouseClicked(event -> {
            stage.getScene().setRoot(gameView);
            pcc.handleRotateCue(stage.getScene());
            pcc.hit(stage.getScene());
        });
    }
}
