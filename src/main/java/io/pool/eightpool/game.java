package io.pool.eightpool;

import io.pool.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class game extends Application {
    public final static int eightPoolTableX = 0;
    public final static int eightPoolTableY = 50;
    public final static int eightPoolTableWidth = 1920;
    public final static int eightPoolTableHeight = 1080;

    @Override
    public void start(Stage stage) throws IOException {
        //TODO Units: 0.04pixels/m
        //TODO Links for 3D Balls:
        // https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/paint/PhongMaterial.html
        // https://stackoverflow.com/questions/31382634/javafx-3d-rotations
        // https://stackoverflow.com/questions/68186839/javafx-3d-sphere-partial-texture
        //

        /**
         * Instantiates the Main Menu View and adds it the Scene
         */
        MainMenuView mmv = new MainMenuView(stage);

        Scene scene = new Scene(mmv);

        stage.setTitle("EightPool");
        stage.setFullScreen(false);
        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}