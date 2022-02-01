package io.pool.eightpool;

import io.pool.controller.BallController;
import io.pool.controller.TableController;
import io.pool.view.BallView;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

public class game extends Application {
    Sphere sphere;
    int increment=1;
    @Override
    public void start(Stage stage) {
        //TODO Links for 3D Balls:
        // https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/paint/PhongMaterial.html
        // https://stackoverflow.com/questions/31382634/javafx-3d-rotations
        // https://stackoverflow.com/questions/68186839/javafx-3d-sphere-partial-texture
        //
        Pane root = new Pane();
        Scene scene = new Scene(root, 1920, 1080);
        stage.setTitle("EightPool");


        TableController table = new TableController(root);


        BallController ballController = new BallController(root);


        GameLoopTimer gameLoopTimer = new GameLoopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {
                ballController.detectCollision(table);
            }
        };
        gameLoopTimer.start();

        /*Rectangle table = new Rectangle(1080, 720);
        table.setFill(Color.GREEN);
        table.setX(300);
        table.setY(100);
        root.getChildren().addAll(table);

        Circle b3 = new Circle(30, Color.BLUE);
        Image imgB3 = new Image(new File("resources/billiards/ball15.jpg").toURI().toString());
        b3.setFill(new ImagePattern(imgB3));
        b3.setCenterX(300);
        b3.setCenterY(200);
        root.getChildren().addAll(b3);



        Circle ball1 = new Circle();
        ball1.setRadius(30);
        //ball1.setFill(Color.WHITE);
        ball1.setLayoutX(700);
        ball1.setLayoutY(400);
        root.getChildren().addAll(ball1);


        Circle ball2 = new Circle();
        ball2.setRadius(30);
        ball2.setFill(Color.RED);
        ball2.setLayoutX(900);
        ball2.setLayoutY(200);
        root.getChildren().addAll(ball2);


        double distance;


        for(int i = 0; i<1000; i++){
            //System.out.println(i);
            distance = Math.sqrt((Math.pow(ball1.getLayoutX()-ball2.getLayoutX(), 2) + Math.pow(ball1.getLayoutX()-ball2.getLayoutX(), 2))) ;
            System.out.println(distance);

            if((distance<= ball1.getRadius()*2)){
            //if(Math.abs(ball1.getLayoutX()-ball2.getLayoutX()) <=ball1.getRadius()*2 || Math.abs(ball1.getLayoutY()-ball2.getLayoutY()) <=ball1.getRadius()*2) {

                System.out.println(ball1.getLayoutX());
                System.out.println(ball2.getLayoutX());
                System.out.println(ball1.getLayoutY());
                System.out.println(ball2.getLayoutY());
                System.out.println(i);
                break;


            }
            ball1.setLayoutX(ball1.getLayoutX()+1);
            ball1.setLayoutY(ball1.getLayoutY()-1);

        }*/


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