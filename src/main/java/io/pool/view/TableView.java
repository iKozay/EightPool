package io.pool.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class TableView {

    private Pane table; // all components of the table
    private Rectangle playTable; // the place where the ball move
    private ArrayList<Circle> holes = new ArrayList<>();
    public final static int holeRadius = 22;

//    private ArrayList<Hole> holes;
    public TableView(Pane root) throws MalformedURLException {
        int width = 1080; // the width of the pane
        int height = 610; // the height of the pane

        int layoutX = 300;
        int layoutY = 100;

        table = new Pane();
        table.setPrefWidth(width);
        table.setPrefHeight(height);
        table.setLayoutX(layoutX);
        table.setLayoutY(layoutY);

        ImageView tableImageView = new ImageView(); // the image view of the table
        Image image = new Image(new File("resources/tableImage/perfectTable.png").toURI().toURL().toExternalForm());
        tableImageView.setImage(image);
        tableImageView.setFitWidth(width);
        tableImageView.setPreserveRatio(true);

        Line whiteLine = new Line();
        whiteLine.setStartX(width-235);
        whiteLine.setStartY(65);
        whiteLine.setEndX(width-235);
        whiteLine.setEndY(height-70);
        whiteLine.setFill(Color.WHITE);


        // the place where the ball moves
        playTable = new Rectangle((width-135), (height-135));
        playTable.setX(65);
        playTable.setY(65);
        playTable.setFill(Color.GREEN);
        playTable.setVisible(false); //because no need to see it. it would be only useful in controlling the ball

        table.getChildren().addAll(tableImageView, playTable, whiteLine); // adding the components to the table


        root.getChildren().add(table); // adding the table to the main pain of the project.

/*
        Circle upLeftCorner = new Circle();
        upLeftCorner.setRadius(30);
        upLeftCorner.setCenterX(50);
        upLeftCorner.setCenterY(55);
        upLeftCorner.setFill(Color.BLUE);

        Circle downLeftCorner = new Circle();
        downLeftCorner.setRadius(30);
        downLeftCorner.setCenterX(50);
        downLeftCorner.setCenterY(height-55);
        downLeftCorner.setFill(Color.BLUE);

        Circle upRightCorner = new Circle();
        upRightCorner.setRadius(30);
        upRightCorner.setCenterX(width-55);
        upRightCorner.setCenterY(55);
        upRightCorner.setFill(Color.BLUE);

        Circle downRightCorner = new Circle();
        downRightCorner.setRadius(30);
        downRightCorner.setCenterX(width - 55);
        downRightCorner.setCenterY(height-55);
        downRightCorner.setFill(Color.BLUE);


        Circle upCenterCorner = new Circle();
        upCenterCorner.setRadius(30);
        upCenterCorner.setCenterX((width/2) - 10);
        upCenterCorner.setCenterY(50);
        upCenterCorner.setFill(Color.BLUE);

        Circle downCenterCorner = new Circle();
        downCenterCorner.setRadius(30);
        downCenterCorner.setCenterX((width/2) - 10);
        downCenterCorner.setCenterY(height-50);
        downCenterCorner.setFill(Color.BLUE);

        table.getChildren().addAll(upLeftCorner, downLeftCorner, upRightCorner, downRightCorner, upCenterCorner, downCenterCorner);

*/

        // make the corners simply circles, and make it possible for you the ball
        // controller to find the center mass of the ball, which will help you to disapear
        // when the center of the ball touches the corner.


    }
    public Pane getFullTable() {
        return table;
    }

    public Rectangle getPlayTable() {
        return playTable;
    }


    public ArrayList<Circle> getHoles() {
        return holes;
    }
}

