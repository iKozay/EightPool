package io.pool.view;

import io.pool.controller.BallController;
import io.pool.controller.GameController;
import io.pool.eightpool.ResourcesLoader;
import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.model.TableBorderModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;


public class TableView {

    private final AnchorPane anchorPane;
    private final Pane table; // all components of the table
    private final ArrayList<Circle> holes = new ArrayList<>();
//    private final ArrayList<StackPane> remainingBallsStackPane1 = new ArrayList<>();
//    private final ArrayList<StackPane> remainingBallsStackPane2 = new ArrayList<>();
    public final static int width = (int) (game.eightPoolTableWidth/1.5); // the width of the pane
    public final static int height = (int) (game.eightPoolTableHeight/1.5); // the height of the pane
    private final int cornerHoleRadius = (int)(width/36);
    private final int centerHoleRadius = (int) (width/43.2);
    private final ImageView tableImageView;
    private Line whiteLine;
    private Label player1Lbl,player2Lbl;
    private Shape accessibleArea;
    private Label playersScore;
    private HBox firstPlayerRemainingBalls;
    private HBox secondPlayerRemainingBalls;

    //boolean selectionCircleClicked;

    public TableView(Pane root) {

        BallModel.RADIUS = getTableWidth()/85;

        double layoutX = game.eightPoolTableX + 0.1*game.eightPoolTableX; // the XPosition in the general pane
        double layoutY = game.eightPoolTableY + 0.1*game.eightPoolTableY; // the YPosition in the general pane

        anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #1d6809");
        anchorPane.setPrefWidth(game.eightPoolTableWidth);
        anchorPane.setMinHeight(game.eightPoolTableHeight);

        table = new Pane();
        table.setPrefWidth(width);
        table.setPrefHeight(height);
        table.setLayoutX(layoutX);
        table.setLayoutY(layoutY);

        tableImageView = new ImageView(); // the image view of the table
        Image tableImage = ResourcesLoader.tableImages.get(0);
        tableImageView.setImage(tableImage);
        tableImageView.setFitWidth(width);
        tableImageView.setFitHeight(height);

        whiteLine = new Line();
        whiteLine.setStartX((width/3.6));
        whiteLine.setStartY(height/8.934);
        whiteLine.setEndX((width/3.6));
        whiteLine.setEndY(height-(height/8.4375));
        whiteLine.setStroke(Color.WHITE);
        whiteLine.setStrokeWidth(1);

        table.getChildren().addAll(tableImageView, whiteLine);

        GridPane playersIcon = new GridPane();
        playersIcon.setHgap(width/21.65);
        playersIcon.setVgap(-3);
        playersIcon.setAlignment(Pos.TOP_LEFT);
        playersIcon.setPrefWidth(width);
        playersIcon.setPrefHeight(height/9.);
        player1Lbl = new Label("player1");
        player1Lbl.setPrefWidth((width/2.) - width/15.);
        player1Lbl.setPrefHeight(height/9.);
        player1Lbl.setStyle("-fx-background-color: #3D4956;");
        player1Lbl.setTextFill(Color.WHITE);
        player1Lbl.setFont(Font.font("Verdana", FontWeight.BOLD, width/43.2));
        player2Lbl = new Label("player2");
        player2Lbl.setPrefWidth(width/2. - width/15.);
        player2Lbl.setPrefHeight(height/9.);
        player2Lbl.setStyle("-fx-background-color: #3D4956; ");
        player2Lbl.setTextFill(Color.WHITE);
        player2Lbl.setFont(Font.font("Verdana", FontWeight.BOLD, width/43.2));

        Label scoreLabel = new Label("Score");
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font("Verdana", FontWeight.BOLD, width/45.));
        scoreLabel.setAlignment(Pos.CENTER);

        playersScore = new Label("0 : 0");
        playersScore.setAlignment(Pos.CENTER);
        playersScore.setStyle("-fx-background-color: transparent");
        playersScore.setTextFill(Color.WHITE);
        playersScore.setFont(Font.font("Verdana", FontWeight.BOLD, width/40.));
        playersScore.setMinWidth(width/8.);
        playersScore.setPrefHeight(height/9.);

        VBox scoreBox = new VBox();
        scoreBox.setPadding(new Insets(10));
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setStyle("-fx-background-color: #3D4956; -fx-background-radius: 25px;");

        scoreBox.getChildren().addAll(scoreLabel, playersScore);

