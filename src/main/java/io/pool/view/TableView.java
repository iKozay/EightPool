package io.pool.view;

import io.pool.controller.MainMenuController;
import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.model.TableBorderModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class TableView {

    private final AnchorPane anchorPane;
    private final Pane table; // all components of the table
    private final ArrayList<Circle> holes = new ArrayList<>();
    private final static int width = (int) (game.eightPoolTableWidth/1.5); // the width of the pane
    private final static int height = (int) (game.eightPoolTableHeight/1.5); // the height of the pane
    private int cornerHoleRadius = (int) (width/36);
    private int centerHoleRadius = (int) (width/43.2);
    private double tableHeight;

    boolean selectionCircleClicked;

    public TableView(Pane root) throws MalformedURLException {

        BallModel.RADIUS = getTableWidth()/70;

        double layoutX = game.eightPoolTableX + 0.1*game.eightPoolTableX; // the XPosition in the general pane
        double layoutY = game.eightPoolTableY + 0.1*game.eightPoolTableY; // the YPosition in the general pane

        anchorPane = new AnchorPane();

        anchorPane.setPrefWidth(game.eightPoolTableWidth);
        anchorPane.setPrefHeight(game.eightPoolTableHeight);

        table = new Pane();
        table.setPadding(new Insets(width/43.2, 0, 0, 0));
        table.setPrefWidth(width);
        table.setPrefHeight(height);
        table.setLayoutX(layoutX);
        table.setLayoutY(layoutY);

        ImageView tableImageView = new ImageView(); // the image view of the table
        Image image = new Image(new File("src/main/resources/tableImage/finalTable.png").toURI().toURL().toExternalForm());
        tableImageView.setImage(image);
        tableImageView.setFitWidth(width);
        tableImageView.setFitHeight(height);

        table.getChildren().addAll(tableImageView);

        GridPane playersIcon = new GridPane();
        playersIcon.setHgap(width/21.6);
        playersIcon.setPadding(new Insets(0,0,width/21.6,0));
        playersIcon.setAlignment(Pos.CENTER);
        playersIcon.setPrefWidth(width);
        playersIcon.setPrefHeight(height/9);
        Button player1Lbl = new Button("player1");
        player1Lbl.setPrefWidth((width/2) - width/43.2);
        player1Lbl.setStyle("-fx-background-color: #3D4956");
        player1Lbl.setTextFill(Color.WHITE);
        player1Lbl.setFont(Font.font("Verdana", FontWeight.BOLD, width/43.2));
        Button player2Lbl = new Button("player2");
        player2Lbl.setPrefWidth(width/2 - width/43.2);
        player2Lbl.setStyle("-fx-background-color: #3D4956");
        player2Lbl.setTextFill(Color.WHITE);
        player2Lbl.setFont(Font.font("Verdana", FontWeight.BOLD, width/43.2));
        playersIcon.add(player1Lbl, 0,0);
        playersIcon.add(player2Lbl, 1,0);

        VBox rightContainer = new VBox();
        rightContainer.setSpacing(width/43.2);
        rightContainer.setMaxWidth(width/2.7);
        rightContainer.setAlignment(Pos.TOP_CENTER);
        rightContainer.setPadding(new Insets(0, 25, 0, 0));

        GridPane principalBar = new GridPane();
        principalBar.setAlignment(Pos.CENTER_LEFT);
        principalBar.setMaxHeight(height/7.8);
        principalBar.setMaxWidth(width/2.7);
        principalBar.setStyle("-fx-background-color: #3D4956");

        Button backButton, ballsButton, tableButton, cueButton;

        ImageView leaveArrowImageView = new ImageView();
        Image leaveImage = new Image(new File("src/main/resources/UI icons/arrow.png").toURI().toURL().toExternalForm());
        leaveArrowImageView.setImage(leaveImage);
        leaveArrowImageView.setFitWidth(width/36);
        leaveArrowImageView.setPreserveRatio(true);
        backButton = new Button("");
        backButton.setGraphic(leaveArrowImageView);
        backButton.setContentDisplay(ContentDisplay.CENTER);
        backButton.setStyle("-fx-background-color: red");
        backButton.setMaxWidth(width/36);
        backButton.setMaxHeight(height/7.8);
        backButton.setOnAction(e->{
            MainMenuController.gotoMainMenu();
        });

        ballsButton = new Button("Balls");
        ballsButton.setTextFill(Color.WHITE);
        ballsButton.setFont(Font.font("Verdana", FontWeight.BOLD, width/72));
        ballsButton.setStyle("-fx-background-color: transparent");
        ballsButton.setPrefWidth(width/10.8);

        tableButton = new Button("Table");
        tableButton.setTextFill(Color.WHITE);
        tableButton.setFont(Font.font("Verdana", FontWeight.BOLD, width/72));
        tableButton.setStyle("-fx-background-color: transparent");
        tableButton.setPrefWidth(width/10.8);

        cueButton = new Button("Cue");
        cueButton.setTextFill(Color.WHITE);
        cueButton.setFont(Font.font("Verdana", FontWeight.BOLD, width/72));
        cueButton.setStyle("-fx-background-color: transparent");
        cueButton.setPrefWidth(width/10.8);

        principalBar.add(backButton, 0,0);
        principalBar.add(ballsButton, 1,0);
        principalBar.add(tableButton, 2,0);
        principalBar.add(cueButton, 3,0);
        ///////

        FlowPane ballsPrimaryPane = new FlowPane();
        ballsPrimaryPane.setLayoutX(0);
        ballsPrimaryPane.setLayoutY(0);
        ballsPrimaryPane.setMaxWidth(width/2.7);
        ballsPrimaryPane.setPadding(new Insets(25,0,25,0));
        ballsPrimaryPane.setAlignment(Pos.CENTER);
        ballsPrimaryPane.setStyle("-fx-background-color: #3D4956");
        ballsPrimaryPane.setVgap(height/23.4);
        ballsPrimaryPane.setHgap(width/25);

        FlowPane ballsStrokePane = new FlowPane();
        ballsPrimaryPane.setLayoutX(0);
        ballsPrimaryPane.setLayoutY(0);
        ballsStrokePane.setPadding(new Insets(25,0,25,0));
        ballsStrokePane.setAlignment(Pos.CENTER);
        ballsStrokePane.setStyle("-fx-background-color: #3D4956");
        ballsStrokePane.setVgap(height/23.4);
        ballsStrokePane.setHgap(width/30.857);

        StackPane ballsPane = new StackPane();
        ballsPane.setMaxSize(width/2.7, height/2.925);
        ballsPane.getChildren().addAll(ballsPrimaryPane);

        for (int i=1;i<16;i++) {
            BallModel bModel;
            BallView bView;
            bModel = new BallModel(i, new Image(new File("src/main/resources/billiards3D/ball" + i + ".jpg").toURI().toURL().toExternalForm()));
            bView = new BallView(bModel.getImg(),BallModel.RADIUS);

            selectionCircleClicked = false;
            Circle selectionCircle = new Circle(bView.getBall().getRadius() + 3);
            selectionCircle.setFill(Color.YELLOW);
            selectionCircle.setVisible(false);
            bView.getBall().setOnMouseEntered(event -> selectionCircle.setVisible(true));
            bView.getBall().setOnMouseClicked(event -> selectionCircleClicked = true);
            bView.getBall().setOnMouseExited(event -> {
                if (!selectionCircleClicked) selectionCircle.setVisible(false);
            });


            ballsPrimaryPane.getChildren().add(bView.getBall());
            ballsStrokePane.getChildren().add(selectionCircle);
        }


        rightContainer.getChildren().addAll(principalBar, ballsPane);

        anchorPane.getChildren().addAll(rightContainer, playersIcon, table);

        AnchorPane.setLeftAnchor(table, width/43.2);
        AnchorPane.setTopAnchor(playersIcon, 0.0);
        AnchorPane.setLeftAnchor(playersIcon, width/43.2);
        AnchorPane.setTopAnchor(rightContainer, 0.0);
        AnchorPane.setRightAnchor(rightContainer,0.0);

        Line whiteLine = new Line();
        whiteLine.setStartX(width-(width/3.6));
        whiteLine.setStartY(height/8.934);
        whiteLine.setEndX(width-(width/3.6));
        whiteLine.setEndY(height-(height/8.4375));
        whiteLine.setStroke(Color.WHITE);
        whiteLine.setStrokeWidth(3);

        root.getChildren().add(anchorPane); // adding the table to the main pain of the project.

        createHoles();
        createLines();

    }
    public void createHoles(){
        Circle upLeftCorner = new Circle();
        upLeftCorner.setRadius(cornerHoleRadius);
        upLeftCorner.setCenterX(width/22.5);
        upLeftCorner.setCenterY(height/11);
        upLeftCorner.setFill(Color.BLUE);
        //upLeftCorner.setVisible(false);
        holes.add(upLeftCorner);

        Circle downLeftCorner = new Circle();
        downLeftCorner.setRadius(cornerHoleRadius);
        downLeftCorner.setCenterX(width/22.5);
        downLeftCorner.setCenterY(height/1.1);
        downLeftCorner.setFill(Color.BLUE);
//        downLeftCorner.setVisible(false);
        holes.add(downLeftCorner);

        System.out.println("table height" + tableHeight);
        System.out.println("height" + height);

        Circle upRightCorner = new Circle();
        upRightCorner.setRadius(cornerHoleRadius);
        upRightCorner.setCenterX(width/1.05675);
        upRightCorner.setCenterY(height/11);
        upRightCorner.setFill(Color.BLUE);
        //upRightCorner.setVisible(false);
        holes.add(upRightCorner);

        Circle downRightCorner = new Circle();
        downRightCorner.setRadius(cornerHoleRadius);
        downRightCorner.setCenterX(width/1.05675);
        downRightCorner.setCenterY(height/1.1);
        downRightCorner.setFill(Color.BLUE);
        //downRightCorner.setVisible(false);
        holes.add(downRightCorner);

        Circle upCenterCorner = new Circle();
        upCenterCorner.setRadius(centerHoleRadius);
        upCenterCorner.setCenterX((width/2)/1.014);
        upCenterCorner.setCenterY(height/14.268);
        upCenterCorner.setFill(Color.BLUE);
        //upCenterCorner.setVisible(false);
        holes.add(upCenterCorner);

        Circle downCenterCorner = new Circle();
        downCenterCorner.setRadius(centerHoleRadius);
        downCenterCorner.setCenterX((width/2)/1.014);
        downCenterCorner.setCenterY((height)/1.08);
        //downCenterCorner.setVisible(false);
        downCenterCorner.setFill(Color.BLUE);
        holes.add(downCenterCorner);

        table.getChildren().addAll(holes);

    }
    public void createLines(){
        //TODO Add the rest of the lines for the rest of the table border lines
        TableBorderModel upLeftLine = new TableBorderModel(width/11.368, height/9, width/2.16, height/9,1,-1);
        upLeftLine.setStroke(Color.WHITE);
        upLeftLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(upLeftLine);
        //lines.add(upLeftLine);

        TableBorderModel upRightLine = new TableBorderModel(width/1.9115, height/9, width/1.1077, height/9,1,-1);
        upRightLine.setStroke(Color.WHITE);
        upRightLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(upRightLine);

        TableBorderModel downLeftLine = new TableBorderModel(width/11.368, height/1.1359, width/2.16, height/1.1359,1,-1);
        downLeftLine.setStroke(Color.WHITE);
        downLeftLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(downLeftLine);

        TableBorderModel downRightLine = new TableBorderModel(width/1.9115, height/1.1359, width/1.1077, height/1.1359,1,-1);
        downRightLine.setStroke(Color.WHITE);
        downRightLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(downRightLine);

        TableBorderModel centerLeftLine = new TableBorderModel(width/16.6154, height/5.68, width/16.6154, height/1.2316,-1,1);
        centerLeftLine.setStroke(Color.WHITE);
        centerLeftLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(centerLeftLine);

        TableBorderModel centerRightLine = new TableBorderModel(width/1.0693, height/5.68, width/1.0693, height/1.2316,-1,1);
        centerRightLine.setStroke(Color.WHITE);
        centerRightLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(centerRightLine);



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
}

