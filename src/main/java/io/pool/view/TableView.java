package io.pool.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class TableView {

    private Pane table; // all components of the table
    private Rectangle playTable; // the place where the ball move

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


        // the place where the ball moves
        playTable = new Rectangle((width-135), (height-135));
        playTable.setX(65);
        playTable.setY(65);
        playTable.setFill(Color.GREEN);
        playTable.setVisible(false); //because no need to see it. it would be only useful in controlling the ball

        table.getChildren().addAll(tableImageView, playTable); // adding the components to the table


        root.getChildren().add(table); // adding the table to the main pain of the project.

        Corner upLeftCorner = new Corner(table, holeRadius, 40, 50, 55, -45);
        Corner downLeftCorner = new Corner(table, holeRadius, 40, 50, height - 55, 45);

        Corner upCenterCorner = new Corner(table, holeRadius + 10, 20, width/2, 55, -90);
        Corner downCenterCorner = new Corner(table, holeRadius + 10, 20, width/2, height -55, -90);

        Corner upRightCorner = new Corner(table, holeRadius, 40, width - 55, 55, -135);
        Corner downRightCorner = new Corner(table, holeRadius, 40, width - 55, height - 55, 135);

//        for (int i=0; i<2;i++) {
//            for(int j=0; j < 3; j++) {
//
//                Hole hole = new Hole(true);
//                hole.setCenterX((j*(width/2)) + (holeRadius-(j*holeRadius)));
//                hole.setCenterY((i*height) + (holeRadius-(2*i*holeRadius)));
//                table.getChildren().add(hole);
//            }
//        }



    }
    public Pane getFullTable() {
        return table;
    }

    public Rectangle getPlayTable() {
        return playTable;
    }
}

