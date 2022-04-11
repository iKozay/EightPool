package io.pool.eightpool;

import io.pool.Database.BallConfigurationDB;
import io.pool.Database.DBConnection;
import io.pool.Database.PlayerTableDB;
import io.pool.view.*;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


public class game extends Application {
    public final static int eightPoolTableX = 0;
    public final static int eightPoolTableY = 50;
    public final static int eightPoolTableWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
    public final static int eightPoolTableHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();

    @Override
    public void start(Stage stage) throws IOException {

        //TODO Units: 0.04pixels/m
        //TODO Links for 3D Balls:
        // https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/paint/PhongMaterial.html
        // https://stackoverflow.com/questions/31382634/javafx-3d-rotations
        // https://stackoverflow.com/questions/68186839/javafx-3d-sphere-partial-texture
        //

        System.out.println("Height: " + eightPoolTableHeight);
        System.out.println("Width: " + eightPoolTableWidth);

        ResourcesLoader.load();
        PlayerTableDB.fetchAllPlayers();
        /**
         * Instantiates the Main Menu View and adds it the Scene
         */
        MainMenuView mmv = new MainMenuView(stage);

        Scene scene = new Scene(mmv);
        scene.getStylesheets().add("sliderDesign.css");
        System.out.println(Screen.getPrimary().getVisualBounds().getHeight());

        stage.setTitle("EightPool");
        stage.setFullScreen(false);
        stage.setScene(scene);
        stage.show();
        BallConfigurationDB.deleteAllData();


    }

    public static void main(String[] args) {
        launch();
    }
}