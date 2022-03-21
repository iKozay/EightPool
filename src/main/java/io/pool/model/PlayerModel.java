package io.pool.model;

import java.util.ArrayList;

public class PlayerModel {

    private ArrayList<BallModel> ballNeededIn;
    private String username;
    /**
     * 0 = Solid
     * 1 = Stripe
     */
    private int ballType;


    public PlayerModel(String username) {
        this.username = username;
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
}
