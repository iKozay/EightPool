package io.pool.view;

import io.pool.controller.MainMenuController;
import io.pool.eightpool.game;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class aboutView extends Pane {
    public aboutView() {
        VBox vb = new VBox();
        Text lbl = new Text("""
                Concept: \n This project revolves around the principles of linear momentum and collision as found in mechanics. Billiard balls collide with each other during the unfolding of the game. These collisions can be predicted and written out as mathematical formulas. This will allow us to design and implement a working billiards game. Furthermore, Newton’s physical laws would be integrated in the project when the cue hits the white ball. This would allow the user to cover a wider range of concepts in physics. Friction is a part of the game as well. This is how the balls come to rest on the table after being initial struck by the player. There would be some math in the project as well, where the user can see the distance between the ball he/she hits and the target.
                """);
        lbl.setWrappingWidth(game.eightPoolTableWidth*0.6);
        lbl.setFont(new Font(30));
        Button back = new Button("Back to main menu");
        back.setOnAction(e->{
            MainMenuController.gotoMainMenu();
        });
        vb.getChildren().addAll(lbl,back);
        this.getChildren().addAll(vb);
    }
}
