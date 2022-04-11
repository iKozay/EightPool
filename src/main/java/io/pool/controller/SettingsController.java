package io.pool.controller;


import io.pool.Database.PlayerTableDB;
import io.pool.Database.SettingsDB;
import io.pool.model.PhysicsModule;
import io.pool.model.PlayerModel;
import io.pool.view.SettingsView;
import javafx.event.ActionEvent;

public class SettingsController {

    private int controlOption;
    private SettingsView settingsView;
    private int cueHelper;
    private double frictionRatio;
    private int tableTheme;


    public SettingsController(SettingsView settingsView) {
        this.settingsView = settingsView;
        controlOption = (int)SettingsDB.readSettingPreferencesDB(SettingsDB.SettingsDBReadOptions[0]);
        setControlOption(controlOption);
        cueHelper = (int)SettingsDB.readSettingPreferencesDB(SettingsDB.SettingsDBReadOptions[1]);
        frictionRatio = SettingsDB.readSettingPreferencesDB(SettingsDB.SettingsDBReadOptions[2]);
        tableTheme = (int)SettingsDB.readSettingPreferencesDB(SettingsDB.SettingsDBReadOptions[3]);
    }

    public int getControlOption() {
        return controlOption;
    }

    public void setControlOption(int controlOption) {
        this.controlOption = controlOption;
        System.out.println(controlOption);
        if(this.controlOption==0){
            PoolCueController.keyboardOnly=false;
            PoolCueController.mouseOnly=true;
        }else if(this.controlOption==1){
            PoolCueController.keyboardOnly=false;
            PoolCueController.mouseOnly=false;
        }else{
            PoolCueController.keyboardOnly=true;
            PoolCueController.mouseOnly=false;
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

    public void setUsername(ActionEvent e, boolean newPlayer){
        boolean hasSpaces = settingsView.getNewPlayerTextField().getText().contains(" ");
        if(hasSpaces) {
            settingsView.getMsgLabel().setText("Username can't have any spaces!");
            return;
        }else{
            if(PlayerModel.doesPlayerExists(settingsView.getNewPlayerTextField().getText())&&newPlayer){
                settingsView.getMsgLabel().setText("Username is already taken!");
                return;
            }else{
                if(!settingsView.getNewPlayerTextField().getText().isEmpty()){
                    if(newPlayer) {
                        PlayerTableDB.createNewPlayerDB(new PlayerModel(settingsView.getNewPlayerTextField().getText()));
                        settingsView.getProfilesTable().getSelectionModel().select(PlayerModel.playersList.size() - 1);
                    }else{
                        int index = settingsView.getProfilesTable().getSelectionModel().getFocusedIndex();
                        if(index!=-1) {
                            PlayerModel selectedPlayer = PlayerModel.playersList.get(index);
                            String oldUsername = selectedPlayer.getUsername();
                            System.out.println(oldUsername + " --> " + settingsView.getNewPlayerTextField().getText());
                            if (settingsView.getNewPlayerTextField().getText().equalsIgnoreCase(oldUsername)) {
                                settingsView.getMsgLabel().setText("New username can't be the same as old username!");
                            } else {
                                PlayerTableDB.renamePlayerTableDB(selectedPlayer, settingsView.getNewPlayerTextField().getText());
                                selectedPlayer.setUsername(settingsView.getNewPlayerTextField().getText());
                                settingsView.getProfilesTable().refresh();
                            }
                        }else{
                            if(settingsView.getProfilesTable().getItems().size()==0){
                                settingsView.getMsgLabel().setText("Database is empty!");
                            }else {
                                settingsView.getMsgLabel().setText("Select a player to rename.");
                            }
                        }
                    }
                }else{
                    settingsView.getMsgLabel().setText("Username can't be null!");
                }
            }
        }
    }

    public void deletePlayer(ActionEvent e) {
        int row = settingsView.getProfilesTable().getSelectionModel().getFocusedIndex();
        PlayerTableDB.removePlayerDB(PlayerModel.playersList.get(row));
        PlayerModel.playersList.remove(row);
    }

    public int getTableTheme() {
        return tableTheme;
    }

    public void setTableTheme(int tableTheme) {
        this.tableTheme = tableTheme;
        SettingsDB.updateSettingsDB(SettingsDB.SettingsDBReadOptions[3],this.tableTheme,true);
    }
}