        firstPlayerRemainingBalls = new HBox();
        firstPlayerRemainingBalls.setPrefWidth(width/2.6);
        firstPlayerRemainingBalls.setMinWidth(width/2.6);
        firstPlayerRemainingBalls.setPrefHeight(height/15.);
        firstPlayerRemainingBalls.setMinHeight(height/15.);
        firstPlayerRemainingBalls.setSpacing(width/43.2);
        firstPlayerRemainingBalls.setAlignment(Pos.CENTER);
        firstPlayerRemainingBalls.setStyle("-fx-background-color: #3D4956; -fx-background-radius: 15px;");
        firstPlayerRemainingBalls.setVisible(false);

//        for (int i = 0; i < 7; i++) {
//            StackPane remainingBall = new StackPane();
//            remainingBall.getChildren().add(createNeededCircles());
//            firstPlayerRemainingBalls.getChildren().add(remainingBall);
//        }

        secondPlayerRemainingBalls = new HBox();
        secondPlayerRemainingBalls.setPrefWidth(width/2.6);
        secondPlayerRemainingBalls.setMinWidth(width/2.6);
        secondPlayerRemainingBalls.setPrefHeight(height/15.);
        secondPlayerRemainingBalls.setMinHeight(height/15.);
        secondPlayerRemainingBalls.setSpacing(width/43.2);
        secondPlayerRemainingBalls.setAlignment(Pos.CENTER);
        secondPlayerRemainingBalls.setStyle("-fx-background-color: #3D4956; -fx-background-radius: 15px;");
        secondPlayerRemainingBalls.setVisible(false);

        //        for (int i = 0; i < 7; i++) {
//            StackPane remainingBall = new StackPane();
//            remainingBall.getChildren().add(createNeededCircles());
//            secondPlayerRemainingBalls.getChildren().add(remainingBall);
//        }

        playersIcon.add(player1Lbl, 0,0);
        playersIcon.add(scoreBox, 1, 0);
        playersIcon.add(player2Lbl, 2,0);
        playersIcon.add(firstPlayerRemainingBalls, 0, 1);
        playersIcon.add(secondPlayerRemainingBalls, 2, 1);

        HBox remainingBallsPane = new HBox();
        remainingBallsPane.setSpacing(100);
        remainingBallsPane.setAlignment(Pos.CENTER);

        anchorPane.getChildren().addAll( playersIcon, table);

        AnchorPane.setLeftAnchor(table, width/43.2);
        AnchorPane.setTopAnchor(table, width/8.);//////
        AnchorPane.setTopAnchor(playersIcon, 0.0);
        AnchorPane.setLeftAnchor(playersIcon, width/43.2);

        root.getChildren().add(anchorPane); // adding the table to the main pain of the project.

