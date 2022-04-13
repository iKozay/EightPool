package io.pool.controller;

import io.pool.view.BallView;
import io.pool.view.TableView;
import javafx.scene.shape.Circle;

public class TableController {

    /** Instance of the table that will be shown to the user */
    private TableView tableView;
    private GameController gameController;
    /** tableX and tableY */
    private double tableX;
    private double tableY;
    private boolean isBallGotInHole = false;


    /**
     * Main constructor of TableController
     * @param tableView The Table View
     */
    public TableController(TableView tableView, GameController gameController) {
        this.tableView = tableView;
        this.gameController = gameController;
        tableX = tableView.getFullTable().getLayoutX()+(TableView.width/43.2);
        tableY = tableView.getFullTable().getLayoutY()+(TableView.width/16.);
    }


    /**
     * Gets the Table View
     * @return the Table View
     */
    public TableView getTableView() {
        return tableView;
    }

    /**
     * Checks if the BallView is inside the hole
     * @param ballView the BallView
     * @return <code>true</code> if the ball is inside the hole. <code>false</code> otherwise
     */
    public boolean checkBallInHole(BallView ballView) {
        for(Circle hole:tableView.getHoles()) {
            double xSquared = Math.pow((ballView.getBall().getLayoutX() - tableX - hole.getCenterX()), 2);
            double ySquared = Math.pow((ballView.getBall().getLayoutY() - tableY - hole.getCenterY()), 2);
            double centerToCenter = Math.sqrt(xSquared+ySquared);
            if(centerToCenter < hole.getRadius()) {
                if(!gameController.getAiController().isAITraining()) SoundController.BallInHole();
                return true;
            }
        }
        return false;
    }

    public void turnView(GameController gameController){
//        if (isBallGotInHole) {
//            tableView.assignBallsInTableView(1, gameController.getP1().getBallNeededIn());
//            tableView.assignBallsInTableView(2, gameController.getP2().getBallNeededIn());
//            isBallGotInHole = false;
//            gameController.setFirstBallInHole(false);
//        }

        if(gameController.getP1().isTurn()){
            tableView.getPlayer1Lbl().setStyle("-fx-background-color: green");
            tableView.getPlayer1Lbl().setText(gameController.getP1().getUsername());
            if(gameController.getGameType() == 1) {
                tableView.getPlayer2Lbl().setStyle("-fx-background-color: #3D4956");
                tableView.getPlayer2Lbl().setText(gameController.getP2().getUsername());
            }
        }else{
            tableView.getPlayer2Lbl().setStyle("-fx-background-color: green");
            tableView.getPlayer1Lbl().setText(gameController.getP1().getUsername());
            tableView.getPlayer2Lbl().setText(gameController.getP2().getUsername());
            tableView.getPlayer1Lbl().setStyle("-fx-background-color: #3D4956");

        }
    }
    public void turnsColor(){
        if(!(gameController.getP2() == null)){
            tableView.getPlayer2Lbl().setText(gameController.getP2().getUsername());
        }else{

            tableView.getPlayer1Lbl().setText(gameController.getP1().getUsername());
        }
        if(gameController.getP1().isTurn()){
            tableView.getPlayer1Lbl().setStyle("-fx-background-color: green");
            tableView.getPlayer2Lbl().setStyle("-fx-background-color: #3D4956");
        }else{
            tableView.getPlayer2Lbl().setStyle("-fx-background-color: green");
            tableView.getPlayer1Lbl().setStyle("-fx-background-color: #3D4956");
        }
    }

    public double getTableX() {
        return tableX;
    }

    public double getTableY() {
        return tableY;
    }

    public void setBallGotInHole(boolean ballGotInHole) {
        isBallGotInHole = ballGotInHole;
    }

    public boolean getBallGotInHole() {
        return isBallGotInHole;
    }

}
