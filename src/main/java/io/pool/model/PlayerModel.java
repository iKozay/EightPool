package io.pool.model;

import java.util.ArrayList;

public class PlayerModel {

    private ArrayList<BallModel> ballNeededIn;
    private String username;
    /**
     * 0 = Solid
     * 1 = Stripe
     */
    private int ballType = 1;
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
}
