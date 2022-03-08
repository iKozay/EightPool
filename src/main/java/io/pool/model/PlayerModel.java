package io.pool.model;

import java.util.ArrayList;

public class PlayerModel {

    private ArrayList<BallModel> ballNeededIn;
    private String username;


    public PlayerModel(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
