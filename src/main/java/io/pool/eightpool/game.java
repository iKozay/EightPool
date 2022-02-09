package io.pool.eightpool;

import io.pool.controller.BallController;
import io.pool.controller.PoolCueController;
import io.pool.controller.TableController;
import io.pool.model.TableModel;
import io.pool.view.PoolCueView;
import io.pool.view.TableView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

import java.net.MalformedURLException;

public class game extends Application {
    Sphere sphere;
    int increment=1;
    @Override
    public void start(Stage stage) throws MalformedURLException {
        // TODO Units: 0.04pixels/m
        //TODO Links for 3D Balls:
        // https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/paint/PhongMaterial.html
        // https://stackoverflow.com/questions/31382634/javafx-3d-rotations
        // https://stackoverflow.com/questions/68186839/javafx-3d-sphere-partial-texture
        //
        Pane root = new Pane();
        Scene scene = new Scene(root, 1920, 1080);
        stage.setTitle("EightPool");


        TableView tableView = new TableView(root);
        TableModel tableModel = new TableModel(tableView);

        PoolCueController pcc = new PoolCueController();
        PoolCueView cueView = new PoolCueView();
        root.getChildren().add(cueView.getCue());

        BallController ballController = new BallController(root);

        TableController tableController = new TableController(tableView, tableModel);

        tableController.setBallController(ballController);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            }
        };
        timer.start();

        GameLoopTimer gameLoopTimer = new GameLoopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {
                if(secondsSinceLastFrame<1) {
                    ballController.detectCollision(tableView.getPlayTable(), secondsSinceLastFrame);
                    root.setOnMouseMoved(event -> pcc.cueRotateHandler(event));
                }

            }
        };
        gameLoopTimer.start();

        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}