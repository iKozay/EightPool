package io.pool.controller;


import io.pool.Database.SettingsDB;
import io.pool.model.PhysicsModule;
import io.pool.view.SettingsView;

public class SettingsController {

    private int controlOption;
    private SettingsView settingsView;
    private int cueHelper;
    private double frictionRatio;


    public SettingsController(SettingsView settingsView) {
        this.settingsView = settingsView;
        controlOption = (int)SettingsDB.readSettingPreferencesDB(SettingsDB.SettingsDBReadOptions[0]);
        cueHelper = (int)SettingsDB.readSettingPreferencesDB(SettingsDB.SettingsDBReadOptions[1]);
        frictionRatio = SettingsDB.readSettingPreferencesDB(SettingsDB.SettingsDBReadOptions[2]);
    }

    public int getControlOption() {
        return controlOption;
    }

    public void setControlOption(int controlOption) {
        this.controlOption = controlOption;
        if(this.controlOption==0){
            PoolCueController.keyboardOnly=false;
        }else{
            PoolCueController.keyboardOnly=true;
        }
        SettingsDB.updateSettingsDB(SettingsDB.SettingsDBReadOptions[0],this.controlOption,true);
    }

    public int getCueHelper() {
        return cueHelper;
    }

    public void setCueHelper(int cueHelper) {
        this.cueHelper = cueHelper;
        if(this.cueHelper==0){
            PoolCueController.cueHelperEnabled=false;
        }else{
            PoolCueController.cueHelperEnabled=true;
        }
        SettingsDB.updateSettingsDB(SettingsDB.SettingsDBReadOptions[1],this.cueHelper,true);
    }

    public double getFrictionPercentage() {
        return frictionRatio*100;
    }

    public void setFrictionPercentage(double frictionPercentage) {
        this.frictionRatio = frictionPercentage/100.0;
        settingsView.getOption3().setValue(getFrictionPercentage());
        PhysicsModule.FRICTION_RATIO=this.frictionRatio;
        SettingsDB.updateSettingsDB(SettingsDB.SettingsDBReadOptions[2],this.frictionRatio,false);
    }

}
