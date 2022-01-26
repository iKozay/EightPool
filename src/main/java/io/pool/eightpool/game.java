package io.pool.eightpool;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class game extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Pane(), 320, 240);
        stage.setTitle("EightPool");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}