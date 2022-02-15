package io.pool.eightpool;

import io.pool.controller.BallController;
import io.pool.controller.PoolCueController;
import io.pool.controller.TableController;
import io.pool.model.TableModel;
import io.pool.view.BallView;
import io.pool.view.PoolCueView;
import io.pool.view.TableView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.net.MalformedURLException;

public class game extends Application {
    public final static int eightPoolTableX = 0;
    public final static int eightPoolTableY = 0;

    private double oldPosX;
    private double oldPosY;
    @Override
    public void start(Stage stage) throws MalformedURLException {
        // TODO Units: 0.04pixels/m
        //TODO Links for 3D Balls:
        // https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/paint/PhongMaterial.html
        // https://stackoverflow.com/questions/31382634/javafx-3d-rotations
        // https://stackoverflow.com/questions/68186839/javafx-3d-sphere-partial-texture
        //
        Pane root = new Pane();
        Scene scene = new Scene(root, 1080, 610);
        stage.setTitle("EightPool");

        TableView tableView = new TableView(root);
        TableModel tableModel = new TableModel(tableView);

        PoolCueController pcc = new PoolCueController();
        PoolCueView cueView = new PoolCueView();
        root.getChildren().addAll(cueView.getCue());

        BallController ballController = new BallController(root);

        TableController tableController = new TableController(tableView, tableModel);

        tableController.setBallController(ballController);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (BallView ballView : ballController.ballViewArrayList()) {
                    for (int i = 0; i < tableView.getHoles().size(); i++) {
                        if(tableController.checkInterBallsHoles(ballView, i)) {
                            ballView.getBall().setRadius(ballView.getBall().getRadius() - 0.3);
                        }
                    }
                }
            }
        };
        timer.start();

        scene.setOnMouseEntered(event -> {
            oldPosX = event.getSceneX();
            oldPosY = event.getSceneY();
        });
        scene.setOnMouseMoved(event -> {
            pcc.handleRotateCue(event, oldPosX, oldPosY);
        });

        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}