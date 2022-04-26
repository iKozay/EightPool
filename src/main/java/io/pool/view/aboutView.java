package io.pool.view;

import io.pool.controller.MainMenuController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class aboutView extends Pane {
    public aboutView() {
        HBox hb = new HBox();
        Label lbl = new Label("About the project");
        Button back = new Button("Back to main menu");
        back.setOnAction(e->{
            MainMenuController.gotoMainMenu();
        });
        hb.getChildren().addAll(lbl,back);
        this.getChildren().addAll(hb);
    }
}
