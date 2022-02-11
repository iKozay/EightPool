package io.pool.view;

import io.pool.eightpool.game;
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

    private final Pane table; // all components of the table
    private final Rectangle playTable; // the place where the ball move
    private final ArrayList<Circle> holes = new ArrayList<>();
    public final static int cornerHoleRadius = 30;
    public final static int centerHoleRadius = 25;

    //    private ArrayList<Hole> holes;
    public TableView(Pane root) throws MalformedURLException {
        int width = 1080; // the width of the pane
        int height = 610; // the height of the pane

        int layoutX = game.eightPoolTableX; // the XPosition in the general pane
        int layoutY = game.eightPoolTableY; // the YPosition in the general pane

        table = new Pane();
        table.setPrefWidth(width);
        table.setPrefHeight(height);
        table.setLayoutX(layoutX);
        table.setLayoutY(layoutY);

        ImageView tableImageView = new ImageView(); // the image view of the table
        Image image = new Image(new File("resources/tableImage/finalTable.png").toURI().toURL().toExternalForm());
        tableImageView.setImage(image);
        tableImageView.setFitWidth(width);
        tableImageView.setPreserveRatio(true);

        Line whiteLine = new Line();
        whiteLine.setStartX(width-300);
        whiteLine.setStartY(68);
        whiteLine.setEndX(width-300);
        whiteLine.setEndY(height-72);
        whiteLine.setStroke(Color.WHITE);
        whiteLine.setStrokeWidth(3);


        // the place where the ball moves
        playTable = new Rectangle((width-135), (height-135));
        playTable.setX(65);
        playTable.setY(65);
        playTable.setFill(Color.GREEN);
        playTable.setVisible(false); //because no need to see it. it would be only useful in controlling the ball

        table.getChildren().addAll(tableImageView, playTable, whiteLine); // adding the components to the table

        root.getChildren().add(table); // adding the table to the main pain of the project.

        Circle upLeftCorner = new Circle();
        upLeftCorner.setRadius(cornerHoleRadius);
        upLeftCorner.setCenterX(48);
        upLeftCorner.setCenterY(55);
        upLeftCorner.setVisible(false);
        holes.add(upLeftCorner);

        Circle downLeftCorner = new Circle();
        downLeftCorner.setRadius(cornerHoleRadius);
        downLeftCorner.setCenterX(48);
        downLeftCorner.setCenterY(height-57);
        downLeftCorner.setVisible(false);
        holes.add(downLeftCorner);

        Circle upRightCorner = new Circle();
        upRightCorner.setRadius(cornerHoleRadius);
        upRightCorner.setCenterX(width-58);
        upRightCorner.setCenterY(55);
        upRightCorner.setVisible(false);
        holes.add(upRightCorner);

        Circle downRightCorner = new Circle();
        downRightCorner.setRadius(cornerHoleRadius);
        downRightCorner.setCenterX(width - 58);
        downRightCorner.setCenterY(height- 57);
        downRightCorner.setVisible(false);
        holes.add(downRightCorner);

        Circle upCenterCorner = new Circle();
        upCenterCorner.setRadius(centerHoleRadius);
        upCenterCorner.setCenterX((width/2)-7);
        upCenterCorner.setCenterY(41);
        upCenterCorner.setVisible(false);
        holes.add(upCenterCorner);

        Circle downCenterCorner = new Circle();
        downCenterCorner.setRadius(centerHoleRadius);
        downCenterCorner.setCenterX((width/2) - 7);
        downCenterCorner.setCenterY(height-47);
        downCenterCorner.setVisible(false);
        holes.add(downCenterCorner);

        table.getChildren().addAll(holes);


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

