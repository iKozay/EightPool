package io.pool.controller;


import io.pool.Database.SettingsDB;
import io.pool.model.PlayerModel;
import io.pool.view.SettingsView;
import javafx.collections.ObservableList;

public class SettingsController {

    private SettingsView settingsView;
    private int cueHelper;
    private double frictionRatio;


    public SettingsController(SettingsView settingsView) {
        this.settingsView = settingsView;
        //cueHelper = SettingsDB;
    }

    public int getControlOption() {
    }

    public void setControlOption(int selectedIndex) {
    }

    public int getCueHelper() {
        return cueHelper;
    }

    public void setCueHelper(int selectedIndex) {
    }

    public double getFrictionPercentage() {
        return frictionRatio;
    }

    public void setFrictionPercentage(double doubleValue) {
    }

}
