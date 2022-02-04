package io.pool.eightpool;

import io.pool.controller.BallController;
import io.pool.controller.TableController;
import io.pool.model.TableModel;
import io.pool.view.PoolCueView;
import io.pool.view.TableView;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.net.MalformedURLException;

public class game extends Application {
    Sphere sphere;
    int increment=1;
    @Override
    public void start(Stage stage) throws MalformedURLException {
        //TODO Links for 3D Balls:
        // https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/paint/PhongMaterial.html
        // https://stackoverflow.com/questions/31382634/javafx-3d-rotations
        // https://stackoverflow.com/questions/68186839/javafx-3d-sphere-partial-texture
        //
        Pane root = new Pane();
        Scene scene = new Scene(root, 1920, 1080);
        stage.setTitle("EightPool");


        TableView tableView = new TableView(root);
        TableModel tableModel = new TableModel();
        TableController tableController = new TableController(tableView, tableModel);

        PoolCueView cueView = new PoolCueView();
        root.getChildren().add(cueView.getCue());

        BallController ballController = new BallController(root);


        GameLoopTimer gameLoopTimer = new GameLoopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {
                ballController.detectCollision(tableView.getPlayTable());
            }
        };
        gameLoopTimer.start();

        stage.setScene(scene);
        stage.show();

    }
public void moveSphere(){
    if(sphere.getLayoutX()<=100){
        increment=1;
    }else if (sphere.getLayoutX()>=500){
        increment=-1;
    }
    sphere.setLayoutX(sphere.getLayoutX()+increment);
    Rotate r = new Rotate(increment*5, new Point3D(sphere.getLayoutX(),sphere.getLayoutY(),0));
    sphere.getTransforms().add(r);
}
    public static void main(String[] args) {
        launch();
    }
}