package io.pool.model;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public class PlayerModel {

    public static ObservableList<PlayerModel> playersList;
    private ArrayList<BallModel> ballNeededIn;
    private String username;
    /**
     * 0 = Solid
     * 1 = Stripe
     */
    private int ballType;
    private int selectedPoolCue;
    private int numberOfWins;
    private int numberOfLoss;
    private int averageNumberOfShotsPerGame;
    private boolean turn;


    public PlayerModel(){
        this.username = null;
    }

    public PlayerModel(String username,boolean turn) {
        this.username = username;
        this.turn = turn;
    }

    public PlayerModel(String username,int selectedPoolCue, int numberOfWins,int numberOfLoss, int averageNumberOfShotsPerGame){
        this.username=username;
        this.selectedPoolCue=selectedPoolCue;
        this.numberOfWins=numberOfWins;
        this.numberOfLoss=numberOfLoss;
        this.averageNumberOfShotsPerGame=averageNumberOfShotsPerGame;
    }



//getters and setters

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public int getBallType() {
        return ballType;
    }

    public void setBallType(int ballType) {
        this.ballType = ballType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<BallModel> getBallNeededIn() {return ballNeededIn;}

    public void setBallNeededIn(ArrayList<BallModel> ballNeededIn) {this.ballNeededIn = ballNeededIn;}

    public int getNumberOfWins() {return numberOfWins;}

    public void setNumberOfWins(int numberOfWins) {this.numberOfWins = numberOfWins;}

    public int getNumberOfLoss() {return numberOfLoss;}

    public void setNumberOfLoss(int numberOfLoss) {this.numberOfLoss = numberOfLoss;}

    public int getAverageNumberOfShotsPerGame() {return averageNumberOfShotsPerGame;}

    public void setAverageNumberOfShotsPerGame(int averageNumberOfShotsPerGame) {this.averageNumberOfShotsPerGame = averageNumberOfShotsPerGame;}

    public int getSelectedPoolCue() {
        return selectedPoolCue;
    }

    public void setSelectedPoolCue(int selectedPoolCue) {
        this.selectedPoolCue = selectedPoolCue;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerModel that = (PlayerModel) o;
        return username.equals(that.username);
    }
}
