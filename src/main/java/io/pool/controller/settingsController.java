package io.pool.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class settingsController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML protected void handleToMainMenu(ActionEvent event) {
        MainMenuController.gotoMainMenu();
    }
    @FXML protected void handleclose(ActionEvent event) {
        Platform.exit();
    }


}