        createHoles();
        createLines(root);

    }
    public void createHoles(){
        //Hole 1
        Circle upLeftCorner = new Circle();
        upLeftCorner.setRadius(cornerHoleRadius);
        upLeftCorner.setCenterX(width/22.5);
        upLeftCorner.setCenterY(height/11);
        holes.add(upLeftCorner);

        //Hole 2
        Circle downLeftCorner = new Circle();
        downLeftCorner.setRadius(cornerHoleRadius);
        downLeftCorner.setCenterX(width/22.5);
        downLeftCorner.setCenterY(height/1.1);
        holes.add(downLeftCorner);

        //Hole 3
        Circle downCenterCorner = new Circle();
        downCenterCorner.setRadius(centerHoleRadius);
        downCenterCorner.setCenterX((width/2)/1.014);
        downCenterCorner.setCenterY((height)/1.08);
        holes.add(downCenterCorner);

        //Hole 4
        Circle downRightCorner = new Circle();
        downRightCorner.setRadius(cornerHoleRadius);
        downRightCorner.setCenterX(width/1.05675);
        downRightCorner.setCenterY(height/1.1);
        holes.add(downRightCorner);

        //Hole 5
        Circle upRightCorner = new Circle();
        upRightCorner.setRadius(cornerHoleRadius);
        upRightCorner.setCenterX(width/1.05675);
        upRightCorner.setCenterY(height/11);
        holes.add(upRightCorner);

        //Hole 6
        Circle upCenterCorner = new Circle();
        upCenterCorner.setRadius(centerHoleRadius);
        upCenterCorner.setCenterX((width/2)/1.014);
        upCenterCorner.setCenterY(height/14.268);
        holes.add(upCenterCorner);


        for(Circle hole:holes){
            //hole.setFill(Color.BLUE);
            hole.setVisible(false);
        }

        table.getChildren().addAll(holes);

    }
    public void createLines(Pane root){


        new TableBorderModel("topLeftHoleA1",width/11.228,height/9,width/14.062,height/12.9,1);
        new TableBorderModel("topLeftHoleA2",width/16,height/5.9130,width/22.9299,height/7.15789,2);
        new TableBorderModel("bottomLeftHoleB1",width/16,height/1.21429,width/22.9299,height/1.16587,1);
        new TableBorderModel("bottomLeftHoleB2",width/11.175561,height/1.13145,width/14.221220,height/1.089777,2);
        new TableBorderModel("bottomMiddleHoleC1",width/2.165,height/1.13145,width/2.128,height/1.09149,1);
        new TableBorderModel("bottomMiddleHoleC2",width/1.9048,height/1.13145,width/1.935,height/1.09149,2);
        new TableBorderModel("bottomRightHoleD1",width/1.10919,height/1.13145,width/1.087,height/1.09149,1);
        new TableBorderModel("bottomRightHoleD2",width/1.069,height/1.21864,width/1.047,height/1.16638,2);
        new TableBorderModel("topRightHoleE1",width/1.069,height/5.9130,width/1.047,height/7.15789,1);
        new TableBorderModel("topRightHoleE2",width/1.10719,height/9,width/1.084,height/13.302,2);
        new TableBorderModel("topMiddleHoleF1",width/1.9115,height/9,width/1.931,height/13.302,1);
        new TableBorderModel("topMiddleHoleF2",width/2.16,height/9,width/2.130,height/13.302,2);


        new TableBorderModel("upLeftLine",width/11.228, height/9, width/2.16, height/9,0.9,-0.9);
        new TableBorderModel("upRightLine",width/1.9115, height/9, width/1.10719, height/9,0.9,-0.9);
        new TableBorderModel("downLeftLine",width/11.175561, height/1.13145, width/2.165, height/1.13145,0.9,-0.9);
        new TableBorderModel("downRightLine",width/1.9048, height/1.13145, width/1.10919, height/1.13145,0.9,-0.9);
        new TableBorderModel("centerLeftLine",width/16, height/5.9130, width/16, height/1.21429,-0.9,0.9);
        new TableBorderModel("centerRightLine",width/1.069, height/5.9130, width/1.069, height/1.21864,-0.9,0.9);


        //Setting the path
        Path playableArea = new Path();
        //Left side
        playableArea.getElements().add(new MoveTo(width/22.9299,height/7.15789));
        playableArea.getElements().add(new LineTo(width/16,height/5.9130));
        playableArea.getElements().add(new LineTo(width/16, height/1.21429));
        playableArea.getElements().add(new LineTo(width/22.9299,height/1.16587));
        playableArea.getElements().add(new ArcTo(cornerHoleRadius,cornerHoleRadius,0,width/14.221220,height/1.089777,true,false));

        //Bottom side
        playableArea.getElements().add(new LineTo(width/11.175561,height/1.13145));
        playableArea.getElements().add(new LineTo(width/2.165, height/1.13145));
        playableArea.getElements().add(new LineTo(width/2.128,height/1.09149));

        playableArea.getElements().add(new ArcTo(centerHoleRadius,cornerHoleRadius,0,width/1.935,height/1.09149,true,false));


        playableArea.getElements().add(new LineTo(width/1.9048,height/1.13145));
        playableArea.getElements().add(new LineTo(width/1.10919, height/1.13145));
        playableArea.getElements().add(new LineTo(width/1.087,height/1.09149));

        playableArea.getElements().add(new ArcTo(cornerHoleRadius,cornerHoleRadius,0,width/1.047,height/1.16638,true,false));


        //Right side

        playableArea.getElements().add(new LineTo(width/1.047,height/1.16638));
        playableArea.getElements().add(new LineTo(width/1.069,height/1.21864));
        playableArea.getElements().add(new LineTo(width/1.069, height/5.9130));
        playableArea.getElements().add(new LineTo(width/1.047,height/7.15789));
        playableArea.getElements().add(new ArcTo(cornerHoleRadius,cornerHoleRadius,0,width/1.084,height/13.302,true,false));


        //Top Side

        playableArea.getElements().add(new LineTo(width/1.10719,height/9));
        playableArea.getElements().add(new LineTo(width/1.9115, height/9));
        playableArea.getElements().add(new LineTo(width/1.931,height/13.302));

        playableArea.getElements().add(new ArcTo(centerHoleRadius,cornerHoleRadius,0,width/2.130,height/13.302,true,false));


        playableArea.getElements().add(new LineTo(width/2.16,height/9));
        playableArea.getElements().add(new LineTo(width/11.228, height/9));
        playableArea.getElements().add(new LineTo(width/14.062,height/12.9));
        playableArea.getElements().add(new ArcTo(cornerHoleRadius,cornerHoleRadius,0,width/22.9299,height/7.15789,true,false));


        playableArea.setLayoutX(table.getLayoutX()+(width/43.2));
        playableArea.setLayoutY(table.getLayoutY()+(width/16.));
        playableArea.setFill(Color.TRANSPARENT);

        Rectangle screen = new Rectangle(Screen.getPrimary().getVisualBounds().getWidth(),Screen.getPrimary().getVisualBounds().getHeight());
        TableBorderModel.tableBorderArea = Shape.subtract(screen,playableArea);
        TableBorderModel.tableBorderArea.setVisible(false);

        //

        accessibleArea = new Polyline(width/11.228, height/9+(2* BallModel.RADIUS), width/1.10719, height/9+(2* BallModel.RADIUS), width/1.069-(2* BallModel.RADIUS),height/5.9130,width/1.069-(2* BallModel.RADIUS), height/1.21864,width/1.10919,height/1.13145-(2* BallModel.RADIUS),width/11.175561, height/1.13145-(2* BallModel.RADIUS),width/16+(2* BallModel.RADIUS),height/1.21429,width/16+(2* BallModel.RADIUS), height/5.9130,width/11.228,height/9+(2* BallModel.RADIUS));
        accessibleArea.setFill(Color.TRANSPARENT);
        accessibleArea.setStroke(Color.TRANSPARENT);
        //accessibleArea.setVisible(false);


        for (TableBorderModel tbm:TableBorderModel.tableBorder) {
            tbm.setVisible(false);
        }

        root.getChildren().addAll(TableBorderModel.tableBorderArea);
        table.getChildren().addAll(TableBorderModel.tableBorder);
        table.getChildren().addAll(accessibleArea);
    }

    public void assignBallsInTableView(int paneNumber, ArrayList<BallModel> ballModels) {
        int neededEmptyPlaces = 7 - ballModels.size();
        if (paneNumber == 1) {
            firstPlayerRemainingBalls.getChildren().clear();
            for (int i = 0; i < ballModels.size(); i++) {
                BallView bView = new BallView(ResourcesLoader.ballImages.get(ballModels.get(i).getNumber() - 1), BallModel.RADIUS);
                StackPane remainingBall = new StackPane();
                remainingBall.getChildren().addAll(createNeededCircles(), bView.getBall());
                firstPlayerRemainingBalls.getChildren().add(remainingBall);
            }
            if (neededEmptyPlaces != 0) {
                for (int i = 0; i < neededEmptyPlaces; i++) {
                    StackPane remainingBall = new StackPane();
                    remainingBall.getChildren().addAll(createNeededCircles());
                    firstPlayerRemainingBalls.getChildren().add(remainingBall);
                }
            }
        } else {
            secondPlayerRemainingBalls.getChildren().clear();
            for (int i = 0; i < ballModels.size(); i++) {
                BallView bView = new BallView(ResourcesLoader.ballImages.get(ballModels.get(i).getNumber() - 1), BallModel.RADIUS);
                StackPane remainingBall = new StackPane();
                remainingBall.getChildren().addAll(createNeededCircles(), bView.getBall());
                secondPlayerRemainingBalls.getChildren().add(remainingBall);
            }
            if (neededEmptyPlaces != 0) {
                for (int i = 0; i < neededEmptyPlaces; i++) {
                    StackPane remainingBall = new StackPane();
                    remainingBall.getChildren().addAll(createNeededCircles());
                    secondPlayerRemainingBalls.getChildren().add(remainingBall);
                }
            }
        }
    }
    public Circle createNeededCircles() {
        Circle ballPlace = new Circle();
        ballPlace.setRadius(BallModel.RADIUS);
        ballPlace.setFill(Color.valueOf("#191e24"));
        ballPlace.setStroke(Color.valueOf("#667d99"));
        ballPlace.setStrokeWidth(2);
        return ballPlace;
    }

    public Label getPlayer1Lbl() {
        return player1Lbl;
    }

    public void setPlayer1Lbl(Label player1Lbl) {
        this.player1Lbl = player1Lbl;
    }

    public Label getPlayer2Lbl() {
        return player2Lbl;
    }

    public void setPlayer2Lbl(Label player2Lbl) {
        this.player2Lbl = player2Lbl;
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

    public Shape getAccessibleArea() {
        return accessibleArea;
    }

    public Label getPlayersScore() {
        return playersScore;
    }

    public Line getWhiteLine() {
        return whiteLine;
    }

    public void removeRemainingBallsSection() {
        firstPlayerRemainingBalls.getChildren().clear();
        secondPlayerRemainingBalls.getChildren().clear();
        firstPlayerRemainingBalls.setVisible(false);
        secondPlayerRemainingBalls.setVisible(false);
    }

    public void createRemainingBallSection() {

        firstPlayerRemainingBalls.setVisible(true);
        secondPlayerRemainingBalls.setVisible(true);

        for (int i = 0; i < 7; i++) {
            StackPane remainingBall = new StackPane();
            remainingBall.getChildren().add(createNeededCircles());
            firstPlayerRemainingBalls.getChildren().add(remainingBall);
        }
        for (int i = 0; i < 7; i++) {
            StackPane remainingBall = new StackPane();
            remainingBall.getChildren().add(createNeededCircles());
            secondPlayerRemainingBalls.getChildren().add(remainingBall);
        }

    }
}

