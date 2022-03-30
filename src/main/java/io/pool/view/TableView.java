package io.pool.view;

import io.pool.eightpool.ResourcesLoader;
import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.model.TableBorderModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.util.ArrayList;


public class TableView {

    private final AnchorPane anchorPane;
    private final Pane table; // all components of the table
    private final ArrayList<Circle> holes = new ArrayList<>();
    public final static int width = (int) (game.eightPoolTableWidth/1.5); // the width of the pane
    public final static int height = (int) (game.eightPoolTableHeight/1.5); // the height of the pane
    private final int cornerHoleRadius = (int)(width/36);
    private final int centerHoleRadius = (int) (width/43.2);
    private final ImageView tableImageView;

    //boolean selectionCircleClicked;

    public TableView(Pane root) {

        BallModel.RADIUS = getTableWidth()/80;

        double layoutX = game.eightPoolTableX + 0.1*game.eightPoolTableX; // the XPosition in the general pane
        double layoutY = game.eightPoolTableY + 0.1*game.eightPoolTableY; // the YPosition in the general pane

        anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #1d6809");
        anchorPane.setPrefWidth(game.eightPoolTableWidth);
        anchorPane.setMinHeight(game.eightPoolTableHeight);

        table = new Pane();
        table.setPadding(new Insets(width/43.2, 0, 0, 0));
        table.setPrefWidth(width);
        table.setPrefHeight(height);
        table.setLayoutX(layoutX);
        table.setLayoutY(layoutY);

        tableImageView = new ImageView(); // the image view of the table
        Image tableImage = ResourcesLoader.tableImages.get(0);
        tableImageView.setImage(tableImage);
        tableImageView.setFitWidth(width);
        tableImageView.setFitHeight(height);

        Line whiteLine = new Line();
        whiteLine.setStartX(width-(width/3.6));
        whiteLine.setStartY(height/8.934);
        whiteLine.setEndX(width-(width/3.6));
        whiteLine.setEndY(height-(height/8.4375));
        whiteLine.setStroke(Color.WHITE);
        whiteLine.setStrokeWidth(3);

        table.getChildren().addAll(tableImageView, whiteLine);

        GridPane playersIcon = new GridPane();
        playersIcon.setHgap(width/21.6);
        playersIcon.setPadding(new Insets(0,0,width/21.6,0));
        playersIcon.setAlignment(Pos.CENTER);
        playersIcon.setPrefWidth(width);
        playersIcon.setPrefHeight(height/9.);
        Button player1Lbl = new Button("player1");
        player1Lbl.setPrefWidth((width/2.) - width/43.2);
        player1Lbl.setStyle("-fx-background-color: #3D4956");
        player1Lbl.setTextFill(Color.WHITE);
        player1Lbl.setFont(Font.font("Verdana", FontWeight.BOLD, width/43.2));
        Button player2Lbl = new Button("player2");
        player2Lbl.setPrefWidth(width/2. - width/43.2);
        player2Lbl.setStyle("-fx-background-color: #3D4956");
        player2Lbl.setTextFill(Color.WHITE);
        player2Lbl.setFont(Font.font("Verdana", FontWeight.BOLD, width/43.2));
        playersIcon.add(player1Lbl, 0,0);
        playersIcon.add(player2Lbl, 1,0);


        anchorPane.getChildren().addAll( playersIcon, table);

        AnchorPane.setLeftAnchor(table, width/43.2);
        AnchorPane.setTopAnchor(playersIcon, 0.0);
        AnchorPane.setLeftAnchor(playersIcon, width/43.2);

        root.getChildren().add(anchorPane); // adding the table to the main pain of the project.

        createHoles();
        createLines();

    }
    public void createHoles(){
        Circle upLeftCorner = new Circle();
        upLeftCorner.setRadius(cornerHoleRadius);
        upLeftCorner.setCenterX(width/22.5);
        upLeftCorner.setCenterY(height/11);
        holes.add(upLeftCorner);

        Circle downLeftCorner = new Circle();
        downLeftCorner.setRadius(cornerHoleRadius);
        downLeftCorner.setCenterX(width/22.5);
        downLeftCorner.setCenterY(height/1.1);
        holes.add(downLeftCorner);

        Circle upRightCorner = new Circle();
        upRightCorner.setRadius(cornerHoleRadius);
        upRightCorner.setCenterX(width/1.05675);
        upRightCorner.setCenterY(height/11);
        holes.add(upRightCorner);

        Circle downRightCorner = new Circle();
        downRightCorner.setRadius(cornerHoleRadius);
        downRightCorner.setCenterX(width/1.05675);
        downRightCorner.setCenterY(height/1.1);
        holes.add(downRightCorner);

        Circle upCenterCorner = new Circle();
        upCenterCorner.setRadius(centerHoleRadius);
        upCenterCorner.setCenterX((width/2)/1.014);
        upCenterCorner.setCenterY(height/14.268);
        holes.add(upCenterCorner);

        Circle downCenterCorner = new Circle();
        downCenterCorner.setRadius(centerHoleRadius);
        downCenterCorner.setCenterX((width/2)/1.014);
        downCenterCorner.setCenterY((height)/1.08);
        holes.add(downCenterCorner);

        for(Circle hole:holes){
            //hole.setFill(Color.BLUE);
            //hole.setVisible(true);
        }

        table.getChildren().addAll(holes);

    }
    public void createLines(){
        new TableBorderModel("upLeftLine",width/11.228, height/9.0667, width/2.16, height/9.0667,0.9,-0.9);
        new TableBorderModel("upRightLine",width/1.9115, height/9., width/1.10919, height/9.,1,-1);
        new TableBorderModel("upRightLine",width/1.9115, height/9, width/1.10919, height/9,0.9,-0.9);
        new TableBorderModel("downLeftLine",width/11.175561, height/1.13145, width/2.165, height/1.13145,0.9,-0.9);
        new TableBorderModel("downRightLine",width/1.9048, height/1.13145, width/1.10919, height/1.13145,0.985,-0.9);
        new TableBorderModel("centerLeftLine",width/16, height/5.9130, width/16, height/1.21429,-0.9,0.9);
        new TableBorderModel("centerLeftLine",width/16., height/5.9130, width/16., height/1.21429,-1,1);
        new TableBorderModel("centerRightLine",width/1.069, height/5.9130, width/1.069, height/1.21864,-0.9,0.9);

        //TODO Review these reflection factors to verify their effectiveness
        new TableBorderModel("topLeftHoleA1",width/11.228,height/9.0667,width/14.062,height/12.9,-0.9,-0.9);
        new TableBorderModel("topLeftHoleA2",width/16,height/5.9130,width/21.33333,height/7.15789,0.9,0.9);
        new TableBorderModel("bottomLeftHoleB1",width/16.143,height/1.21429,width/22.9299,height/1.16587,0.9,-0.9);
        new TableBorderModel("bottomLeftHoleB2",width/11.175561,height/1.13145,width/14.221220,height/1.089777,-0.9,0.9);
        new TableBorderModel("bottomMiddleHoleC1",width/2.165,height/1.13145,width/2.128,height/1.09149,0.9,0.9);
        new TableBorderModel("bottomMiddleHoleC2",width/1.9048,height/1.13145,width/1.935,height/1.09149,-0.9,0.9);
        new TableBorderModel("bottomRightHoleD1",width/1.109,height/1.13145,width/1.087,height/1.09149,0.9,0.9);
        new TableBorderModel("bottomRightHoleD2",width/1.069,height/1.21864,width/1.047,height/1.16638,-0.9,-0.9);
        new TableBorderModel("topRightHoleE1",width/1.069,height/5.9130,width/1.0518,height/7.15789,-0.9,0.9);
        new TableBorderModel("topRightHoleE2",width/1.10919,height/9,width/1.087,height/13.302,0.9,-0.9);
        new TableBorderModel("topMiddleHoleF1",width/1.9115,height/9,width/1.931,height/13.302,-0.9,-0.9);
        new TableBorderModel("topMiddleHoleF2",width/2.16,height/9,width/2.130,height/13.302,0.9,-0.9);

        for (TableBorderModel tbm:TableBorderModel.tableBorder) {
            tbm.setStroke(Color.WHITE);
            tbm.setStrokeWidth(0.5);
            tbm.setVisible(false);
        }
        table.getChildren().addAll(TableBorderModel.tableBorder);
    }
    public Pane getFullTable() {
        return table;
    }

    public ArrayList<Circle> getHoles() {
        return holes;
    }

    public static int getTableWidth() {
        return width;
    }

    public AnchorPane getGamePane() {
        return anchorPane;
    }

    public ImageView getTableImageView() {
        return tableImageView;
    }
}

