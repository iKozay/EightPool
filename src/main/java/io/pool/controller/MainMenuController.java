package io.pool.controller;

import io.pool.Database.DBConnection;
import io.pool.model.GameModel;
import io.pool.model.PlayerModel;
import io.pool.view.GameView;
import io.pool.view.MainMenuView;
import io.pool.view.PoolCueView;
import io.pool.view.SettingsView;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;

public class MainMenuController {

    private static MainMenuView mmv;
    private static Stage stage;
    private SettingsView settingsView;
    private static GameView gameView;

    public MainMenuController(MainMenuView mmv,Stage stage) {
        this.mmv = mmv;
        this.stage = stage;
        this.settingsView = new SettingsView();
        mmv.setController(this);
        try {
            solo1Action();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        pvAIAction();
        pvpAction();
    }

    public static void gotoMainMenu(){
        if(stage.getScene().getRoot().equals(gameView)) {
            gameView.getGameController().getPoolCueController().resetEventHandler(stage.getScene());
            gameView.getGameController().resetGame();
        }
        stage.getScene().setRoot(mmv);
    }
    public void gotoSettings(){
        stage.getScene().setRoot(settingsView);
    }

    public void pvpAction(){
        mmv.getPvp1Btn().setOnMouseExited(event -> {
            if(mmv.getChildren().contains(mmv.getPvp1Btn())){
                mmv.getChildren().remove(mmv.getPvp1Btn());
                mmv.getPvpText().setText("P\n  v\n    P");
            }
        });

        mmv.getP2().hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                mmv.getChildren().add(mmv.getPvp1Btn());
                mmv.getPvpText().setText("PLAY");
            }
        });
        mmv.getPVPstartBtn().setOnAction(e->{
            stage.getScene().setRoot(gameView);
            this.gameView.getGameController().getPoolCueController().handleRotateCue(stage.getScene());
            this.gameView.getGameController().getPoolCueController().hit(stage.getScene());
            try {
                gameView.getGameController().startGame(1);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }

        });
        mmv.getPvp1Btn().setOnAction(event -> {
            stage.getScene().setRoot(mmv.getPvpRootMenu());
        });


    }


    public void pvAIAction(){
        mmv.getButtonGroup().setOnMouseExited(event -> {
            if(mmv.getChildren().contains(mmv.getButtonGroup())){
                mmv.getChildren().remove(mmv.getButtonGroup());
                mmv.getVersusAIText().setText("CHALLENGE\n    THE AI");
            }
        });

        mmv.getP3().hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                mmv.getChildren().add(mmv.getButtonGroup());
                mmv.getVersusAIText().setText("");
            }
        });
        for (int i = 0;i<3;i++){

            int finalI = i;
            mmv.getButtonGroup().getChildren().get(i).hoverProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue){
                    mmv.getButtonGroup().getChildren().get(finalI).setStyle("-fx-background-color: rgba(255,255,255,.7)");
                }
            });

            int finalI1 = i;
            mmv.getButtonGroup().getChildren().get(i).setOnMouseExited(event -> {
                mmv.getButtonGroup().getChildren().get(finalI1).setStyle("-fx-background-color: rgba(255,255,255,.3)");
            });
        }

    }

    GameModel gameModel = new GameModel();
    public void solo1Action() throws MalformedURLException {
        gameView = new GameView();
        mmv.getSolo1Btn().setOnMouseClicked(event -> {
            stage.getScene().setRoot(gameView);
            this.gameView.getGameController().getPoolCueController().handleRotateCue(stage.getScene());
            this.gameView.getGameController().getPoolCueController().hit(stage.getScene());
            try {
                gameView.getGameController().startGame(0);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //gameModel.setGameType(1);
            //DBConnection.instantiateLastLayoutDB(gameModel.getGameType(), new PlayerModel("Test"), new PlayerModel());

            gameModel.setGameType(1);
            //DBConnection.instantiateLastLayoutDB(gameModel.getGameType(), new PlayerModel("Test"), new PlayerModel());
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
}
