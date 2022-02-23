package io.pool.view;

import io.pool.controller.MainMenuController;
import io.pool.eightpool.game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class TableView {

    private final BorderPane borderPane;
    private final Pane table; // all components of the table
    private final Rectangle playTable; // the place where the ball move
    private final ArrayList<Circle> holes = new ArrayList<>();
    private final ArrayList<Line> lines = new ArrayList<>();
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

        Label player1Lbl = new Label("player1");
        player1Lbl.setStyle("-fx-font-size: 24");
        Label player2Lbl = new Label("player2");
        player2Lbl.setStyle("-fx-font-size: 24");
        HBox playersIcon = new HBox(700,player1Lbl,player2Lbl);
        playersIcon.setAlignment(Pos.CENTER);
        playersIcon.setStyle("-fx-border-color: black");
        playersIcon.setPrefHeight(50);
        Text information = new Text();
        information.setText("Ball 1: " + "" + "\n" + "Ball 1: " + "" + "\n" + "Ball 2: " + "" + "\n" + "Ball 3: " + ""
                + "\n" + "Ball 4: " + "" + "\n" + "Ball 5: " + "" + "\n" + "Ball 6: " + "" + "\n" + "Ball 7: " + "" + "\n"
                + "Ball 8: " + "" + "\n" + "Ball 9: " + "" + "\n" + "Ball 10: " + "" + "\n" + "Ball 11: " + "" + "\n"
                + "Ball 12: " + "" + "\n" + "Ball 13: " + "" + "\n" + "Ball 14: " + "" + "\n" + "Ball 15: " + ""
                + "\n" + "Ball 16: " + "" + "\n" + "Ball Cue: " + "" + "\n");
        information.setStyle("-fx-font-size: 15");
        Slider sldFriction = new Slider();
        sldFriction.setShowTickLabels(true);
        Label sldLblFriction = new Label("Friction");
        Slider sldForce = new Slider();
        sldForce.setShowTickLabels(true);
        Label sldLblForce = new Label("Force");
        Button goToMenu = new Button("Go to main menu");
        goToMenu.setOnAction(e->{
            MainMenuController.gotoMainMenu();
        });
        VBox ballInformation = new VBox(information,sldLblForce,sldForce,sldLblFriction,sldFriction,goToMenu);
        ballInformation.setPrefHeight(500);
        ballInformation.setPrefWidth(220);
        ballInformation.setStyle("-fx-border-color: black");

        borderPane = new BorderPane();
        borderPane.setRight(ballInformation);
        borderPane.setTop(playersIcon);
        borderPane.setCenter(table);
        borderPane.setPrefWidth(1300);
        borderPane.setPrefHeight(610);



        ImageView tableImageView = new ImageView(); // the image view of the table
        Image image = new Image(new File("src/main/resources/tableImage/finalTable.png").toURI().toURL().toExternalForm());
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

        root.getChildren().add(borderPane); // adding the table to the main pain of the project.

        Circle upLeftCorner = new Circle();
        upLeftCorner.setRadius(cornerHoleRadius);
        upLeftCorner.setCenterX(48);
        upLeftCorner.setCenterY(55);
//        upLeftCorner.setFill(Color.BLUE);
        upLeftCorner.setVisible(false);
        holes.add(upLeftCorner);

        Circle downLeftCorner = new Circle();
        downLeftCorner.setRadius(cornerHoleRadius);
        downLeftCorner.setCenterX(48);
        downLeftCorner.setCenterY(height-57);
//        downLeftCorner.setFill(Color.BLUE);
        downLeftCorner.setVisible(false);
        holes.add(downLeftCorner);

        Circle upRightCorner = new Circle();
        upRightCorner.setRadius(cornerHoleRadius);
        upRightCorner.setCenterX(width-58);
        upRightCorner.setCenterY(55);
//        upRightCorner.setFill(Color.BLUE);
        upRightCorner.setVisible(false);
        holes.add(upRightCorner);

        Circle downRightCorner = new Circle();
        downRightCorner.setRadius(cornerHoleRadius);
        downRightCorner.setCenterX(width - 58);
        downRightCorner.setCenterY(height- 57);
//        downRightCorner.setFill(Color.BLUE);
        downRightCorner.setVisible(false);
        holes.add(downRightCorner);

        Circle upCenterCorner = new Circle();
        upCenterCorner.setRadius(centerHoleRadius);
        upCenterCorner.setCenterX((width/2)-7);
        upCenterCorner.setCenterY(41);
//        upCenterCorner.setFill(Color.BLUE);
        upCenterCorner.setVisible(false);
        holes.add(upCenterCorner);

        Circle downCenterCorner = new Circle();
        downCenterCorner.setRadius(centerHoleRadius);
        downCenterCorner.setCenterX((width/2) - 7);
        downCenterCorner.setCenterY(height-47);
        downCenterCorner.setVisible(false);
//        downCenterCorner.setFill(Color.BLUE);
        holes.add(downCenterCorner);

        table.getChildren().addAll(holes);


        Line upLeftLine = new Line(95, 65, 500, 65);
        upLeftLine.setStroke(Color.WHITE);
        upLeftLine.setStrokeWidth(3);
        lines.add(upLeftLine);

        Line upRightLine = new Line(565, 65, 975, 65);
        upRightLine.setStroke(Color.WHITE);
        upRightLine.setStrokeWidth(3);
        lines.add(upRightLine);

        Line downLeftLine = new Line(95, height-70, 500, height-70);
        downLeftLine.setStroke(Color.WHITE);
        downLeftLine.setStrokeWidth(3);
        lines.add(downLeftLine);

        Line downRightLine = new Line(565, height-70, 975, height-70);
        downRightLine.setStroke(Color.WHITE);
        downRightLine.setStrokeWidth(3);
        lines.add(downRightLine);

        Line centerLeftLine = new Line(65, 103, 65, height-110);
        centerLeftLine.setStroke(Color.WHITE);
        centerLeftLine.setStrokeWidth(3);
        lines.add(centerLeftLine);

        Line centerRightLine = new Line(1010, 103, 1010, height-110);
        centerRightLine.setStroke(Color.WHITE);
        centerRightLine.setStrokeWidth(3);
        lines.add(centerRightLine);

        table.getChildren().addAll(lines);


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


    //tableView: getHeight, getWidth, getX, getY, in the class diagram
}

