package io.pool.controller;

import io.pool.Database.BallConfigurationDB;
import io.pool.model.PlayerModel;
import io.pool.view.GameView;
import io.pool.view.MainMenuView;
import io.pool.view.SettingsView;
import io.pool.view.aboutView;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

public class MainMenuController {

    private static MainMenuView mmv;
    private static Stage stage;
    private aboutView aboutView;
    private SettingsView settingsView;
    private static GameView gameView;

    public MainMenuController(MainMenuView mmv,Stage stage) {
        this.mmv = mmv;
        this.stage = stage;
        this.settingsView = new SettingsView();
        this.aboutView = new aboutView();
        mmv.setController(this);
        solo1Action();
        pvAIAction();
        pvpAction();
    }

    public static void gotoMainMenu(){
        if(stage.getScene().getRoot().equals(gameView)) {
            gameView.getGameController().getPoolCueController().resetPoolCue();
            gameView.getGameController().resetGame(false);
        }
        stage.getScene().setRoot(mmv);
    }
    public void gotoSettings(){
        stage.getScene().setRoot(settingsView);
        settingsView.getProfilesTable().refresh();
    }
    public void gotoHelp() {
        stage.getScene().setRoot(this.aboutView);
    }


    public void pvpAction(){
        mmv.getPvp1Btn().setOnMouseExited(event -> {
            if(mmv.getSecondRowPane().getChildren().contains(mmv.getPvp1Btn())){
                mmv.getSecondRowPane().getChildren().remove(mmv.getPvp1Btn());
                mmv.getPvpText().setText("P\n  v\n    P");
            }
        });

        mmv.getP2().hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                if(!mmv.getSecondRowPane().getChildren().contains(mmv.getPvp1Btn())) mmv.getSecondRowPane().getChildren().add(mmv.getPvp1Btn());
                mmv.getPvpText().setText("PLAY");
            }
        });
        mmv.getPVPstartBtn().setOnAction(e->{
            stage.getScene().setRoot(gameView);
            this.gameView.getGameController().getPoolCueController().handleRotateCue(stage.getScene());
            this.gameView.getGameController().getPoolCueController().hit(stage.getScene());
                gameView.getGameController().startGame(gameView.getGameController().getGameType(), (PlayerModel) mmv.getComboBoxP1().getSelectionModel().getSelectedItem(), (PlayerModel) mmv.getComboBoxP2().getSelectionModel().getSelectedItem());
        });
        mmv.getPvp1Btn().setOnAction(event -> {
            gameView.getGameController().setGameType(1);
            mmv.Player2List = FXCollections.observableArrayList();
            mmv.Player1List = FXCollections.observableArrayList(PlayerModel.playersList);
            mmv.getComboBoxP1().setItems(mmv.Player1List);
            mmv.getComboBoxP1().getSelectionModel().clearSelection();
            mmv.getComboBoxP2().setItems(mmv.Player2List);
            mmv.getComboBoxP2().getSelectionModel().clearSelection();
            stage.getScene().setRoot(mmv.getPvpRootMenu());
            mmv.getPVPstartBtn().setDisable(true);
        });


    }


    public void pvAIAction(){
        mmv.getButtonGroup().setOnMouseExited(event -> {
            if(mmv.getSecondRowPane().getChildren().contains(mmv.getButtonGroup())){
                mmv.getSecondRowPane().getChildren().remove(mmv.getButtonGroup());
                mmv.getVersusAIText().setText("CHALLENGE\n    THE AI");
            }
        });

        mmv.getP3().hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                if(!mmv.getSecondRowPane().getChildren().contains(mmv.getButtonGroup()))mmv.getSecondRowPane().getChildren().add(mmv.getButtonGroup());
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
        for(int i = 0;i<3;i++) {
            int finalI = i;
            mmv.getButtonGroup().getChildren().get(i).setOnMouseClicked(e -> {
                gameView.getGameController().setGameType(finalI+2);
                mmv.Player1List = FXCollections.observableArrayList(PlayerModel.playersList);
                mmv.getComboBoxP1().setItems(mmv.Player1List);
                mmv.getComboBoxP1().getSelectionModel().clearSelection();
                mmv.getComboBoxP2().setItems(MainMenuView.aiList);
                mmv.getComboBoxP2().getSelectionModel().select(finalI);
                stage.getScene().setRoot(mmv.getPvpRootMenu());
                mmv.getPVPstartBtn().setDisable(true);
            });
        }
    }

    public void solo1Action() {
        gameView = new GameView(settingsView.getSettingsController());
        mmv.getSolo1Btn().setOnMouseClicked(event -> {
            stage.getScene().setRoot(gameView);
            this.gameView.getGameController().getPoolCueController().handleRotateCue(stage.getScene());
            this.gameView.getGameController().getPoolCueController().hit(stage.getScene());
            gameView.getGameController().startGame(0,new PlayerModel("Practice",1,0,0,0),null);
//            DBConnection.instantiateLastLayoutDB(gameModel.getGameType(), new PlayerModel("Test",false), new PlayerModel());
        });

        mmv.getSolo1Btn().setOnMouseExited(event -> {
            if(mmv.getSecondRowPane().getChildren().contains(mmv.getSolo1Btn())){
                mmv.getSecondRowPane().getChildren().remove(mmv.getSolo1Btn());
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
                if(!mmv.getSecondRowPane().getChildren().contains(mmv.getSolo1Btn()))mmv.getSecondRowPane().getChildren().add(mmv.getSolo1Btn());
            }
        });
    }

    public static GameView getGameView() {
        return gameView;
    }

    public void setSettingsView(SettingsView settingsView) {
        this.settingsView = settingsView;
    }

}
