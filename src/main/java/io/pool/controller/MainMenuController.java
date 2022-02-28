package io.pool.controller;

import io.pool.view.GameView;
import io.pool.view.MainMenuView;
import io.pool.view.SettingsView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.MalformedURLException;

public class MainMenuController {

    private static MainMenuView mmv;
    private static Stage stage;
    private SettingsView settingsView;
    private static GameView gameView;

    public MainMenuController(MainMenuView mmv,Stage stage) {
        this.mmv = mmv;
        this.stage = stage;
        mmv.setController(this);
    }

    public static void gotoMainMenu(){
        stage.getScene().setRoot(mmv);
        gameView.getGameController().getPoolCueController().resetEventHandler(stage.getScene());
        gameView.getGameController().resetGame();
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

    public void solo1Action(){

        mmv.getSolo1Btn().setOnMouseClicked(event -> {
            stage.getScene().setRoot(gameView);
            this.gameView.getGameController().getPoolCueController().handleRotateCue(stage.getScene());
            this.gameView.getGameController().getPoolCueController().hit(stage.getScene());
            try {
                gameView.getGameController().startGame();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });


        mmv.getSolo1Btn().setOnMouseExited(event -> {
            if(mmv.getChildren().contains(mmv.getSolo1Btn())){
                mmv.getChildren().remove(mmv.getSolo1Btn());
                //mmv.getSoloText().setScaleX(0.75);
                //mmv.getSoloText().setScaleY(0.75);
                mmv.getSoloText().setText("SOLO");
            }
        });
        mmv.getP1().hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                System.out.println("Hovering");
                //mmv.getSoloText().setScaleX(1.25);
                //mmv.getSoloText().setScaleY(1.25);
                mmv.getSoloText().setText("PLAY");
                mmv.getChildren().add(mmv.getSolo1Btn());
            }
        });
    }




    public void setSettingsView(SettingsView settingsView) {
        this.settingsView = settingsView;
    }

    public void setGameView(GameView root) {
        this.gameView = root;
        solo1Action();

    }
}
